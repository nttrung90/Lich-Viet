package com.lichviet.vannien.ui.weather

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CityAdapter(
    private val allCities: List<String>,
    private val onCitySelected: (String) -> Unit
) : RecyclerView.Adapter<CityAdapter.CityViewHolder>() {

    private val displayedCities = mutableListOf<String>().apply { addAll(allCities) }

    fun filter(query: String) {
        displayedCities.clear()
        if (query.isBlank()) {
            displayedCities.addAll(allCities)
        } else {
            displayedCities.addAll(allCities.filter { it.contains(query, ignoreCase = true) })
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
        return CityViewHolder(view)
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        val city = displayedCities[position]
        holder.bind(city)
    }

    override fun getItemCount(): Int = displayedCities.size

    inner class CityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val text1: TextView = itemView.findViewById(android.R.id.text1)

        fun bind(city: String) {
            text1.text = city
            itemView.setOnClickListener { onCitySelected(city) }
        }
    }
}
