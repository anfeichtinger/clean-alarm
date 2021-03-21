package dev.feichtinger.cleanalarm.background

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Looper
import android.widget.Toast
import dev.feichtinger.cleanalarm.R
import dev.feichtinger.cleanalarm.model.Alarm
import java.util.*

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val sharedPrefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val alarm = Alarm.fromJson(intent.getStringExtra("alarm")!!)
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        when (intent.action) {
            context.getString(R.string.snooze_alarm_action) -> {
                snoozeAlarm(context, alarm, notificationManager, sharedPrefs)
            }
            context.getString(R.string.stop_alarm_action) -> {
                stopAlarm(context, alarm, notificationManager)
            }
            else -> {
                startRingingService(context, sharedPrefs, alarm)
            }
        }
    }

    companion object {
        fun build(context: Context, alarm: Alarm): Intent {
            return Intent(context, AlarmReceiver::class.java).putExtra("alarm", alarm.toJson())
        }
    }

    private fun startRingingService(
        context: Context,
        sharedPrefs: SharedPreferences,
        alarm: Alarm
    ) {
        val notification = context.showNotificationWithFullScreenIntent(alarm)
        val ringIntent = Intent(context, RingtonePlayingService::class.java)
        ringIntent.putExtra("gradIncrease", sharedPrefs.getBoolean("gradIncreaseVol", false))
        ringIntent.putExtra("notification", notification)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(ringIntent)
        } else {
            (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).notify(
                alarm.hashCode(),
                notification
            )
            context.startService(ringIntent)
        }
    }

    private fun cancelNotification(
        notificationManager: NotificationManager,
        alarm: Alarm,
    ) {
        if (notificationManager.activeNotifications.isNotEmpty()) {
            notificationManager.cancel(alarm.hashCode())
        }
    }

    private fun stopAlarm(
        context: Context, alarm: Alarm,
        notificationManager: NotificationManager
    ) {
        val ringIntent = Intent(context, RingtonePlayingService::class.java)
        context.stopService(ringIntent)
        cancelNotification(notificationManager, alarm)

        if (Looper.myLooper() == null) {
            Looper.prepare()
        }
        Toast.makeText(context, "Alarm turned off", Toast.LENGTH_LONG).show()
    }

    private fun snoozeAlarm(
        context: Context, alarm: Alarm,
        notificationManager: NotificationManager,
        sharedPrefs: SharedPreferences
    ) {
        val ringIntent = Intent(context, RingtonePlayingService::class.java)
        context.stopService(ringIntent)
        cancelNotification(notificationManager, alarm)

        alarm.time.set(
            alarm.time.get(Calendar.YEAR),
            alarm.time.get(Calendar.MONTH),
            alarm.time.get(Calendar.DAY_OF_MONTH),
            alarm.time.get(Calendar.HOUR_OF_DAY),
            Calendar.getInstance()
                .get(Calendar.MINUTE) + sharedPrefs.getInt("snooze_for", 5),
            alarm.time.get(Calendar.SECOND)
        )
        if (Looper.myLooper() == null) {
            Looper.prepare()
        }
        Toast.makeText(
            context,
            "Snoozed until: ${
                alarm.time.get(Calendar.HOUR_OF_DAY).toString().padStart(2, '0')
            }:${
                alarm.time.get(Calendar.MINUTE).toString().padStart(2, '0')
            }",
            Toast.LENGTH_LONG
        ).show()
        context.scheduleNotification(alarm)
    }
}