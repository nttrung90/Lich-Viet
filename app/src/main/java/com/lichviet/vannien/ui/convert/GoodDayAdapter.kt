package com.lichviet.vannien.ui.convert

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lichviet.vannien.calendar.LunarCalendarEngine

class GoodDayAdapter(
    private val onDayClick: (LunarCalendarEngine.LunarDate) -> Unit
) : RecyclerView.Adapter<GoodDayAdapter.GoodDayViewHolder>() {

    private val items = mutableListOf<LunarCalendarEngine.LunarDate>()

    fun submitList(list: List<LunarCalendarEngine.LunarDate>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoodDayViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_2, parent, false)
        return GoodDayViewHolder(view)
    }

    override fun onBindViewHolder(holder: GoodDayViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class GoodDayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val text1: TextView = itemView.findViewById(android.R.id.text1)
        private val text2: TextView = itemView.findViewById(android.R.id.text2)

        fun bind(item: LunarCalendarEngine.LunarDate) {
            text1.text = "${item.dayOfWeek}, ${item.solarDay}/${item.solarMonth}/${item.solarYear} (Hoàng Đạo)"
            text1.setTextColor(android.graphics.Color.parseColor("#1B5E20"))
            text1.setTypeface(null, android.graphics.Typeface.BOLD)

            val monthText = if (item.isLeap) "Tháng ${item.month} (Nhuận)" else "Tháng ${item.month}"
            text2.text = "Âm lịch: Ngày ${item.day} $monthText - Trực: ${item.truc} - Tiết: ${item.tietKhi}"
            text2.setTextColor(android.graphics.Color.parseColor("#555555"))

            itemView.setOnClickListener { onDayClick(item) }
        }
    }
}
