package com.lichviet.vannien.ui.month

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.lichviet.vannien.R
import com.lichviet.vannien.calendar.LunarCalendarEngine
import com.lichviet.vannien.data.HolidayRepository
import com.lichviet.vannien.databinding.FragmentMonthCalendarBinding
import com.lichviet.vannien.ui.detail.DayDetailActivity
import java.util.Calendar
import java.util.Locale
import kotlin.math.abs

class MonthCalendarFragment : Fragment() {

    private var _binding: FragmentMonthCalendarBinding? = null
    private val binding get() = _binding!!

    private val displayCalendar = Calendar.getInstance()
    private lateinit var adapter: MonthCalendarAdapter
    private var selectedDayModel: CalendarDayModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMonthCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupListeners()
        setupSwipeGesture()
        loadMonthData()
    }

    private fun setupRecyclerView() {
        adapter = MonthCalendarAdapter { dayModel ->
            if (dayModel.isCurrentMonth) {
                selectedDayModel = dayModel
                updateSummaryCard(dayModel)
            } else {
                // Chuyển sang tháng của ngày vừa chọn
                displayCalendar.set(Calendar.YEAR, dayModel.solarYear)
                displayCalendar.set(Calendar.MONTH, dayModel.solarMonth - 1)
                displayCalendar.set(Calendar.DAY_OF_MONTH, dayModel.solarDay)
                loadMonthData(dayModel.solarDay)
            }
        }
        binding.rvCalendarGrid.layoutManager = GridLayoutManager(requireContext(), 7)
        binding.rvCalendarGrid.adapter = adapter
    }

    private fun setupListeners() {
        // Tháng trước
        binding.btnPrevMonth.setOnClickListener {
            displayCalendar.add(Calendar.MONTH, -1)
            loadMonthData()
        }

        // Tháng sau
        binding.btnNextMonth.setOnClickListener {
            displayCalendar.add(Calendar.MONTH, 1)
            loadMonthData()
        }

        // Nút "Hôm Nay" -> quay về tháng hiện tại và chọn ngày hôm nay
        binding.btnMonthToday.setOnClickListener {
            displayCalendar.timeInMillis = System.currentTimeMillis()
            loadMonthData(Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
        }

        // Bấm vào tiêu đề "Tháng MM - YYYY" -> mở bộ chọn ngày tháng nhanh
        binding.layoutMonthTitlePicker.setOnClickListener {
            showMonthYearPicker()
        }

        // Nút "Xem chi tiết" mở màn hình chi tiết ngày
        binding.btnSummaryViewDetail.setOnClickListener {
            selectedDayModel?.let { model ->
                val intent = Intent(requireContext(), DayDetailActivity::class.java).apply {
                    putExtra("SOLAR_DAY", model.solarDay)
                    putExtra("SOLAR_MONTH", model.solarMonth)
                    putExtra("SOLAR_YEAR", model.solarYear)
                }
                startActivity(intent)
            }
        }
    }

    private fun showMonthYearPicker() {
        val y = displayCalendar.get(Calendar.YEAR)
        val m = displayCalendar.get(Calendar.MONTH)
        val d = displayCalendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            displayCalendar.set(Calendar.YEAR, selectedYear)
            displayCalendar.set(Calendar.MONTH, selectedMonth)
            displayCalendar.set(Calendar.DAY_OF_MONTH, selectedDay)
            loadMonthData(selectedDay)
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
                if (abs(diffX) > abs(diffY)) {
                    if (abs(diffX) > SWIPE_THRESHOLD && abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            // Vuốt sang phải -> Tháng trước
                            displayCalendar.add(Calendar.MONTH, -1)
                            loadMonthData()
                        } else {
                            // Vuốt sang trái -> Tháng sau
                            displayCalendar.add(Calendar.MONTH, 1)
                            loadMonthData()
                        }
                        return true
                    }
                }
                return false
            }
        })

        binding.rvCalendarGrid.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            false
        }
    }

    fun loadMonthData(preferredDay: Int? = null) {
        val month = displayCalendar.get(Calendar.MONTH) + 1
        val year = displayCalendar.get(Calendar.YEAR)

        binding.tvMonthCalendarTitle.text = String.format(Locale.getDefault(), "Tháng %02d - %d", month, year)

        val cal = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month - 1)
            set(Calendar.DAY_OF_MONTH, 1)
        }

        val daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        // Day of week: Sunday = 1, Monday = 2 ...
        val firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK)
        // Convert to Monday = 0, Sunday = 6
        val startOffset = if (firstDayOfWeek == Calendar.SUNDAY) 6 else firstDayOfWeek - 2

        val dayList = mutableListOf<CalendarDayModel>()

        // 1. Padding tháng trước
        val prevCal = (cal.clone() as Calendar).apply { add(Calendar.MONTH, -1) }
        val daysInPrevMonth = prevCal.getActualMaximum(Calendar.DAY_OF_MONTH)
        for (i in startOffset - 1 downTo 0) {
            val d = daysInPrevMonth - i
            val mPrev = prevCal.get(Calendar.MONTH) + 1
            val yPrev = prevCal.get(Calendar.YEAR)
            val (ld, lm) = LunarCalendarEngine.convertSolar2Lunar(d, mPrev, yPrev)
            prevCal.set(Calendar.DAY_OF_MONTH, d)
            val dow = prevCal.get(Calendar.DAY_OF_WEEK)
            val holiday = HolidayRepository.getHoliday(d, mPrev, ld, lm)

            dayList.add(
                CalendarDayModel(
                    solarDay = d,
                    solarMonth = mPrev,
                    solarYear = yPrev,
                    lunarDay = ld,
                    lunarMonth = lm,
                    dayOfWeek = dow,
                    isCurrentMonth = false,
                    isToday = false,
                    isHoangDao = false,
                    isSpecial = false,
                    holidayName = holiday
                )
            )
        }

        // 2. Ngày trong tháng hiện tại
        val todayCal = Calendar.getInstance()
        val isThisMonthAndYear = (todayCal.get(Calendar.YEAR) == year && todayCal.get(Calendar.MONTH) + 1 == month)
        val todayDate = todayCal.get(Calendar.DAY_OF_MONTH)

        var matchedSelectedModel: CalendarDayModel? = null

        for (d in 1..daysInMonth) {
            val (ld, lm) = LunarCalendarEngine.convertSolar2Lunar(d, month, year)
            val fullDate = LunarCalendarEngine.getFullLunarDate(d, month, year)
            cal.set(Calendar.DAY_OF_MONTH, d)
            val dow = cal.get(Calendar.DAY_OF_WEEK)
            val isToday = isThisMonthAndYear && (d == todayDate)
            val holiday = HolidayRepository.getHoliday(d, month, ld, lm)
            val isSpecial = (ld == 1 || ld == 15 || !holiday.isNullOrEmpty())

            val model = CalendarDayModel(
                solarDay = d,
                solarMonth = month,
                solarYear = year,
                lunarDay = ld,
                lunarMonth = lm,
                dayOfWeek = dow,
                isCurrentMonth = true,
                isToday = isToday,
                isHoangDao = fullDate.isHoangDao,
                isSpecial = isSpecial,
                holidayName = holiday
            )
            dayList.add(model)

            // Chọn ngày ưu tiên: theo preferredDay, hoặc ngày hôm nay, hoặc ngày 1
            if (preferredDay != null && d == preferredDay) {
                matchedSelectedModel = model
            } else if (matchedSelectedModel == null && isToday) {
                matchedSelectedModel = model
            } else if (matchedSelectedModel == null && d == 1) {
                matchedSelectedModel = model
            }
        }

        selectedDayModel = matchedSelectedModel ?: dayList.firstOrNull { it.isCurrentMonth }

        // 3. Padding tháng sau để đủ 5-6 hàng lưới
        val totalCells = if (dayList.size <= 35) 35 else 42
        val nextMonthPadding = totalCells - dayList.size
        val nextCal = (cal.clone() as Calendar).apply { add(Calendar.MONTH, 1) }
        for (d in 1..nextMonthPadding) {
            val mNext = nextCal.get(Calendar.MONTH) + 1
            val yNext = nextCal.get(Calendar.YEAR)
            val (ld, lm) = LunarCalendarEngine.convertSolar2Lunar(d, mNext, yNext)
            nextCal.set(Calendar.DAY_OF_MONTH, d)
            val dow = nextCal.get(Calendar.DAY_OF_WEEK)
            val holiday = HolidayRepository.getHoliday(d, mNext, ld, lm)

            dayList.add(
                CalendarDayModel(
                    solarDay = d,
                    solarMonth = mNext,
                    solarYear = yNext,
                    lunarDay = ld,
                    lunarMonth = lm,
                    dayOfWeek = dow,
                    isCurrentMonth = false,
                    isToday = false,
                    isHoangDao = false,
                    isSpecial = false,
                    holidayName = holiday
                )
            )
        }

        adapter.submitList(dayList, selectedDayModel)
        selectedDayModel?.let { updateSummaryCard(it) }
    }

    private fun updateSummaryCard(model: CalendarDayModel) {
        val full = LunarCalendarEngine.getFullLunarDate(model.solarDay, model.solarMonth, model.solarYear)

        // 1. Ngày dương lịch
        binding.tvSummarySolarDate.text = String.format(
            Locale.getDefault(),
            "%s, %02d/%02d/%d",
            full.dayOfWeek,
            model.solarDay,
            model.solarMonth,
            model.solarYear
        )

        // 2. Tag Hoàng Đạo / Hắc Đạo
        if (full.isHoangDao) {
            binding.tvSummaryHoangDaoTag.text = "Hoàng Đạo"
            binding.tvSummaryHoangDaoTag.setBackgroundResource(R.drawable.bg_badge_hoang_dao)
            binding.tvSummaryHoangDaoTag.setTextColor(Color.parseColor("#2E7D32"))
        } else {
            binding.tvSummaryHoangDaoTag.text = "Hắc Đạo"
            binding.tvSummaryHoangDaoTag.setBackgroundResource(R.drawable.bg_badge_hac_dao)
            binding.tvSummaryHoangDaoTag.setTextColor(Color.parseColor("#546E7A"))
        }

        // 3. Ngày âm lịch & Can Chi
        val monthDisplay = if (full.isLeap) "Tháng ${full.month} (Nhuận)" else "Tháng ${full.month}"
        binding.tvSummaryLunarDate.text = String.format(
            Locale.getDefault(),
            "Âm lịch: Ngày %d %s Năm %s • Ngày %s",
            full.day,
            monthDisplay,
            full.canChiYear,
            full.canChiDay
        )

        // 4. Ngày lễ (nếu có)
        val holiday = HolidayRepository.getHoliday(model.solarDay, model.solarMonth, full.day, full.month)
        if (!holiday.isNullOrEmpty()) {
            binding.tvSummaryHoliday.visibility = View.VISIBLE
            binding.tvSummaryHoliday.text = "🏮 $holiday"
        } else {
            binding.tvSummaryHoliday.visibility = View.GONE
        }

        // 5. Tiết khí & Trực
        binding.tvSummaryExtra.text = "Tiết khí: ${full.tietKhi} • Trực: ${full.truc}"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
