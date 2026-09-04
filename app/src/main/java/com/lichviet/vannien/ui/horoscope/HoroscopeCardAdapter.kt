package com.lichviet.vannien.ui.horoscope

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lichviet.vannien.R

data class HoroscopeFeature(
    val id: Int,
    val title: String,
    val iconResId: Int
)

class HoroscopeCardAdapter(
    private val features: List<HoroscopeFeature>,
    private val onItemClick: (HoroscopeFeature) -> Unit
) : RecyclerView.Adapter<HoroscopeCardAdapter.CardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_horoscope_card, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(features[position])
    }

    override fun getItemCount(): Int = features.size

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivIcon: ImageView = itemView.findViewById(R.id.iv_horo_icon)
        private val tvTitle: TextView = itemView.findViewById(R.id.tv_horo_title)

        fun bind(feature: HoroscopeFeature) {
            ivIcon.setImageResource(feature.iconResId)
            tvTitle.text = feature.title
            itemView.setOnClickListener { onItemClick(feature) }
        }
    }
}
