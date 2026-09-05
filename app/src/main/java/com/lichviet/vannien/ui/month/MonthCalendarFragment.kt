package com.lichviet.vannien.ui.month

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
                // Chuyển sang tháng của ngày vừa chọn với hiệu ứng trượt mượt mà
                val currentYear = displayCalendar.get(Calendar.YEAR)
                val currentMonth = displayCalendar.get(Calendar.MONTH) + 1
                val isNext = (dayModel.solarYear > currentYear) ||
                        (dayModel.solarYear == currentYear && dayModel.solarMonth > currentMonth)

                displayCalendar.set(Calendar.YEAR, dayModel.solarYear)
                displayCalendar.set(Calendar.MONTH, dayModel.solarMonth - 1)
                displayCalendar.set(Calendar.DAY_OF_MONTH, dayModel.solarDay)
                animateMonthChange(toNext = isNext, preferredDay = dayModel.solarDay)
            }
        }
        binding.rvCalendarGrid.layoutManager = GridLayoutManager(requireContext(), 7)
        if (binding.rvCalendarGrid.itemDecorationCount == 0) {
            binding.rvCalendarGrid.addItemDecoration(CalendarGridDividerDecoration())
        }
        binding.rvCalendarGrid.isNestedScrollingEnabled = false
        binding.rvCalendarGrid.adapter = adapter
    }

    private var isAnimating = false

    /**
     * Vuốt sang trái (hoặc bấm mũi tên phải) -> Đến tháng kế tiếp
     */
    private fun goToNextMonthWithAnim() {
        animateMonthChange(toNext = true)
    }

    /**
     * Vuốt sang phải (hoặc bấm mũi tên trái) -> Chuyển về tháng trước
     */
    private fun goToPreviousMonthWithAnim() {
        animateMonthChange(toNext = false)
    }

    private fun animateMonthChange(toNext: Boolean, preferredDay: Int? = null) {
        if (isAnimating) return
        isAnimating = true

        val width = binding.rvCalendarGrid.width.toFloat()
        val travelDistance = if (width > 0) width * 0.25f else 250f
        val exitTranslation = if (toNext) -travelDistance else travelDistance
        val enterTranslation = if (toNext) travelDistance else -travelDistance

        binding.rvCalendarGrid.animate()
            .translationX(exitTranslation)
            .alpha(0.15f)
            .setDuration(120)
            .withEndAction {
                if (preferredDay == null) {
                    displayCalendar.add(Calendar.MONTH, if (toNext) 1 else -1)
                }
                loadMonthData(preferredDay)
                binding.rvCalendarGrid.translationX = enterTranslation
                binding.rvCalendarGrid.animate()
                    .translationX(0f)
                    .alpha(1f)
                    .setDuration(150)
                    .withEndAction {
                        isAnimating = false
                    }
                    .start()
            }
            .start()
    }

    private fun setupListeners() {
        // Tháng trước -> chuyển về tháng trước
        binding.btnPrevMonth.setOnClickListener {
            goToPreviousMonthWithAnim()
        }

        // Tháng sau -> đến tháng kế tiếp
        binding.btnNextMonth.setOnClickListener {
            goToNextMonthWithAnim()
        }

        // Nút "Hôm Nay" -> quay về tháng hiện tại và chọn ngày hôm nay
        binding.btnMonthToday.setOnClickListener {
            val now = Calendar.getInstance()
            val isDiff = displayCalendar.get(Calendar.YEAR) != now.get(Calendar.YEAR) ||
                    displayCalendar.get(Calendar.MONTH) != now.get(Calendar.MONTH)

            displayCalendar.timeInMillis = System.currentTimeMillis()
            if (isDiff) {
                binding.rvCalendarGrid.animate()
                    .alpha(0.2f)
                    .setDuration(100)
                    .withEndAction {
                        loadMonthData(now.get(Calendar.DAY_OF_MONTH))
                        binding.rvCalendarGrid.animate().alpha(1f).setDuration(120).start()
                    }
                    .start()
            } else {
                loadMonthData(now.get(Calendar.DAY_OF_MONTH))
            }
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
        val touchSlop = ViewConfiguration.get(requireContext()).scaledTouchSlop
        val density = resources.displayMetrics.density
        var startX = 0f
        var startY = 0f
        var isHorizontalSwipe = false
        var hasHandledSwipe = false

        val gestureDetector = GestureDetector(requireContext(), object : GestureDetector.SimpleOnGestureListener() {
            private val SWIPE_THRESHOLD = 40 * density
            private val SWIPE_VELOCITY_THRESHOLD = 80 * density

            override fun onDown(e: MotionEvent): Boolean = true

            override fun onFling(
                e1: MotionEvent?,
                e2: MotionEvent,
                velocityX: Float,
                velocityY: Float
            ): Boolean {
                if (e1 == null) return false
                val diffX = e2.x - e1.x
                val diffY = e2.y - e1.y
                if (abs(diffX) > abs(diffY) && abs(diffX) > SWIPE_THRESHOLD && abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (!hasHandledSwipe) {
                        hasHandledSwipe = true
                        if (diffX > 0) {
                            // Vuốt sang phải -> Chuyển về tháng trước
                            goToPreviousMonthWithAnim()
                        } else {
                            // Vuốt sang trái -> Đến tháng kế tiếp
                            goToNextMonthWithAnim()
                        }
                        return true
                    }
                }
                return false
            }
        })

        // Lắng nghe thao tác chạm và vuốt trực tiếp trên RecyclerView
        binding.rvCalendarGrid.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                gestureDetector.onTouchEvent(e)
                when (e.actionMasked) {
                    MotionEvent.ACTION_DOWN -> {
                        startX = e.x
                        startY = e.y
                        isHorizontalSwipe = false
                        hasHandledSwipe = false
                    }
                    MotionEvent.ACTION_MOVE -> {
                        val dx = e.x - startX
                        val dy = e.y - startY
                        // Khi người dùng bắt đầu vuốt ngang, chặn itemView con để không kích hoạt click nhầm
                        if (abs(dx) > touchSlop && abs(dx) > abs(dy) * 1.2f) {
                            isHorizontalSwipe = true
                            return true
                        }
                    }
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        if (isHorizontalSwipe && !hasHandledSwipe) {
                            val dx = e.x - startX
                            if (abs(dx) > 50 * density) {
                                hasHandledSwipe = true
                                if (dx > 0) {
                                    // Vuốt sang phải -> Chuyển về tháng trước
                                    goToPreviousMonthWithAnim()
                                } else {
                                    // Vuốt sang trái -> Đến tháng kế tiếp
                                    goToNextMonthWithAnim()
                                }
                            }
                        }
                        isHorizontalSwipe = false
                    }
                }
                return isHorizontalSwipe
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
                gestureDetector.onTouchEvent(e)
                when (e.actionMasked) {
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        if (isHorizontalSwipe && !hasHandledSwipe) {
                            val dx = e.x - startX
                            if (abs(dx) > 50 * density) {
                                hasHandledSwipe = true
                                if (dx > 0) {
                                    // Vuốt sang phải -> Chuyển về tháng trước
                                    goToPreviousMonthWithAnim()
                                } else {
                                    // Vuốt sang trái -> Đến tháng kế tiếp
                                    goToNextMonthWithAnim()
                                }
                            }
                        }
                        isHorizontalSwipe = false
                    }
                }
            }

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
        })

        // Cho phép vuốt trên cả thanh thứ trong tuần (layoutWeekdays)
        val headerTouchListener = View.OnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    startX = event.x
                    startY = event.y
                    hasHandledSwipe = false
                    true
                }
                MotionEvent.ACTION_UP -> {
                    if (!hasHandledSwipe) {
                        val dx = event.x - startX
                        if (abs(dx) > 50 * density) {
                            hasHandledSwipe = true
                            if (dx > 0) goToPreviousMonthWithAnim() else goToNextMonthWithAnim()
                        }
                    }
                    true
                }
                else -> false
            }
        }
        binding.layoutWeekdays.setOnTouchListener(headerTouchListener)
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
            val lunarRes = LunarCalendarEngine.convertSolar2Lunar(d, mPrev, yPrev)
            prevCal.set(Calendar.DAY_OF_MONTH, d)
            val dow = prevCal.get(Calendar.DAY_OF_WEEK)
            val holiday = HolidayRepository.getHoliday(d, mPrev, lunarRes.day, lunarRes.month, yPrev)

            dayList.add(
                CalendarDayModel(
                    solarDay = d,
                    solarMonth = mPrev,
                    solarYear = yPrev,
                    lunarDay = lunarRes.day,
                    lunarMonth = lunarRes.month,
                    dayOfWeek = dow,
                    isCurrentMonth = false,
                    isToday = false,
                    isHoangDao = false,
                    isSpecial = false,
                    holidayName = holiday,
                    isLeapMonth = lunarRes.isLeap
                )
            )
        }

        // 2. Ngày trong tháng hiện tại
        val todayCal = Calendar.getInstance()
        val isThisMonthAndYear = (todayCal.get(Calendar.YEAR) == year && todayCal.get(Calendar.MONTH) + 1 == month)
        val todayDate = todayCal.get(Calendar.DAY_OF_MONTH)

        var matchedSelectedModel: CalendarDayModel? = null

        for (d in 1..daysInMonth) {
            val lunarRes = LunarCalendarEngine.convertSolar2Lunar(d, month, year)
            val fullDate = LunarCalendarEngine.getFullLunarDate(d, month, year)
            cal.set(Calendar.DAY_OF_MONTH, d)
            val dow = cal.get(Calendar.DAY_OF_WEEK)
            val isToday = isThisMonthAndYear && (d == todayDate)
            val holiday = HolidayRepository.getHoliday(d, month, lunarRes.day, lunarRes.month, year)
            val isSpecial = (lunarRes.day == 1 || lunarRes.day == 15 || !holiday.isNullOrEmpty())

            val model = CalendarDayModel(
                solarDay = d,
                solarMonth = month,
                solarYear = year,
                lunarDay = lunarRes.day,
                lunarMonth = lunarRes.month,
                dayOfWeek = dow,
                isCurrentMonth = true,
                isToday = isToday,
                isHoangDao = fullDate.isHoangDao,
                isSpecial = isSpecial,
                holidayName = holiday,
                isLeapMonth = lunarRes.isLeap
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
            val lunarRes = LunarCalendarEngine.convertSolar2Lunar(d, mNext, yNext)
            nextCal.set(Calendar.DAY_OF_MONTH, d)
            val dow = nextCal.get(Calendar.DAY_OF_WEEK)
            val holiday = HolidayRepository.getHoliday(d, mNext, lunarRes.day, lunarRes.month, yNext)

            dayList.add(
                CalendarDayModel(
                    solarDay = d,
                    solarMonth = mNext,
                    solarYear = yNext,
                    lunarDay = lunarRes.day,
                    lunarMonth = lunarRes.month,
                    dayOfWeek = dow,
                    isCurrentMonth = false,
                    isToday = false,
                    isHoangDao = false,
                    isSpecial = false,
                    holidayName = holiday,
                    isLeapMonth = lunarRes.isLeap
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
        val holiday = HolidayRepository.getHoliday(model.solarDay, model.solarMonth, full.day, full.month, model.solarYear)
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
