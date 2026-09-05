package com.lichviet.vannien.ui.convert

import android.content.Intent
import android.graphics.Color
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
import java.util.Locale

class DateConverterFragment : Fragment() {

    private var _binding: FragmentDateConverterBinding? = null
    private val binding get() = _binding!!

    private var isUpdatingPickers = false
    private lateinit var goodDayAdapter: GoodDayAdapter
    private val goodDayCalendar = Calendar.getInstance()

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

            // Đồng bộ tháng tìm ngày tốt theo ngày đang chọn
            goodDayCalendar.set(Calendar.YEAR, binding.pickerSolarYear.value)
            goodDayCalendar.set(Calendar.MONTH, binding.pickerSolarMonth.value - 1)
            updateGoodDayNavigator()
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
                val isLeap = binding.cbLunarLeap.isChecked
                updateSolarFromLunar(lDay, lMonth, lYear, isLeap)
            }
        }
        binding.pickerLunarDay.setOnValueChangedListener(lunarListener)
        binding.pickerLunarMonth.setOnValueChangedListener(lunarListener)
        binding.pickerLunarYear.setOnValueChangedListener(lunarListener)

        // Checkbox Tháng Nhuận
        binding.cbLunarLeap.setOnCheckedChangeListener { _, isChecked ->
            if (!isUpdatingPickers) {
                val lDay = binding.pickerLunarDay.value
                val lMonth = binding.pickerLunarMonth.value
                val lYear = binding.pickerLunarYear.value
                updateSolarFromLunar(lDay, lMonth, lYear, isChecked)
            }
        }
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

    private fun adjustLunarDaysRange(lMonth: Int, lYear: Int, isLeap: Boolean) {
        val maxDays = LunarCalendarEngine.getDaysInLunarMonth(lMonth, lYear, isLeap)
        binding.pickerLunarDay.maxValue = maxDays
        if (binding.pickerLunarDay.value > maxDays) {
            binding.pickerLunarDay.value = maxDays
        }
    }

    private fun updateLeapMonthUI(lMonth: Int, lYear: Int, isLeapResult: Boolean) {
        val leapMonth = LunarCalendarEngine.getLeapMonthOfYear(lYear)
        if (leapMonth > 0) {
            binding.tvLeapInfo.text = "(Năm này nhuận Tháng $leapMonth)"
            if (lMonth == leapMonth) {
                binding.cbLunarLeap.isEnabled = true
                binding.cbLunarLeap.isChecked = isLeapResult
            } else {
                binding.cbLunarLeap.isChecked = false
                binding.cbLunarLeap.isEnabled = false
            }
        } else {
            binding.tvLeapInfo.text = "(Năm nay không có tháng nhuận)"
            binding.cbLunarLeap.isChecked = false
            binding.cbLunarLeap.isEnabled = false
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
        val lunarRes = LunarCalendarEngine.convertSolar2Lunar(sDay, sMonth, sYear)
        binding.pickerLunarMonth.value = lunarRes.month
        binding.pickerLunarYear.value = lunarRes.year
        adjustLunarDaysRange(lunarRes.month, lunarRes.year, lunarRes.isLeap)
        binding.pickerLunarDay.value = lunarRes.day.coerceAtMost(binding.pickerLunarDay.maxValue)
        updateLeapMonthUI(lunarRes.month, lunarRes.year, lunarRes.isLeap)
        isUpdatingPickers = false

        updateResultCard(sDay, sMonth, sYear)
    }

    private fun updateSolarFromLunar(lDay: Int, lMonth: Int, lYear: Int, isLeap: Boolean) {
        isUpdatingPickers = true
        var solarRes = LunarCalendarEngine.convertLunar2Solar(lDay, lMonth, lYear, isLeap)
        if (solarRes.first == 0) {
            // Trường hợp tháng nhuận không hợp lệ
            solarRes = LunarCalendarEngine.convertLunar2Solar(lDay, lMonth, lYear, false)
        }

        if (solarRes.first > 0 && solarRes.second > 0 && solarRes.third > 0) {
            binding.pickerSolarMonth.value = solarRes.second
            binding.pickerSolarYear.value = solarRes.third
            adjustSolarDaysRange()
            binding.pickerSolarDay.value = solarRes.first.coerceIn(binding.pickerSolarDay.minValue, binding.pickerSolarDay.maxValue)
            adjustLunarDaysRange(lMonth, lYear, isLeap)
            updateLeapMonthUI(lMonth, lYear, isLeap)
            updateResultCard(binding.pickerSolarDay.value, solarRes.second, solarRes.third)
        }
        isUpdatingPickers = false
    }

    private fun updateResultCard(sDay: Int, sMonth: Int, sYear: Int) {
        val dateInfo = LunarCalendarEngine.getFullLunarDate(sDay, sMonth, sYear)

        binding.tvResultSolar.text = "${dateInfo.dayOfWeek}, ${String.format(Locale.getDefault(), "%02d/%02d/%d", sDay, sMonth, sYear)} Dương Lịch"

        val leapText = if (dateInfo.isLeap) " (Nhuận)" else ""
        binding.tvResultLunar.text = "Tức Ngày ${dateInfo.day} Tháng ${dateInfo.month}$leapText (Năm ${dateInfo.canChiYear} Âm Lịch)"

        if (dateInfo.isHoangDao) {
            binding.tvResultBadge.text = "HOÀNG ĐẠO"
            binding.tvResultBadge.setTextColor(Color.parseColor("#1B5E20"))
        } else {
            binding.tvResultBadge.text = "HẮC ĐẠO"
            binding.tvResultBadge.setTextColor(Color.parseColor("#C62828"))
        }

        binding.tvResultCanchi.text = "Can Chi: Ngày ${dateInfo.canChiDay} • Tháng ${dateInfo.canChiMonth} • Năm ${dateInfo.canChiYear}"
        binding.tvResultTietKhi.text = "Tiết khí: ${dateInfo.tietKhi}"

        val hoursStr = if (dateInfo.hoangDaoHours.isNotEmpty()) {
            dateInfo.hoangDaoHours.take(6).joinToString(", ") { it.name.split(" ").firstOrNull() ?: it.name }
        } else {
            "Tý, Sửu, Mão, Ngọ, Thân, Dậu"
        }
        binding.tvResultGoodHours.text = "Giờ Hoàng Đạo: $hoursStr"

        val adviceStr = when (dateInfo.truc) {
            "Kiến" -> "Trực Kiến (Tốt) • Khởi sự công việc, xuất hành; tránh động thổ cất nóc"
            "Trừ" -> "Trực Trừ (Đại cát) • Tẩy uế, dọn dẹp, trị bệnh, giải oan, an táng"
            "Mãn" -> "Trực Mãn (Cát tinh) • Mở kho, khai trương, xuất kho, nạp tài"
            "Bình" -> "Trực Bình (Bình hòa) • San nền, sửa chữa, hòa giải, việc gia đạo"
            "Định" -> "Trực Định (Đại cát) • Cưới hỏi, ký hợp đồng, vào nhà mới, an cư"
            "Chấp" -> "Trực Chấp (Cát hung) • Trồng trọt, chăn nuôi, giữ tiền của; tránh cho vay"
            "Phá" -> "Trực Phá (Cẩn trọng) • Phá dỡ công trình cũ; tránh cưới hỏi, khai trương"
            "Nguy" -> "Trực Nguy (Cẩn trọng) • Lễ Phật, cầu an, tích phước; tránh đi sông nước"
            "Thành" -> "Trực Thành (Đại cát) • Trăm sự thành tựu, thi cử, khai trương, cưới gả"
            "Thâu" -> "Trực Thâu (Cát lợi) • Thu hoạch, thu nợ, tích tài, cất giữ của cải"
            "Khai" -> "Trực Khai (Đại cát) • Mở mang sự nghiệp, khai trương, động thổ, cưới hỏi"
            else -> "Trực Bế (Bình an) • Đắp đập, tu bổ, tránh mở cửa hàng hoặc đi xa"
        }
        binding.tvResultAdvice.text = adviceStr
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

        binding.btnPrevMonthGoodDay.setOnClickListener {
            goodDayCalendar.add(Calendar.MONTH, -1)
            updateGoodDayNavigator()
            loadGoodDays()
        }

        binding.btnNextMonthGoodDay.setOnClickListener {
            goodDayCalendar.add(Calendar.MONTH, 1)
            updateGoodDayNavigator()
            loadGoodDays()
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

    private fun updateGoodDayNavigator() {
        val m = goodDayCalendar.get(Calendar.MONTH) + 1
        val y = goodDayCalendar.get(Calendar.YEAR)
        binding.tvGoodDayMonthYear.text = String.format(Locale.getDefault(), "Tháng %02d - %d", m, y)
    }

    private fun loadGoodDays() {
        val month = goodDayCalendar.get(Calendar.MONTH) + 1
        val year = goodDayCalendar.get(Calendar.YEAR)
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

        if (goodDays.isEmpty()) {
            binding.tvEmptyGoodDays.visibility = View.VISIBLE
            binding.rvGoodDays.visibility = View.GONE
        } else {
            binding.tvEmptyGoodDays.visibility = View.GONE
            binding.rvGoodDays.visibility = View.VISIBLE
            goodDayAdapter.submitList(goodDays)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
