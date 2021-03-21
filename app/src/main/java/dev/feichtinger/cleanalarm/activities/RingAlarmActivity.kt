package dev.feichtinger.cleanalarm.activities

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PowerManager
import android.widget.TextView
import dev.feichtinger.cleanalarm.R
import dev.feichtinger.cleanalarm.background.RingtonePlayingService
import dev.feichtinger.cleanalarm.background.turnScreenOnAndKeyguardOff
import java.util.*

class RingAlarmActivity : ThemedActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ring_alarm)

        val calendar = Calendar.getInstance()
        val timeTV = findViewById<TextView>(R.id.currentTimeTv)
        timeTV.text = getString(
            R.string.time,
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE)
        )

        val wakeLock = turnScreenOnAndKeyguardOff()
        stopRinging(wakeLock)
    }

    private fun stopRinging(wakeLock: PowerManager.WakeLock) {
        val ringIntent = Intent(applicationContext, RingtonePlayingService::class.java)
        applicationContext.stopService(ringIntent)
        (applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).cancelAll()
        if (wakeLock.isHeld)
            wakeLock.release()
    }
}
