package dev.feichtinger.cleanalarm.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.scalified.fab.ActionButton
import dev.feichtinger.cleanalarm.R
import dev.feichtinger.cleanalarm.activities.ModAlarmActivity
import dev.feichtinger.cleanalarm.model.Alarm
import dev.feichtinger.cleanalarm.ui.AlarmRecyclerViewAdapter
import java.util.*


class AlarmFragment : Fragment() {

    private val fab by lazy { view?.findViewById<ActionButton>(R.id.alarm_fab)!! }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_alarm, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initFab()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val alarmList = arrayListOf<Alarm>()
        alarmList.add(Alarm("Active Test", true, Calendar.getInstance()))
        alarmList.add(Alarm("Inactive Test", false, Calendar.getInstance()))
        alarmList.add(Alarm("", false, Calendar.getInstance()))

        val rv = view.findViewById<RecyclerView>(R.id.alarmRV)
        rv.layoutManager = LinearLayoutManager(activity)
        rv.adapter = AlarmRecyclerViewAdapter(alarmList)
        rv.isNestedScrollingEnabled = false
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
            /*val calendar = Calendar.getInstance()
            calendar.set(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                calendar.get(Calendar.SECOND) + 5
            )

            Toast.makeText(
                context, "Alarm set for: ${
                    calendar.get(Calendar.HOUR_OF_DAY).toString().padStart(2, '0')
                }:${
                    calendar.get(Calendar.MINUTE).toString().padStart(2, '0')
                }:${
                    calendar.get(Calendar.SECOND).toString().padStart(2, '0')
                }", Toast.LENGTH_LONG
            ).show()
            requireContext().scheduleNotification(Alarm("Wake up", true, calendar))
             */
            startActivityForResult(Intent(context, ModAlarmActivity::class.java), 1)
        }
        fab.show()
    }
}