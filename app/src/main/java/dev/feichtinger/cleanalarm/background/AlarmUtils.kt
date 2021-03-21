package dev.feichtinger.cleanalarm.background

import android.app.*
import android.content.Context
import android.content.Context.POWER_SERVICE
import android.content.Intent
import android.os.Build
import android.os.PowerManager
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import dev.feichtinger.cleanalarm.R
import dev.feichtinger.cleanalarm.activities.RingAlarmActivity
import dev.feichtinger.cleanalarm.model.Alarm
import java.util.*


private fun Context.getFullScreenIntent(alarm: Alarm): PendingIntent {
    val intent = Intent(this, RingAlarmActivity::class.java)
    return PendingIntent.getActivity(this, alarm.hashCode(), intent, 0)
}

private fun NotificationManager.buildChannel() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "Ring Alarm"
        val descriptionText = "This notification shows a ringing alarm"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel("ring_alarm", name, importance).apply {
            description = descriptionText
            setSound(null, null)
        }
        createNotificationChannel(channel)
    }
}

fun Context.showNotificationWithFullScreenIntent(alarm: Alarm): Notification {
    val snoozeIntent = Intent(this, AlarmReceiver::class.java).apply {
        action = getString(R.string.snooze_alarm_action)
        putExtra("alarm", alarm.toJson())
        putExtra("snooze", true)
    }
    val snoozePendingIntent: PendingIntent =
        PendingIntent.getBroadcast(this, alarm.hashCode(), snoozeIntent, 0)

    val turnOffIntent = Intent(this, AlarmReceiver::class.java).apply {
        action = getString(R.string.stop_alarm_action)
        putExtra("alarm", alarm.toJson())
        putExtra("turnoff", true)
    }
    val turnOffPendingIntent: PendingIntent =
        PendingIntent.getBroadcast(this, alarm.hashCode(), turnOffIntent, 0)

    val fullScreenIntent = getFullScreenIntent(alarm)
    val channelId = "ring_alarm"
    val builder =
        NotificationCompat.Builder(this, channelId).setSmallIcon(R.drawable.ic_baseline_alarm_24)
            .setContentTitle(alarm.label)
            .setContentText(
                "${
                    alarm.time.getDisplayName(
                        Calendar.DAY_OF_WEEK,
                        Calendar.SHORT,
                        Locale.getDefault()
                    )
                } ${
                    alarm.time.get(Calendar.HOUR_OF_DAY).toString().padStart(2, '0')
                }:${
                    alarm.time.get(
                        Calendar.MINUTE
                    ).toString().padStart(2, '0')
                }"
            )
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setFullScreenIntent(fullScreenIntent, true)
            .addAction(R.drawable.ic_round_snooze_24, "Snooze", snoozePendingIntent)
            .addAction(R.drawable.ic_baseline_alarm_off_24, "Turn off", turnOffPendingIntent)
            .setTimeoutAfter(
                (getSharedPreferences(
                    "prefs",
                    Context.MODE_PRIVATE
                ).getInt("stop_after", 5) * 60000).toLong()
            )
            .setAutoCancel(true)

    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
        builder.setContentIntent(fullScreenIntent)
    }

    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    with(notificationManager) {
        buildChannel()
        return builder.build()
    }
}

fun Activity.turnScreenOnAndKeyguardOff(): PowerManager.WakeLock {
    val mPowerManager = this.getSystemService(POWER_SERVICE) as PowerManager
    val wakelock = mPowerManager.newWakeLock(
        PowerManager.SCREEN_BRIGHT_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP,
        "AlarmWakelock:"
    )
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
        setShowWhenLocked(true)
        setTurnScreenOn(true)
    } else {
        window.addFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                    or WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON
        )
        wakelock.acquire(5 * 60 * 1000L /*10 minutes*/)
    }
    with(getSystemService(AppCompatActivity.KEYGUARD_SERVICE) as KeyguardManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            requestDismissKeyguard(this@turnScreenOnAndKeyguardOff, null)
        }
    }
    return wakelock
}

fun Context.scheduleNotification(alarm: Alarm) {
    val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
    with(alarmManager) {
        setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            alarm.time.timeInMillis,
            getReceiver(alarm)
        )
    }
}

private fun Context.getReceiver(alarm: Alarm): PendingIntent {
    return PendingIntent.getBroadcast(this, alarm.hashCode(), AlarmReceiver.build(this, alarm), 0)
}