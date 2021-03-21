package dev.feichtinger.cleanalarm.activities

import android.os.Bundle
import androidx.fragment.app.commit
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import dev.feichtinger.cleanalarm.R
import dev.feichtinger.cleanalarm.fragments.AlarmFragment
import dev.feichtinger.cleanalarm.fragments.SettingsFragment
import dev.feichtinger.cleanalarm.fragments.StopwatchFragment
import dev.feichtinger.cleanalarm.fragments.TimerFragment

class SkeletonActivity : ThemedActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_skeleton)

        val menu = findViewById<ChipNavigationBar>(R.id.bottomNavigationBar)
        val fm = supportFragmentManager
        if (intent.getBooleanExtra("isRestart", false)) {
            menu.setItemSelected(R.id.settingsMenuItem, true)
            val settingsFragment = SettingsFragment()
            fm.commit {
                setCustomAnimations(0, 0)
                replace(
                    R.id.fragmentContainer,
                    settingsFragment,
                    settingsFragment.javaClass.simpleName
                )
            }
        } else {
            menu.setItemSelected(R.id.alarmMenuItem, true)
            val alarmFragment = AlarmFragment()
            fm.commit {
                setCustomAnimations(0, 0)
                replace(
                    R.id.fragmentContainer,
                    alarmFragment,
                    alarmFragment.javaClass.simpleName
                )
            }
        }

        menu.setOnItemSelectedListener { id ->
            when (id) {
                R.id.timerMenuItem -> {
                    val timerFragment = TimerFragment()
                    fm.commit {
                        setCustomAnimations(
                            android.R.anim.fade_in,
                            android.R.anim.fade_out
                        )
                        replace(
                            R.id.fragmentContainer,
                            timerFragment,
                            timerFragment.javaClass.simpleName
                        )
                    }
                }
                R.id.stopwatchMenuItem -> {
                    val stopwatchFragment = StopwatchFragment()
                    fm.commit {
                        setCustomAnimations(
                            android.R.anim.fade_in,
                            android.R.anim.fade_out
                        )
                        replace(
                            R.id.fragmentContainer,
                            stopwatchFragment,
                            stopwatchFragment.javaClass.simpleName
                        )
                    }
                }
                R.id.settingsMenuItem -> {
                    val settingsFragment = SettingsFragment()
                    fm.commit {
                        setCustomAnimations(
                            android.R.anim.fade_in,
                            android.R.anim.fade_out
                        )
                        replace(
                            R.id.fragmentContainer,
                            settingsFragment,
                            settingsFragment.javaClass.simpleName
                        )
                    }
                }
                else -> {
                    val alarmFragment = AlarmFragment()
                    fm.commit {
                        setCustomAnimations(
                            android.R.anim.fade_in,
                            android.R.anim.fade_out
                        )
                        replace(
                            R.id.fragmentContainer,
                            alarmFragment,
                            alarmFragment.javaClass.simpleName
                        )
                    }
                }
            }
        }
    }
}