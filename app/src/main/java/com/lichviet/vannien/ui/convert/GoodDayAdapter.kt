package com.lichviet.vannien.ui.convert

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lichviet.vannien.calendar.LunarCalendarEngine
import com.lichviet.vannien.databinding.ItemGoodDayBinding

class GoodDayAdapter(
    private val onDayClick: (LunarCalendarEngine.LunarDate) -> Unit
) : ListAdapter<LunarCalendarEngine.LunarDate, GoodDayAdapter.GoodDayViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoodDayViewHolder {
        val binding = ItemGoodDayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GoodDayViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GoodDayViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class GoodDayViewHolder(private val binding: ItemGoodDayBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: LunarCalendarEngine.LunarDate) {
            binding.tvItemSolarDay.text = item.solarDay.toString()
            binding.tvItemDayOfWeek.text = item.dayOfWeek

            val monthText = if (item.isLeap) "Tháng ${item.month} (Nhuận)" else "Tháng ${item.month}"
            binding.tvItemLunarDate.text = "Âm lịch: Ngày ${item.day} $monthText (${item.canChiYear})"
            binding.tvItemCanchiDay.text = "Ngày ${item.canChiDay} • Trực: ${item.truc} • Tiết: ${item.tietKhi}"

            val hoursDisplay = if (item.hoangDaoHours.isNotEmpty()) {
                "Giờ đẹp: " + item.hoangDaoHours.take(4).joinToString(", ") { it.name.split(" ").firstOrNull() ?: it.name }
            } else {
                "Giờ Hoàng Đạo trong ngày"
            }
            binding.tvItemGoodHours.text = hoursDisplay

            binding.root.setOnClickListener { onDayClick(item) }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<LunarCalendarEngine.LunarDate>() {
        override fun areItemsTheSame(oldItem: LunarCalendarEngine.LunarDate, newItem: LunarCalendarEngine.LunarDate): Boolean {
            return oldItem.solarDay == newItem.solarDay &&
                    oldItem.day == newItem.day &&
                    oldItem.month == newItem.month &&
                    oldItem.year == newItem.year
        }

        override fun areContentsTheSame(oldItem: LunarCalendarEngine.LunarDate, newItem: LunarCalendarEngine.LunarDate): Boolean {
            return oldItem == newItem
        }
    }
}
