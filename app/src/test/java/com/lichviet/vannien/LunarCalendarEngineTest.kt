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

        val batTrach = HoroscopeEngine.getBatTrach(1990, true)
        assertEquals(4, batTrach.huongTot.size)
        assertEquals(4, batTrach.huongXau.size)
    }
}
