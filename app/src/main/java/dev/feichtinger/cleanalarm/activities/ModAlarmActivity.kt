package dev.feichtinger.cleanalarm.activities

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TimePicker
import com.google.android.material.textview.MaterialTextView
import dev.feichtinger.cleanalarm.R
import dev.feichtinger.cleanalarm.getColorFromAttr


class ModAlarmActivity : ThemedActivity() {

    private val sharedPref by lazy { this.getSharedPreferences("prefs", Context.MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mod_alarm)

        initTimePicker(this.resources.configuration.orientation)
    }

    private fun initTimePicker(orientation: Int) {
        val timePicker = findViewById<TimePicker>(R.id.timePicker1)
        timePicker.setIs24HourView(sharedPref?.getBoolean("is24hFormat", true)!!)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ((timePicker.getChildAt(0) as LinearLayout).getChildAt(2) as MaterialTextView).setTextColor(
                getColorFromAttr(R.attr.colorPrimary)
            )
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                ((timePicker.getChildAt(0) as LinearLayout).getChildAt(4) as LinearLayout).getChildAt(
                    0
                ).alpha = 0.4f
            } else {
                (((timePicker.getChildAt(0) as LinearLayout).getChildAt(2) as LinearLayout).getChildAt(
                    2
                ) as LinearLayout).getChildAt(0).alpha = 0.4f
            }
        }
    }
}