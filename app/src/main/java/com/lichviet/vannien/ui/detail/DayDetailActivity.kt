package com.lichviet.vannien.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.lichviet.vannien.calendar.LunarCalendarEngine
import com.lichviet.vannien.data.HolidayRepository
import com.lichviet.vannien.databinding.ActivityDayDetailBinding
import java.util.Calendar
import java.util.Locale

class DayDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDayDetailBinding
    private val currentCalendar = Calendar.getInstance()
    private val hoangDaoAdapter = HoangDaoHourAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDayDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvHoangDaoHours.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvHoangDaoHours.adapter = hoangDaoAdapter

        val sDay = intent.getIntExtra("SOLAR_DAY", currentCalendar.get(Calendar.DAY_OF_MONTH))
        val sMonth = intent.getIntExtra("SOLAR_MONTH", currentCalendar.get(Calendar.MONTH) + 1)
        val sYear = intent.getIntExtra("SOLAR_YEAR", currentCalendar.get(Calendar.YEAR))

        currentCalendar.set(Calendar.YEAR, sYear)
        currentCalendar.set(Calendar.MONTH, sMonth - 1)
        currentCalendar.set(Calendar.DAY_OF_MONTH, sDay)

        setupListeners()
        updateDisplay()
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnPrevDay.setOnClickListener {
            currentCalendar.add(Calendar.DAY_OF_MONTH, -1)
            updateDisplay()
        }

        binding.btnNextDay.setOnClickListener {
            currentCalendar.add(Calendar.DAY_OF_MONTH, 1)
            updateDisplay()
        }
    }

    private fun updateDisplay() {
        val day = currentCalendar.get(Calendar.DAY_OF_MONTH)
        val month = currentCalendar.get(Calendar.MONTH) + 1
        val year = currentCalendar.get(Calendar.YEAR)

        val lunarDate = LunarCalendarEngine.getFullLunarDate(day, month, year)

        // Top bar title: Tháng MM - YYYY
        binding.tvDetailMonthYear.text = String.format(Locale.getDefault(), "Tháng %02d - %d", month, year)

        // Date Navigator text: Thứ ..., DD-MM-YYYY
        binding.tvDetailFullDate.text = String.format(
            Locale.getDefault(),
            "%s, %02d-%02d-%d",
            lunarDate.dayOfWeek,
            day,
            month,
            year
        )

        // 3 Cột Lịch Âm
        binding.tvDetailLunarDay.text = lunarDate.day.toString()
        binding.tvDetailCanchiDay.text = lunarDate.canChiDay

        binding.tvDetailLunarMonth.text = if (lunarDate.isLeap) "${lunarDate.month} (Nhuận)" else lunarDate.month.toString()
        binding.tvDetailCanchiMonth.text = lunarDate.canChiMonth

        binding.tvDetailLunarYear.text = lunarDate.year.toString()
        binding.tvDetailCanchiYear.text = lunarDate.canChiYear

        // Sự kiện
        val holiday = HolidayRepository.getHoliday(day, month, lunarDate.day, lunarDate.month, year)
        binding.tvDetailEvents.text = holiday ?: "Không có sự kiện đặc biệt trong ngày này."

        // Giờ Hoàng Đạo
        hoangDaoAdapter.updateData(lunarDate.hoangDaoHours)

        // Hướng xuất hành
        binding.tvHyThan.text = "- Hỷ Thần: ${lunarDate.hyThan}"
        binding.tvTaiThan.text = "- Tài Thần: ${lunarDate.taiThan}"
        binding.tvHacThan.text = "- Hạc Thần (nên tránh): ${lunarDate.hacThan}"

        // Tiết khí & Trực
        binding.tvTietKhi.text = "Tiết khí: ${lunarDate.tietKhi}"
        val hoangDaoStatus = if (lunarDate.isHoangDao) "Ngày Hoàng Đạo (Cát)" else "Ngày Hắc Đạo (Hung)"
        binding.tvTruc.text = "Trực: ${lunarDate.truc} (${hoangDaoStatus}) - Sao: ${lunarDate.saoNhiThapBatTu}"

        // Sao tốt - Sao xấu
        binding.tvGoodStars.text = "Sao Tốt: " + lunarDate.goodStars.joinToString(", ")
        binding.tvBadStars.text = "Sao Xấu: " + lunarDate.badStars.joinToString(", ")

        // Việc nên làm - Kiêng cữ
        binding.tvShouldDo.text = "Nên làm: " + lunarDate.shouldDo.joinToString(", ")
        binding.tvShouldAvoid.text = "Kiêng cữ: " + lunarDate.shouldAvoid.joinToString(", ")
    }
}
