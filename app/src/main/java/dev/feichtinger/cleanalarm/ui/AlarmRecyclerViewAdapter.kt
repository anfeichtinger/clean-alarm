package dev.feichtinger.cleanalarm.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import dev.feichtinger.cleanalarm.R
import dev.feichtinger.cleanalarm.getColorFromAttr
import dev.feichtinger.cleanalarm.model.Alarm
import java.util.*

class AlarmRecyclerViewAdapter(private val alarms: ArrayList<Alarm>) :
    RecyclerView.Adapter<AlarmRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater, parent)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val alarm: Alarm = alarms[position]
        holder.bind(alarm, this)
    }

    override fun getItemCount() = alarms.size

    class ViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.list_item_alarm, parent, false)) {

        private var alarmLabelTv: TextView? = null
        private var alarmTimeTv: TextView? = null
        private var alarmSw: SwitchCompat? = null

        init {
            alarmLabelTv = itemView.findViewById(R.id.tvAlarmLabel)
            alarmTimeTv = itemView.findViewById(R.id.tvAlarmTime)
            alarmSw = itemView.findViewById(R.id.swAlarm)
        }

        fun bind(alarm: Alarm, alarmRecyclerViewAdapter: AlarmRecyclerViewAdapter) {
            if (alarm.label.isEmpty()) {
                alarmLabelTv?.visibility = View.GONE
            } else {
                alarmLabelTv?.text = alarm.label
            }
            alarmTimeTv?.text = itemView.context.getString(
                R.string.time,
                alarm.time.get(Calendar.HOUR_OF_DAY),
                alarm.time.get(Calendar.MINUTE)
            )
            alarmTimeTv?.typeface =
                ResourcesCompat.getFont(itemView.context, R.font.poppins_semi_bold)
            alarmSw?.isChecked = alarm.active
            if (!alarm.active) {
                alarmTimeTv?.setTextColor(itemView.context.getColorFromAttr(R.attr.mySecondaryTextColor))
            } else {
                alarmTimeTv?.setTextColor(itemView.context.getColorFromAttr(R.attr.colorPrimary))
            }

            alarmSw?.setOnCheckedChangeListener { _, isChecked ->
                alarm.active = isChecked
                alarmRecyclerViewAdapter.notifyDataSetChanged()
            }
        }
    }
}
