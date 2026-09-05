package com.lichviet.vannien.ui.month

import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lichviet.vannien.R
import java.util.Calendar

data class CalendarDayModel(
    val solarDay: Int,
    val solarMonth: Int,
    val solarYear: Int,
    val lunarDay: Int,
    val lunarMonth: Int,
    val dayOfWeek: Int,
    val isCurrentMonth: Boolean,
    val isToday: Boolean,
    val isHoangDao: Boolean,
    val isSpecial: Boolean,
    val holidayName: String? = null
) {
    val isSunday: Boolean get() = (dayOfWeek == Calendar.SUNDAY)
    val isSaturday: Boolean get() = (dayOfWeek == Calendar.SATURDAY)
}

class MonthCalendarAdapter(
    private val onDayClick: (CalendarDayModel) -> Unit
) : RecyclerView.Adapter<MonthCalendarAdapter.DayViewHolder>() {

    private val days = mutableListOf<CalendarDayModel>()
    private var selectedDay: CalendarDayModel? = null

    fun submitList(newDays: List<CalendarDayModel>, selected: CalendarDayModel? = null) {
        days.clear()
        days.addAll(newDays)
        selectedDay = selected ?: newDays.find { it.isToday } ?: newDays.firstOrNull { it.isCurrentMonth }
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
        val isSelected = selectedDay?.let {
            it.solarDay == item.solarDay && it.solarMonth == item.solarMonth && it.solarYear == item.solarYear
        } ?: false
        holder.bind(item, isSelected)
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
                // Ngày thuộc tháng trước hoặc tháng sau
                tvSolar.setTextColor(Color.parseColor("#A0AEC0"))
                tvLunar.setTextColor(Color.parseColor("#CBD5E0"))
                tvLunar.setTypeface(null, Typeface.NORMAL)
                dot.visibility = View.INVISIBLE
                itemView.setBackgroundColor(Color.TRANSPARENT)
            } else {
                // Ngày trong tháng hiện tại
                if (isSelected && day.isToday) {
                    itemView.setBackgroundResource(R.drawable.bg_cell_today_selected)
                    tvSolar.setTextColor(Color.WHITE)
                    tvLunar.setTextColor(Color.parseColor("#DCEDC8"))
                } else if (isSelected) {
                    itemView.setBackgroundResource(R.drawable.bg_cell_selected)
                    tvSolar.setTextColor(Color.parseColor("#004D40"))
                    tvLunar.setTextColor(Color.parseColor("#00796B"))
                } else if (day.isToday) {
                    itemView.setBackgroundResource(R.drawable.bg_cell_today)
                    tvSolar.setTextColor(Color.parseColor("#E65100"))
                    tvLunar.setTextColor(Color.parseColor("#F57C00"))
                } else {
                    itemView.setBackgroundColor(Color.TRANSPARENT)
                    // Màu ngày dương lịch theo thứ trong tuần
                    if (day.isSunday) {
                        tvSolar.setTextColor(Color.parseColor("#D32F2F"))
                    } else if (day.isSaturday) {
                        tvSolar.setTextColor(Color.parseColor("#E65100"))
                    } else {
                        tvSolar.setTextColor(Color.parseColor("#1A202C"))
                    }

                    // Màu ngày âm lịch: Mùng 1 & Rằm & Ngày lễ nổi bật màu đỏ
                    if (day.lunarDay == 1 || day.lunarDay == 15 || !day.holidayName.isNullOrEmpty()) {
                        tvLunar.setTextColor(Color.parseColor("#D32F2F"))
                        tvLunar.setTypeface(null, Typeface.BOLD)
                    } else {
                        tvLunar.setTextColor(Color.parseColor("#2E7D32"))
                        tvLunar.setTypeface(null, Typeface.NORMAL)
                    }
                }

                // Dấu chấm chỉ báo Hoàng Đạo / Ngày Lễ
                if (!day.holidayName.isNullOrEmpty() || day.lunarDay == 1 || day.lunarDay == 15) {
                    dot.visibility = View.VISIBLE
                    dot.setBackgroundResource(R.drawable.bg_dot_red)
                } else if (day.isHoangDao) {
                    dot.visibility = View.VISIBLE
                    dot.setBackgroundResource(R.drawable.bg_dot_gold)
                } else {
                    dot.visibility = View.INVISIBLE
                }
            }

            itemView.setOnClickListener {
                selectedDay = day
                notifyDataSetChanged()
                onDayClick(day)
            }
        }
    }
}
