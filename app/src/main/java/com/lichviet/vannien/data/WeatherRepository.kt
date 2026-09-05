package com.lichviet.vannien.data

import com.lichviet.vannien.R

/**
 * Kho dữ liệu và mô hình dự báo thời tiết theo tỉnh thành Việt Nam
 */
object WeatherRepository {

    data class WeatherForecast(
        val city: String,
        val tempCurrent: Int,
        val tempMin: Int,
        val tempMax: Int,
        val condition: String,
        val rainProb: Int,
        val uvIndex: String,
        val aqi: String,
        val hourlyForecast: List<HourlyItem>,
        val dailyForecast: List<DailyItem>
    )

    data class HourlyItem(
        val time: String,
        val condition: String,
        val temp: Int
    )

    data class DailyItem(
        val dayName: String,
        val condition: String,
        val tempMin: Int,
        val tempMax: Int
    )

    val cities = listOf(
        "Hà Nội", "TP. Hồ Chí Minh", "Đà Nẵng", "Hải Phòng", "Cần Thơ", "Nha Trang",
        "An Giang", "Bà Rịa - Vũng Tàu", "Bắc Giang", "Bắc Kạn", "Bạc Liêu",
        "Bắc Ninh", "Bến Tre", "Bình Định", "Bình Dương", "Bình Phước",
        "Bình Thuận", "Cà Mau", "Cao Bằng", "Đắk Lắk", "Đắk Nông",
        "Điện Biên", "Đồng Nai", "Đồng Tháp", "Gia Lai", "Hà Giang",
        "Hà Nam", "Hà Tĩnh", "Hải Dương", "Hậu Giang", "Hòa Bình",
        "Hưng Yên", "Khánh Hòa", "Kiên Giang", "Kon Tum", "Lai Châu",
        "Lâm Đồng", "Lạng Sơn", "Lào Cai", "Long An", "Nam Định",
        "Nghệ An", "Ninh Bình", "Ninh Thuận", "Phú Thọ", "Phú Yên",
        "Quảng Bình", "Quảng Nam", "Quảng Ngãi", "Quảng Ninh", "Quảng Trị",
        "Sóc Trăng", "Sơn La", "Tây Ninh", "Thái Bình", "Thái Nguyên",
        "Thanh Hóa", "Thừa Thiên Huế", "Tiền Giang", "Trà Vinh", "Tuyên Quang",
        "Vĩnh Long", "Vĩnh Phúc", "Yên Bái"
    )

    @Volatile
    var currentSelectedCity: String = "Hà Nội"

    fun getCurrentWeather(): WeatherForecast = getWeatherForCity(currentSelectedCity)

    fun getWeatherForCity(cityName: String): WeatherForecast {
        val baseTemp = when (cityName) {
            "TP. Hồ Chí Minh", "Cần Thơ", "Cà Mau" -> 32
            "Đà Nẵng", "Nha Trang" -> 31
            "Lâm Đồng", "Lào Cai", "Sơn La" -> 22
            else -> 29 // Hà Nội (ảnh 5: 29°)
        }

        val hourly = listOf(
            HourlyItem("15:00", "Nhiều mây", baseTemp),
            HourlyItem("16:00", "Nhiều mây", baseTemp),
            HourlyItem("17:00", "Rất nhiều mây", baseTemp),
            HourlyItem("18:00", "Mây rải rác", baseTemp - 1),
            HourlyItem("19:00", "Trời quang", baseTemp - 2),
            HourlyItem("20:00", "Mát mẻ", baseTemp - 3)
        )

        val daily = listOf(
            DailyItem("Hôm nay", "Mây rải rác", baseTemp - 4, baseTemp + 1),
            DailyItem("Ngày mai", "Nắng nhẹ", baseTemp - 3, baseTemp + 2),
            DailyItem("Thứ Tư", "Có mưa rào", baseTemp - 4, baseTemp),
            DailyItem("Thứ Năm", "Nhiều mây", baseTemp - 3, baseTemp + 1),
            DailyItem("Thứ Sáu", "Trời nắng", baseTemp - 2, baseTemp + 3),
            DailyItem("Thứ Bảy", "Mây rải rác", baseTemp - 3, baseTemp + 2),
            DailyItem("Chủ Nhật", "Nắng đẹp", baseTemp - 2, baseTemp + 3)
        )

        return WeatherForecast(
            city = cityName,
            tempCurrent = baseTemp,
            tempMin = baseTemp - 4,
            tempMax = baseTemp + 1,
            condition = "Mây rải rác",
            rainProb = 37,
            uvIndex = "Cao",
            aqi = "Bình thường",
            hourlyForecast = hourly,
            dailyForecast = daily
        )
    }

    fun getWeatherIcon(condition: String): Int {
        val cond = condition.lowercase()
        return when {
            cond.contains("dông") || cond.contains("sấm") || cond.contains("sét") || cond.contains("bão") -> R.drawable.ic_weather_thunder
            cond.contains("mưa") -> R.drawable.ic_weather_rainy
            cond.contains("rất nhiều mây") || cond.contains("u ám") -> R.drawable.ic_weather_cloudy
            cond.contains("nắng") || cond.contains("quang") -> R.drawable.ic_weather_sunny
            else -> R.drawable.ic_weather_sun_cloud
        }
    }
}
