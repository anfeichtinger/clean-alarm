package dev.feichtinger.cleanalarm.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.scalified.fab.ActionButton
import dev.feichtinger.cleanalarm.R
import dev.feichtinger.cleanalarm.activities.ModAlarmActivity

class StopwatchFragment : Fragment() {

    private val fab by lazy { view?.findViewById<ActionButton>(R.id.stopwatch_fab)!! }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_stopwatch, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initFab()
    }

    override fun onStop() {
        fab.hide()
        super.onStop()
    }

    override fun onPause() {
        fab.hide()
        super.onPause()
    }

    override fun onResume() {
        fab.show()
        super.onResume()
    }

    private fun initFab() {
        fab.setOnClickListener {

        }
        fab.show()
    }
}