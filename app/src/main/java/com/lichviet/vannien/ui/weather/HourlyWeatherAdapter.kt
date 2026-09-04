package com.lichviet.vannien.ui.weather

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lichviet.vannien.R
import com.lichviet.vannien.data.WeatherRepository

class HourlyWeatherAdapter(
    private val items: List<WeatherRepository.HourlyItem>
) : RecyclerView.Adapter<HourlyWeatherAdapter.HourlyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_hourly_weather, parent, false)
        return HourlyViewHolder(view)
    }

    override fun onBindViewHolder(holder: HourlyViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class HourlyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTime: TextView = itemView.findViewById(R.id.tv_hour_time)
        private val tvCondition: TextView = itemView.findViewById(R.id.tv_hour_condition)
        private val tvTemp: TextView = itemView.findViewById(R.id.tv_hour_temp)
        private val ivIcon: ImageView = itemView.findViewById(R.id.iv_hour_icon)

        fun bind(item: WeatherRepository.HourlyItem) {
            tvTime.text = item.time
            tvCondition.text = item.condition
            tvTemp.text = "${item.temp}°C"
            ivIcon.setImageResource(R.drawable.ic_weather_sun_cloud)
        }
    }
}
