package dev.feichtinger.cleanalarm.activities

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import dev.feichtinger.cleanalarm.R

abstract class ThemedActivity : AppCompatActivity() {
    private val sharedPref by lazy { getSharedPreferences("prefs", Context.MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {

        if (sharedPref.getBoolean("isDark", isSystemDarkTheme())
        )
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        when (sharedPref.getInt("selectedTheme", 0)) {
            1 ->
                if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
                    setTheme(R.style.Theme_CleanAlarm_Night_Blue)
                else
                    setTheme(R.style.Theme_CleanAlarm_Day_Blue)
            2 ->
                if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
                    setTheme(R.style.Theme_CleanAlarm_Night_Purple)
                else
                    setTheme(R.style.Theme_CleanAlarm_Day_Purple)
            3 ->
                if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
                    setTheme(R.style.Theme_CleanAlarm_Night_Red)
                else
                    setTheme(R.style.Theme_CleanAlarm_Day_Red)
            4 ->
                if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
                    setTheme(R.style.Theme_CleanAlarm_Night_Orange)
                else
                    setTheme(R.style.Theme_CleanAlarm_Day_Orange)
            else ->
                if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
                    setTheme(R.style.Theme_CleanAlarm_Night)
                else
                    setTheme(R.style.Theme_CleanAlarm_Day)
        }

        super.onCreate(savedInstanceState)
    }


    private fun isSystemDarkTheme(): Boolean {
        return when (applicationContext.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> true
            else -> false
        }
    }
}