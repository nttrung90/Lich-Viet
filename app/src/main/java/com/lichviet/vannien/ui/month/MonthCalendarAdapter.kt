package com.lichviet.vannien.ui.month

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lichviet.vannien.R
import com.lichviet.vannien.calendar.LunarCalendarEngine

data class CalendarDayModel(
    val solarDay: Int,
    val solarMonth: Int,
    val solarYear: Int,
    val lunarDay: Int,
    val lunarMonth: Int,
    val isCurrentMonth: Boolean,
    val isToday: Boolean,
    val isHoangDao: Boolean,
    val isSpecial: Boolean
)

class MonthCalendarAdapter(
    private val onDayClick: (CalendarDayModel) -> Unit
) : RecyclerView.Adapter<MonthCalendarAdapter.DayViewHolder>() {

    private val days = mutableListOf<CalendarDayModel>()
    private var selectedDay: CalendarDayModel? = null

    fun submitList(newDays: List<CalendarDayModel>, selected: CalendarDayModel? = null) {
        days.clear()
        days.addAll(newDays)
        selectedDay = selected ?: newDays.find { it.isToday }
        notifyDataSetChanged()
    }

    fun setSelected(day: CalendarDayModel) {
        selectedDay = day
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_calendar_day, parent, false)
        return DayViewHolder(view)
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        val item = days[position]
        holder.bind(item, item == selectedDay)
    }

    override fun getItemCount(): Int = days.size

    inner class DayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvSolar: TextView = itemView.findViewById(R.id.tv_cell_solar_day)
        private val tvLunar: TextView = itemView.findViewById(R.id.tv_cell_lunar_day)
        private val dot: View = itemView.findViewById(R.id.dot_indicator)

        fun bind(day: CalendarDayModel, isSelected: Boolean) {
            tvSolar.text = day.solarDay.toString()
            tvLunar.text = if (day.lunarDay == 1) "${day.lunarDay}/${day.lunarMonth}" else day.lunarDay.toString()

            if (!day.isCurrentMonth) {
                tvSolar.setTextColor(Color.parseColor("#BBBBBB"))
                tvLunar.setTextColor(Color.parseColor("#CCCCCC"))
                dot.visibility = View.INVISIBLE
                itemView.setBackgroundColor(Color.TRANSPARENT)
            } else {
                tvSolar.setTextColor(Color.parseColor("#5C3317"))
                tvLunar.setTextColor(Color.parseColor("#2E7D32"))
                dot.visibility = if (day.isHoangDao || day.isSpecial) View.VISIBLE else View.INVISIBLE

                if (isSelected) {
                    itemView.setBackgroundResource(R.drawable.bg_folk_panel)
                } else if (day.isToday) {
                    itemView.setBackgroundResource(R.drawable.bg_today_badge)
                } else {
                    itemView.setBackgroundColor(Color.TRANSPARENT)
                }
            }

            itemView.setOnClickListener {
                if (day.isCurrentMonth) {
                    selectedDay = day
                    notifyDataSetChanged()
                    onDayClick(day)
                }
            }
        }
    }
}
