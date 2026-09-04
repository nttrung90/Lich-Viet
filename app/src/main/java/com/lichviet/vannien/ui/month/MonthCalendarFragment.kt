package com.lichviet.vannien.ui.month

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.lichviet.vannien.calendar.LunarCalendarEngine
import com.lichviet.vannien.databinding.FragmentMonthCalendarBinding
import com.lichviet.vannien.ui.detail.DayDetailActivity
import java.util.Calendar
import java.util.Locale

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
        loadMonthData()
    }

    private fun setupRecyclerView() {
        adapter = MonthCalendarAdapter { dayModel ->
            selectedDayModel = dayModel
            updateSummaryCard(dayModel)
        }
        binding.rvCalendarGrid.layoutManager = GridLayoutManager(requireContext(), 7)
        binding.rvCalendarGrid.adapter = adapter
    }

    private fun setupListeners() {
        binding.btnPrevMonth.setOnClickListener {
            displayCalendar.add(Calendar.MONTH, -1)
            loadMonthData()
        }

        binding.btnNextMonth.setOnClickListener {
            displayCalendar.add(Calendar.MONTH, 1)
            loadMonthData()
        }

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

    private fun loadMonthData() {
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

        // Padding previous month
        val prevCal = (cal.clone() as Calendar).apply { add(Calendar.MONTH, -1) }
        val daysInPrevMonth = prevCal.getActualMaximum(Calendar.DAY_OF_MONTH)
        for (i in startOffset - 1 downTo 0) {
            val d = daysInPrevMonth - i
            val (ld, lm) = LunarCalendarEngine.convertSolar2Lunar(d, prevCal.get(Calendar.MONTH) + 1, prevCal.get(Calendar.YEAR))
            dayList.add(
                CalendarDayModel(
                    solarDay = d,
                    solarMonth = prevCal.get(Calendar.MONTH) + 1,
                    solarYear = prevCal.get(Calendar.YEAR),
                    lunarDay = ld,
                    lunarMonth = lm,
                    isCurrentMonth = false,
                    isToday = false,
                    isHoangDao = false,
                    isSpecial = false
                )
            )
        }

        // Current month days
        val todayCal = Calendar.getInstance()
        val isThisMonthAndYear = (todayCal.get(Calendar.YEAR) == year && todayCal.get(Calendar.MONTH) + 1 == month)
        val todayDate = todayCal.get(Calendar.DAY_OF_MONTH)

        for (d in 1..daysInMonth) {
            val (ld, lm) = LunarCalendarEngine.convertSolar2Lunar(d, month, year)
            val fullDate = LunarCalendarEngine.getFullLunarDate(d, month, year)
            val isToday = isThisMonthAndYear && (d == todayDate)
            val isSpecial = (ld == 1 || ld == 15)

            val model = CalendarDayModel(
                solarDay = d,
                solarMonth = month,
                solarYear = year,
                lunarDay = ld,
                lunarMonth = lm,
                isCurrentMonth = true,
                isToday = isToday,
                isHoangDao = fullDate.isHoangDao,
                isSpecial = isSpecial
            )
            dayList.add(model)
            if (isToday || (selectedDayModel == null && d == 1)) {
                selectedDayModel = model
            }
        }

        // Padding next month to fill complete 5-6 rows
        val totalCells = if (dayList.size <= 35) 35 else 42
        val nextMonthPadding = totalCells - dayList.size
        val nextCal = (cal.clone() as Calendar).apply { add(Calendar.MONTH, 1) }
        for (d in 1..nextMonthPadding) {
            val (ld, lm) = LunarCalendarEngine.convertSolar2Lunar(d, nextCal.get(Calendar.MONTH) + 1, nextCal.get(Calendar.YEAR))
            dayList.add(
                CalendarDayModel(
                    solarDay = d,
                    solarMonth = nextCal.get(Calendar.MONTH) + 1,
                    solarYear = nextCal.get(Calendar.YEAR),
                    lunarDay = ld,
                    lunarMonth = lm,
                    isCurrentMonth = false,
                    isToday = false,
                    isHoangDao = false,
                    isSpecial = false
                )
            )
        }

        adapter.submitList(dayList, selectedDayModel)
        selectedDayModel?.let { updateSummaryCard(it) }
    }

    private fun updateSummaryCard(model: CalendarDayModel) {
        val full = LunarCalendarEngine.getFullLunarDate(model.solarDay, model.solarMonth, model.solarYear)
        binding.tvSummarySolarDate.text = String.format(
            Locale.getDefault(),
            "%s, %02d-%02d-%d",
            full.dayOfWeek,
            model.solarDay,
            model.solarMonth,
            model.solarYear
        )
        binding.tvSummaryLunarDate.text = String.format(
            Locale.getDefault(),
            "Âm lịch: Ngày %d Tháng %d Năm %s",
            model.lunarDay,
            model.lunarMonth,
            full.canChiYear
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
