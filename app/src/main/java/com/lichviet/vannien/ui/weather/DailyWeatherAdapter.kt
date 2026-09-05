package com.lichviet.vannien.ui.weather

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lichviet.vannien.R
import com.lichviet.vannien.data.WeatherRepository

class DailyWeatherAdapter(
    initialItems: List<WeatherRepository.DailyItem> = emptyList()
) : RecyclerView.Adapter<DailyWeatherAdapter.DailyViewHolder>() {

    private val items = mutableListOf<WeatherRepository.DailyItem>().apply {
        addAll(initialItems)
    }

    fun updateData(newItems: List<WeatherRepository.DailyItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_daily_weather, parent, false)
        return DailyViewHolder(view)
    }

    override fun onBindViewHolder(holder: DailyViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class DailyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvDayName: TextView = itemView.findViewById(R.id.tv_day_name)
        private val tvCondition: TextView = itemView.findViewById(R.id.tv_daily_condition)
        private val tvTemp: TextView = itemView.findViewById(R.id.tv_daily_temp)
        private val ivIcon: ImageView = itemView.findViewById(R.id.iv_daily_icon)

        fun bind(item: WeatherRepository.DailyItem) {
            tvDayName.text = item.dayName
            tvCondition.text = item.condition
            tvTemp.text = "${item.tempMin}° - ${item.tempMax}°C"
            ivIcon.setImageResource(R.drawable.ic_weather_sun_cloud)
        }
    }
}
