package dev.feichtinger.cleanalarm.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import dev.feichtinger.cleanalarm.R

class WeekdaySelector(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {
    init {
        inflate(context, R.layout.weekday_selector, this)
        val children = ArrayList<View>()
    }
}