package com.lichviet.vannien.ui.convert

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lichviet.vannien.calendar.LunarCalendarEngine
import com.lichviet.vannien.databinding.ItemGoodDayBinding

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
        val binding = ItemGoodDayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GoodDayViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GoodDayViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

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
}
