package com.lichviet.vannien.ui.weather

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lichviet.vannien.R
import com.lichviet.vannien.data.WeatherRepository
import com.lichviet.vannien.databinding.ActivityWeatherBinding

class WeatherActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWeatherBinding
    private var currentCity = "Hà Nội"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
        loadWeatherData(currentCity)
    }

    private fun setupListeners() {
        binding.btnWeatherBack.setOnClickListener {
            finish()
        }

        binding.btnSelectCity.setOnClickListener {
            showCitySelectorDialog()
        }

        binding.btnWeatherSettings.setOnClickListener {
            showCitySelectorDialog()
        }
    }

    private fun loadWeatherData(cityName: String) {
        currentCity = cityName
        val weather = WeatherRepository.getWeatherForCity(cityName)

        binding.tvCityName.text = weather.city
        binding.tvTempHuge.text = "${weather.tempCurrent}°"
        binding.tvConditionTemp.text = "${weather.tempCurrent}°C"
        binding.tvConditionDesc.text = weather.condition
        binding.tvTempMin.text = "↑ ${weather.tempMin}°C"
        binding.tvTempMax.text = "↓ ${weather.tempMax}°C"

        binding.tvRainUv.text = "Khả năng có mưa: ${weather.rainProb}% - UV: ${weather.uvIndex}"
        binding.tvAirQuality.text = "Chất lượng không khí: ${weather.aqi}"

        // 6 giờ tiếp theo
        binding.rvHourlyWeather.layoutManager = LinearLayoutManager(this)
        binding.rvHourlyWeather.adapter = HourlyWeatherAdapter(weather.hourlyForecast)

        // 7 ngày tới
        binding.rvDailyWeather.layoutManager = LinearLayoutManager(this)
        binding.rvDailyWeather.adapter = DailyWeatherAdapter(weather.dailyForecast)
    }

    private fun showCitySelectorDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_city_selector)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        val etSearch: EditText = dialog.findViewById(R.id.et_search_city)
        val rvCity: RecyclerView = dialog.findViewById(R.id.rv_city_list)

        val adapter = CityAdapter(WeatherRepository.cities) { selectedCity ->
            loadWeatherData(selectedCity)
            dialog.dismiss()
        }

        rvCity.layoutManager = LinearLayoutManager(this)
        rvCity.adapter = adapter

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.filter(s?.toString() ?: "")
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        dialog.show()
    }
}
