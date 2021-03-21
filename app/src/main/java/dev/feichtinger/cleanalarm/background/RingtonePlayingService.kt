package dev.feichtinger.cleanalarm.background

import android.app.Notification
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.*
import java.util.*
import kotlin.concurrent.schedule


class RingtonePlayingService : Service() {
    private val mp by lazy {
        MediaPlayer.create(
            baseContext,
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        )
    }
    private val vibrator by lazy { baseContext.getSystemService(VIBRATOR_SERVICE) as Vibrator }
    private val pattern = longArrayOf(0, 500, 1000)
    private var isPlaying = false

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notification = intent.getParcelableExtra<Notification>("notification")
            startForeground(startId, notification)
        }

        mp.reset()
        mp.setDataSource(this, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))

        if (intent.getBooleanExtra("gradIncrease", false)) {
            gradIncreaseVolume(mp)
        } else {
            mp.setAudioAttributes(
                AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ALARM).build()
            )
        }

        mp.isLooping = true
        mp.prepare()
        mp.start()
        isPlaying = true

        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(
                VibrationEffect.createWaveform(pattern, 0)
            )
        } else {
            vibrator.vibrate(pattern, 0)
        }

        Timer().schedule(
            (getSharedPreferences(
                "prefs",
                Context.MODE_PRIVATE
            ).getInt("stop_after", 5) * 60000).toLong()
        ) { stopSelf() }
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        isPlaying = false
        if (mp.isPlaying)
            mp.stop()
        mp.reset()
        mp.release()
        vibrator.cancel()
    }

    private fun gradIncreaseVolume(mp: MediaPlayer) {

        val volumeList = mutableListOf(
            1f,
            10f,
            20f,
            30f,
            40f,
            50f,
            60f,
            70f,
            80f,
            90f,
            100f
        )

        volumeList.forEachIndexed { index, fl ->
            Handler(mainLooper).postDelayed({
                if (isPlaying) {
                    mp.setVolume(fl, fl)
                    println("Set volume to: $fl")
                }
            }, (index * 5000).toLong())
        }
    }
}