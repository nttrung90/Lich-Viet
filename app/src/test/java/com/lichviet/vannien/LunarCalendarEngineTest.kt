package com.lichviet.vannien

import com.lichviet.vannien.calendar.HoroscopeEngine
import com.lichviet.vannien.calendar.LunarCalendarEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class LunarCalendarEngineTest {

    @Test
    fun testLunarConversionBasic() {
        // Kiểm tra ngày Dương lịch -> Âm lịch
        val (lunarDay, lunarMonth, lunarYear) = LunarCalendarEngine.convertSolar2Lunar(20, 8, 2019)
        assertEquals(20, lunarDay)
        assertEquals(7, lunarMonth)
        assertEquals(2019, lunarYear)
    }

    @Test
    fun testTwoWayConversion() {
        // Kiểm tra chuyển đổi 2 chiều Dương <-> Âm
        val sDay = 15
        val sMonth = 8
        val sYear = 2024

        val (lDay, lMonth, lYear) = LunarCalendarEngine.convertSolar2Lunar(sDay, sMonth, sYear)
        val (backSDay, backSMonth, backSYear) = LunarCalendarEngine.convertLunar2Solar(lDay, lMonth, lYear)

        assertEquals(sDay, backSDay)
        assertEquals(sMonth, backSMonth)
        assertEquals(sYear, backSYear)
    }

    @Test
    fun testFullLunarDateCalculation() {
        val dateInfo = LunarCalendarEngine.getFullLunarDate(4, 9, 2026)

        assertNotNull(dateInfo.canChiYear)
        assertNotNull(dateInfo.canChiMonth)
        assertNotNull(dateInfo.canChiDay)
        assertNotNull(dateInfo.tietKhi)
        assertNotNull(dateInfo.truc)
        assertEquals(6, dateInfo.hoangDaoHours.size)
        assertTrue(dateInfo.goodStars.isNotEmpty())
        assertNotNull(dateInfo.hyThan)
        assertNotNull(dateInfo.taiThan)
    }

    @Test
    fun testHoroscopeCalculations() {
        // Kiểm tra tuổi 1990 (Canh Ngọ)
        val starMale = HoroscopeEngine.calculateCuuDieuStar(1990, 2026, true)
        val starFemale = HoroscopeEngine.calculateCuuDieuStar(1990, 2026, false)
        assertNotNull(starMale.name)
        assertNotNull(starFemale.name)

        // Nam 1990: Quái số 1 (Khảm)
        val batTrach1990Male = HoroscopeEngine.getBatTrach(1990, true)
        assertEquals(1, batTrach1990Male.quaiSo)
        assertEquals("Khảm (Thủy)", batTrach1990Male.cungMenh)
        assertEquals(4, batTrach1990Male.huongTot.size)
        assertEquals(4, batTrach1990Male.huongXau.size)

        // Nữ 1990: Quái số gốc là 5 -> Ngũ vi trung cung -> Quy về Cấn (Thổ, quái số 8)
        val batTrach1990Female = HoroscopeEngine.getBatTrach(1990, false)
        assertEquals(8, batTrach1990Female.quaiSo)
        assertEquals("Cấn (Thổ)", batTrach1990Female.cungMenh)

        // Nam 1995: 1+9+9+5 = 24 -> 6 -> 11 - 6 = 5 -> Nam quy về Khôn (Thổ, quái số 2)
        val batTrach1995Male = HoroscopeEngine.getBatTrach(1995, true)
        assertEquals(2, batTrach1995Male.quaiSo)
        assertEquals("Khôn (Thổ)", batTrach1995Male.cungMenh)
    }

    @Test
    fun testTetNguyenDanAndLeapMonth() {
        // Tết Giáp Thìn 2024: 10/02/2024 Dương lịch = Mùng 1 Tết Âm lịch
        val tet2024 = LunarCalendarEngine.getFullLunarDate(10, 2, 2024)
        assertEquals(1, tet2024.day)
        assertEquals(1, tet2024.month)
        assertEquals(2024, tet2024.year)
        assertEquals("Giáp Thìn", tet2024.canChiYear)
        assertEquals(false, tet2024.isLeap)

        // Tháng Nhuận năm Quý Mão 2023:
        // Ngày 22/03/2023 Dương lịch là ngày Mùng 1 Tháng 2 Nhuận Âm lịch
        val leapDate2023 = LunarCalendarEngine.getFullLunarDate(22, 3, 2023)
        assertEquals(1, leapDate2023.day)
        assertEquals(2, leapDate2023.month)
        assertEquals(true, leapDate2023.isLeap)

        // Chuyển ngược lại từ Âm sang Dương cho tháng nhuận
        val (sDay, sMonth, sYear) = LunarCalendarEngine.convertLunar2Solar(1, 2, 2023, isLeap = true)
        assertEquals(22, sDay)
        assertEquals(3, sMonth)
        assertEquals(2023, sYear)

        // Kiểm tra hàm xác định năm nhuận:
        // Năm 2023 nhuận tháng 2
        assertEquals(2, LunarCalendarEngine.getLeapMonthOfYear(2023))
        // Năm 2020 nhuận tháng 4
        assertEquals(4, LunarCalendarEngine.getLeapMonthOfYear(2020))
        // Năm 2024 không có tháng nhuận
        assertEquals(0, LunarCalendarEngine.getLeapMonthOfYear(2024))

        // Kiểm tra số ngày trong tháng âm lịch (29 hoặc 30 ngày)
        val days1 = LunarCalendarEngine.getDaysInLunarMonth(1, 2024)
        assertTrue(days1 == 29 || days1 == 30)
    }
}
