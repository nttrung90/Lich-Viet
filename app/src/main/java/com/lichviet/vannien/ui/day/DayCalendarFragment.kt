package com.lichviet.vannien.ui.day

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.lichviet.vannien.R
import com.lichviet.vannien.calendar.LunarCalendarEngine
import com.lichviet.vannien.data.FolkQuoteRepository
import com.lichviet.vannien.databinding.FragmentDayCalendarBinding
import com.lichviet.vannien.ui.detail.DayDetailActivity
import com.lichviet.vannien.ui.horoscope.HoroscopeDetailActivity
import com.lichviet.vannien.ui.weather.WeatherActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.abs

class DayCalendarFragment : Fragment() {

    private var _binding: FragmentDayCalendarBinding? = null
    private val binding get() = _binding!!

    private val currentCalendar = Calendar.getInstance()
    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDayCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
        setupSwipeGesture()
        updateCalendarDisplay()
    }

    private fun setupListeners() {
        // Nút thời tiết -> Mở màn hình Thời Tiết (Ảnh 5)
        binding.btnWeather.setOnClickListener {
            startActivity(Intent(requireContext(), WeatherActivity::class.java))
        }

        // Nút vương miện -> Mở Tử Vi Toàn Tập (Ảnh 3)
        binding.btnCrown.setOnClickListener {
            val intent = Intent(requireContext(), HoroscopeDetailActivity::class.java).apply {
                putExtra("FEATURE_ID", 1)
                putExtra("TITLE", "Tử Vi Năm")
            }
            startActivity(intent)
        }

        // Nút sự kiện hoặc bấm vào tờ lịch -> Mở Chi Tiết Ngày (Ảnh 4)
        val openDetailAction = View.OnClickListener {
            openDayDetail()
        }
        binding.btnEvent.setOnClickListener(openDetailAction)
        binding.panelLunarInfo.setOnClickListener(openDetailAction)
        binding.cardCalendarPage.setOnClickListener(openDetailAction)

        // Nút "Hôm Nay" -> Trở về ngày hiện tại
        binding.btnToday.setOnClickListener {
            currentCalendar.timeInMillis = System.currentTimeMillis()
            updateCalendarDisplay()
        }

        // Chọn tháng - năm
        binding.btnSelectMonthYear.setOnClickListener {
            showMonthYearPicker()
        }
    }

    private fun openDayDetail() {
        val intent = Intent(requireContext(), DayDetailActivity::class.java).apply {
            putExtra("SOLAR_DAY", currentCalendar.get(Calendar.DAY_OF_MONTH))
            putExtra("SOLAR_MONTH", currentCalendar.get(Calendar.MONTH) + 1)
            putExtra("SOLAR_YEAR", currentCalendar.get(Calendar.YEAR))
        }
        startActivity(intent)
    }

    private fun showMonthYearPicker() {
        val y = currentCalendar.get(Calendar.YEAR)
        val m = currentCalendar.get(Calendar.MONTH)
        val d = currentCalendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            currentCalendar.set(Calendar.YEAR, selectedYear)
            currentCalendar.set(Calendar.MONTH, selectedMonth)
            currentCalendar.set(Calendar.DAY_OF_MONTH, selectedDay)
            updateCalendarDisplay()
        }, y, m, d).show()
    }

    private fun setupSwipeGesture() {
        val gestureDetector = GestureDetector(requireContext(), object : GestureDetector.SimpleOnGestureListener() {
            private val SWIPE_THRESHOLD = 80
            private val SWIPE_VELOCITY_THRESHOLD = 80

            override fun onFling(
                e1: MotionEvent?,
                e2: MotionEvent,
                velocityX: Float,
                velocityY: Float
            ): Boolean {
                if (e1 == null) return false
                val diffX = e2.x - e1.x
                val diffY = e2.y - e1.y
                if (abs(diffX) > abs(diffY) &&
                    abs(diffX) > SWIPE_THRESHOLD &&
                    abs(velocityX) > SWIPE_VELOCITY_THRESHOLD
                ) {
                    if (diffX > 0) {
                        // Vuốt sang phải -> Ngày trước
                        currentCalendar.add(Calendar.DAY_OF_MONTH, -1)
                    } else {
                        // Vuốt sang trái -> Ngày tiếp theo
                        currentCalendar.add(Calendar.DAY_OF_MONTH, 1)
                    }
                    updateCalendarDisplay()
                    return true
                }
                return false
            }
        })

        binding.layoutSwipeContainer.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            true
        }
    }

    fun updateCalendarDisplay() {
        val day = currentCalendar.get(Calendar.DAY_OF_MONTH)
        val month = currentCalendar.get(Calendar.MONTH) + 1
        val year = currentCalendar.get(Calendar.YEAR)
        val hour = currentCalendar.get(Calendar.HOUR_OF_DAY)

        // Tính toán thông tin Âm Lịch thiên văn
        val lunarDate = LunarCalendarEngine.getFullLunarDate(day, month, year)

        // Top bar Tháng - Năm
        binding.tvMonthYear.text = String.format(Locale.getDefault(), "Tháng %02d - %d", month, year)

        // Số ngày dương lịch to
        binding.tvSolarDay.text = day.toString()

        // Thứ trong tuần
        binding.tvDayOfWeek.text = lunarDate.dayOfWeek.uppercase()

        // Câu thành ngữ / ca dao tục ngữ
        val quote = FolkQuoteRepository.getDailyQuote(currentCalendar.get(Calendar.DAY_OF_YEAR))
        binding.tvFolkQuote.text = quote.quote
        binding.tvQuoteAuthor.text = quote.author

        // Giờ hiện tại và Can Chi của giờ
        binding.tvClockTime.text = timeFormat.format(currentCalendar.time)
        val canChiHour = LunarCalendarEngine.getCanChiHour(hour, lunarDate.canChiDay.split(" ").firstOrNull() ?: "Giáp")
        binding.tvCanChiHour.text = canChiHour

        // Ngày và tháng âm lịch
        binding.tvLunarDay.text = lunarDate.day.toString()
        binding.tvLunarMonth.text = if (lunarDate.isLeap) "Tháng ${lunarDate.month} (Nhuận)" else "Tháng ${lunarDate.month}"

        // Can Chi ngày, tháng, năm
        binding.tvCanchiDay.text = "Ng. ${lunarDate.canChiDay}"
        binding.tvCanchiMonth.text = "Th. ${lunarDate.canChiMonth}"
        binding.tvCanchiYear.text = "Năm ${lunarDate.canChiYear}"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
