package com.lichviet.vannien.ui.convert

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.NumberPicker
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.lichviet.vannien.R
import com.lichviet.vannien.calendar.LunarCalendarEngine
import com.lichviet.vannien.databinding.FragmentDateConverterBinding
import com.lichviet.vannien.ui.detail.DayDetailActivity
import java.util.Calendar

class DateConverterFragment : Fragment() {

    private var _binding: FragmentDateConverterBinding? = null
    private val binding get() = _binding!!

    private var isUpdatingPickers = false
    private lateinit var goodDayAdapter: GoodDayAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDateConverterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTabs()
        setupPickers()
        setupButtons()
        setupGoodDayFinder()

        // Khởi tạo ngày hiện tại
        val cal = Calendar.getInstance()
        setSolarPickers(cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR))
    }

    private fun setupTabs() {
        binding.tabDoiNgay.setOnClickListener {
            binding.tabDoiNgay.setBackgroundResource(R.drawable.bg_tab_selected)
            binding.tabTimNgayTot.setBackgroundResource(R.drawable.bg_tab_unselected)
            binding.containerDoiNgay.visibility = View.VISIBLE
            binding.containerTimNgayTot.visibility = View.GONE
        }

        binding.tabTimNgayTot.setOnClickListener {
            binding.tabTimNgayTot.setBackgroundResource(R.drawable.bg_tab_selected)
            binding.tabDoiNgay.setBackgroundResource(R.drawable.bg_tab_unselected)
            binding.containerDoiNgay.visibility = View.GONE
            binding.containerTimNgayTot.visibility = View.VISIBLE
            loadGoodDays()
        }
    }

    private fun setupPickers() {
        // Cấu hình phạm vi Picker Dương Lịch
        binding.pickerSolarDay.minValue = 1
        binding.pickerSolarDay.maxValue = 31
        binding.pickerSolarMonth.minValue = 1
        binding.pickerSolarMonth.maxValue = 12
        binding.pickerSolarYear.minValue = 1900
        binding.pickerSolarYear.maxValue = 2100

        // Cấu hình phạm vi Picker Âm Lịch
        binding.pickerLunarDay.minValue = 1
        binding.pickerLunarDay.maxValue = 30
        binding.pickerLunarMonth.minValue = 1
        binding.pickerLunarMonth.maxValue = 12
        binding.pickerLunarYear.minValue = 1900
        binding.pickerLunarYear.maxValue = 2100

        // Lắng nghe thay đổi Picker Dương Lịch -> Cập nhật Picker Âm Lịch
        val solarListener = NumberPicker.OnValueChangeListener { _, _, _ ->
            if (!isUpdatingPickers) {
                adjustSolarDaysRange()
                val sDay = binding.pickerSolarDay.value
                val sMonth = binding.pickerSolarMonth.value
                val sYear = binding.pickerSolarYear.value
                updateLunarFromSolar(sDay, sMonth, sYear)
            }
        }
        binding.pickerSolarDay.setOnValueChangedListener(solarListener)
        binding.pickerSolarMonth.setOnValueChangedListener(solarListener)
        binding.pickerSolarYear.setOnValueChangedListener(solarListener)

        // Lắng nghe thay đổi Picker Âm Lịch -> Cập nhật Picker Dương Lịch
        val lunarListener = NumberPicker.OnValueChangeListener { _, _, _ ->
            if (!isUpdatingPickers) {
                val lDay = binding.pickerLunarDay.value
                val lMonth = binding.pickerLunarMonth.value
                val lYear = binding.pickerLunarYear.value
                updateSolarFromLunar(lDay, lMonth, lYear)
            }
        }
        binding.pickerLunarDay.setOnValueChangedListener(lunarListener)
        binding.pickerLunarMonth.setOnValueChangedListener(lunarListener)
        binding.pickerLunarYear.setOnValueChangedListener(lunarListener)
    }

    private fun adjustSolarDaysRange() {
        val month = binding.pickerSolarMonth.value
        val year = binding.pickerSolarYear.value
        val cal = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month - 1)
            set(Calendar.DAY_OF_MONTH, 1)
        }
        val maxDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        if (binding.pickerSolarDay.maxValue != maxDays) {
            binding.pickerSolarDay.maxValue = maxDays
            if (binding.pickerSolarDay.value > maxDays) {
                binding.pickerSolarDay.value = maxDays
            }
        }
    }

    private fun setSolarPickers(day: Int, month: Int, year: Int) {
        isUpdatingPickers = true
        binding.pickerSolarMonth.value = month
        binding.pickerSolarYear.value = year
        adjustSolarDaysRange()
        binding.pickerSolarDay.value = day.coerceAtMost(binding.pickerSolarDay.maxValue)
        isUpdatingPickers = false

        updateLunarFromSolar(binding.pickerSolarDay.value, month, year)
    }

    private fun updateLunarFromSolar(sDay: Int, sMonth: Int, sYear: Int) {
        isUpdatingPickers = true
        val (lDay, lMonth, lYear) = LunarCalendarEngine.convertSolar2Lunar(sDay, sMonth, sYear)
        binding.pickerLunarDay.value = lDay
        binding.pickerLunarMonth.value = lMonth
        binding.pickerLunarYear.value = lYear
        isUpdatingPickers = false
    }

    private fun updateSolarFromLunar(lDay: Int, lMonth: Int, lYear: Int) {
        isUpdatingPickers = true
        val (sDay, sMonth, sYear) = LunarCalendarEngine.convertLunar2Solar(lDay, lMonth, lYear)
        binding.pickerSolarMonth.value = sMonth
        binding.pickerSolarYear.value = sYear
        adjustSolarDaysRange()
        binding.pickerSolarDay.value = sDay.coerceAtMost(binding.pickerSolarDay.maxValue)
        isUpdatingPickers = false
    }

    private fun setupButtons() {
        binding.btnViewDayDetail.setOnClickListener {
            val intent = Intent(requireContext(), DayDetailActivity::class.java).apply {
                putExtra("SOLAR_DAY", binding.pickerSolarDay.value)
                putExtra("SOLAR_MONTH", binding.pickerSolarMonth.value)
                putExtra("SOLAR_YEAR", binding.pickerSolarYear.value)
            }
            startActivity(intent)
        }
    }

    private fun setupGoodDayFinder() {
        val purposes = listOf(
            "Cưới hỏi, đính hôn",
            "Khai trương, mở cửa hàng",
            "Động thổ, khởi công xây dựng",
            "Xuất hành, đi xa cầu may",
            "Ký kết hợp đồng, giao dịch lớn",
            "Vào nhà mới, nhập trạch"
        )
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, purposes)
        binding.spinnerPurpose.adapter = adapter
        binding.spinnerPurpose.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                loadGoodDays()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        goodDayAdapter = GoodDayAdapter { lunarDate ->
            val intent = Intent(requireContext(), DayDetailActivity::class.java).apply {
                putExtra("SOLAR_DAY", lunarDate.solarDay)
                putExtra("SOLAR_MONTH", lunarDate.solarMonth)
                putExtra("SOLAR_YEAR", lunarDate.solarYear)
            }
            startActivity(intent)
        }
        binding.rvGoodDays.layoutManager = LinearLayoutManager(requireContext())
        binding.rvGoodDays.adapter = goodDayAdapter
    }

    private fun loadGoodDays() {
        val month = binding.pickerSolarMonth.value
        val year = binding.pickerSolarYear.value
        val purposePos = binding.spinnerPurpose.selectedItemPosition

        val goodDays = mutableListOf<LunarCalendarEngine.LunarDate>()
        val cal = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month - 1)
            set(Calendar.DAY_OF_MONTH, 1)
        }
        val daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)

        for (d in 1..daysInMonth) {
            val date = LunarCalendarEngine.getFullLunarDate(d, month, year)
            if (date.isHoangDao) {
                val matchesPurpose = when (purposePos) {
                    0 -> { // Cưới hỏi: Trực Định, Thành, Khai; tránh Nguyệt Kỵ, Tam Nương
                        date.truc in listOf("Định", "Thành", "Khai", "Mãn") &&
                                date.badStars.none { it.startsWith("Nguyệt Kỵ") || it.startsWith("Tam Nương") }
                    }
                    1 -> { // Khai trương: Trực Kiến, Khai, Thành, Mãn; tránh Nguyệt Kỵ
                        date.truc in listOf("Kiến", "Khai", "Thành", "Mãn") &&
                                date.badStars.none { it.startsWith("Nguyệt Kỵ") }
                    }
                    2 -> { // Động thổ: Trực Kiến, Khai, Định; tránh Tam Nương
                        date.truc in listOf("Kiến", "Khai", "Định") &&
                                date.badStars.none { it.startsWith("Tam Nương") }
                    }
                    3 -> { // Xuất hành: Trực Khai, Thành, Kiến, Trừ; tránh Nguyệt Kỵ
                        date.truc in listOf("Khai", "Thành", "Kiến", "Trừ") &&
                                date.badStars.none { it.startsWith("Nguyệt Kỵ") }
                    }
                    4 -> { // Ký kết hợp đồng: Trực Thành, Định, Khai
                        date.truc in listOf("Thành", "Định", "Khai")
                    }
                    5 -> { // Vào nhà mới: Trực Thành, Định, Khai, Mãn; tránh Tam Nương
                        date.truc in listOf("Thành", "Định", "Khai", "Mãn") &&
                                date.badStars.none { it.startsWith("Tam Nương") }
                    }
                    else -> true
                }
                if (matchesPurpose) {
                    goodDays.add(date)
                }
            }
        }
        goodDayAdapter.submitList(goodDays)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
