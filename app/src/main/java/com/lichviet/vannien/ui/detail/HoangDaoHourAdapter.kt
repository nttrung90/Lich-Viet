package com.lichviet.vannien.ui.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lichviet.vannien.R
import com.lichviet.vannien.calendar.LunarCalendarEngine

class HoangDaoHourAdapter(
    private val items: List<LunarCalendarEngine.HoangDaoHour>
) : RecyclerView.Adapter<HoangDaoHourAdapter.HourViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_hoang_dao_hour, parent, false)
        return HourViewHolder(view)
    }

    override fun onBindViewHolder(holder: HourViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class HourViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvName: TextView = itemView.findViewById(R.id.tv_hour_name)
        private val tvRange: TextView = itemView.findViewById(R.id.tv_hour_range)
        private val ivIcon: ImageView = itemView.findViewById(R.id.iv_zodiac_icon)

        fun bind(hour: LunarCalendarEngine.HoangDaoHour) {
            tvName.text = hour.name
            tvRange.text = "(${hour.timeRange})"
            ivIcon.setImageResource(R.drawable.ic_yin_yang)
        }
    }
}
