package dev.feichtinger.cleanalarm.fragments

import android.content.Context
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Spinner
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.google.android.material.switchmaterial.SwitchMaterial
import dev.feichtinger.cleanalarm.R
import dev.feichtinger.cleanalarm.activities.SkeletonActivity

class SettingsFragment : Fragment() {
    private val themeSwitch by lazy { view?.findViewById<SwitchMaterial>(R.id.themeSwitch) }
    private val greenButton by lazy { view?.findViewById<ImageButton>(R.id.greenThemeBtn) }
    private val blueButton by lazy { view?.findViewById<ImageButton>(R.id.blueThemeBtn) }
    private val purpleButton by lazy { view?.findViewById<ImageButton>(R.id.purpleThemeBtn) }
    private val redButton by lazy { view?.findViewById<ImageButton>(R.id.redThemeBtn) }
    private val orangeButton by lazy { view?.findViewById<ImageButton>(R.id.orangeThemeBtn) }

    private val use24hSwitch by lazy { view?.findViewById<SwitchMaterial>(R.id.use24hSwitch) }
    private val gradIncreaseVolSwitch by lazy { view?.findViewById<SwitchMaterial>(R.id.gradIncreaseSwitch) }

    private val stopAfterSpinner by lazy { view?.findViewById<Spinner>(R.id.snooze_after_spinner) }
    private val snoozeForSpinner by lazy { view?.findViewById<Spinner>(R.id.snooze_for_spinner) }

    private val sharedPref by lazy { activity?.getSharedPreferences("prefs", Context.MODE_PRIVATE) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_settings, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initThemeSettings()
        initGeneralSettings()
        initAlarmSettings()
    }

    @ColorInt
    private fun Context.getColorFromAttr(
        @AttrRes attrColor: Int,
        typedValue: TypedValue = TypedValue(),
        resolveRefs: Boolean = true
    ): Int {
        theme.resolveAttribute(attrColor, typedValue, resolveRefs)
        return typedValue.data
    }

    private fun setBorder(btn: ImageButton?) {
        if (btn == null)
            return
        val greenBg = greenButton?.background as GradientDrawable; greenBg.setStroke(0, 0)
        val blueBg = blueButton?.background as GradientDrawable; blueBg.setStroke(0, 0)
        val purpleBg = purpleButton?.background as GradientDrawable; purpleBg.setStroke(0, 0)
        val redBg = redButton?.background as GradientDrawable; redBg.setStroke(0, 0)
        val orangeBg = orangeButton?.background as GradientDrawable; orangeBg.setStroke(0, 0)

        val background = btn.background as GradientDrawable
        view?.context?.let { background.setStroke(8, it.getColorFromAttr(R.attr.myTextColor)) }
    }

    private fun restartApp() {
        val i = Intent(requireContext(), SkeletonActivity::class.java)
        i.putExtra("isRestart", true)
        startActivity(i)

        requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        requireActivity().finish()
    }

    private fun initThemeSettings() {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            themeSwitch?.isChecked = true
        }
        requireView().postDelayed({ themeSwitch?.isClickable = true }, 850)

        themeSwitch?.setOnCheckedChangeListener { _, isChecked ->
            sharedPref?.edit()?.putBoolean("isDark", isChecked)?.apply()
            restartApp()
        }

        greenButton?.setOnClickListener {
            sharedPref?.edit()?.putInt("selectedTheme", 0)?.apply()
            restartApp()
        }
        blueButton?.setOnClickListener {
            sharedPref?.edit()?.putInt("selectedTheme", 1)?.apply()
            restartApp()
        }
        purpleButton?.setOnClickListener {
            sharedPref?.edit()?.putInt("selectedTheme", 2)?.apply()
            restartApp()
        }
        redButton?.setOnClickListener {
            sharedPref?.edit()?.putInt("selectedTheme", 3)?.apply()
            restartApp()
        }
        orangeButton?.setOnClickListener {
            sharedPref?.edit()?.putInt("selectedTheme", 4)?.apply()
            restartApp()
        }

        when (sharedPref?.getInt("selectedTheme", 0)) {
            1 -> setBorder(blueButton)
            2 -> setBorder(purpleButton)
            3 -> setBorder(redButton)
            4 -> setBorder(orangeButton)
            else -> setBorder(greenButton)
        }
    }

    private fun initGeneralSettings() {
        // Fill Dropdown with lang strings
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.languages,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
            view?.findViewById<Spinner>(R.id.lang_spinner)?.adapter = adapter
        }
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.week_start,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
            view?.findViewById<Spinner>(R.id.start_week_spinner)?.adapter = adapter
        }

        use24hSwitch?.isChecked = sharedPref?.getBoolean("is24hFormat", true) == true
        use24hSwitch?.setOnCheckedChangeListener { _, isChecked ->
            sharedPref?.edit()?.putBoolean("is24hFormat", isChecked)?.apply()
        }
    }

    private fun initAlarmSettings() {
        // Fill Dropdown with snooze strings
        val snoozeArray = requireContext().resources.getStringArray(R.array.snooze_time)
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.snooze_time,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
            stopAfterSpinner?.adapter = adapter
            stopAfterSpinner?.setSelection(
                snoozeArray.indexOf(snoozeArray
                    .first {
                        it.startsWith(
                            sharedPref!!.getInt("stop_after", 5).toString()
                        )
                    })
            )
            stopAfterSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    sharedPref?.edit()?.putInt(
                        "stop_after",
                        requireContext().resources.getStringArray(R.array.snooze_time)[id.toInt()]
                            .substring(0, 2).trim().toInt()
                    )?.apply()
                }
            }
        }
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.snooze_time,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
            snoozeForSpinner?.adapter = adapter
            snoozeForSpinner?.setSelection(
                snoozeArray.indexOf(snoozeArray
                    .first {
                        it.startsWith(
                            sharedPref!!.getInt("snooze_for", 5).toString()
                        )
                    })
            )
            snoozeForSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    sharedPref?.edit()?.putInt(
                        "snooze_for",
                        requireContext().resources.getStringArray(R.array.snooze_time)[id.toInt()].substring(
                            0,
                            2
                        ).trim().toInt()
                    )?.apply()
                }
            }
        }

        gradIncreaseVolSwitch?.isChecked =
            sharedPref?.getBoolean("gradIncreaseVol", false) == true
        gradIncreaseVolSwitch?.setOnCheckedChangeListener { _, isChecked ->
            sharedPref?.edit()?.putBoolean("gradIncreaseVol", isChecked)?.apply()
        }
    }
}