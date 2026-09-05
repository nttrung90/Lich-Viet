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

    @Test
    fun testBatTrachModuloZeroEdgeCases() {
        // Nam 1991: 1+9+9+1 = 20 -> 2. 11 - 2 = 9. (11 - sum) % 9 == 0 -> quaiSo phải là 9 (Ly - Hỏa)
        val male1991 = HoroscopeEngine.getBatTrach(1991, true)
        assertEquals(9, male1991.quaiSo)
        assertEquals("Ly (Hỏa)", male1991.cungMenh)

        // Nữ 1994: 1+9+9+4 = 23 -> 5. 5 + 4 = 9. (sum + 4) % 9 == 0 -> quaiSo phải là 9 (Ly - Hỏa)
        val female1994 = HoroscopeEngine.getBatTrach(1994, false)
        assertEquals(9, female1994.quaiSo)
        assertEquals("Ly (Hỏa)", female1994.cungMenh)
    }

    @Test
    fun testHolidayMatching() {
        // 1/1 Dương lịch
        val h1 = com.lichviet.vannien.data.HolidayRepository.getHoliday(1, 1, 20, 11, 2024)
        assertNotNull(h1)
        assertTrue(h1!!.contains("Tết Dương Lịch"))

        // Rằm tháng Giêng (15/1 Âm lịch)
        val h2 = com.lichviet.vannien.data.HolidayRepository.getHoliday(24, 2, 15, 1, 2024)
        assertNotNull(h2)
        assertTrue(h2!!.contains("Rằm tháng Giêng"))
    }

    @Test
    fun testHoangOcCalculation() {
        // Tuổi 10 -> Nhất Cát (Tốt)
        val (good10, name10) = HoroscopeEngine.checkHoangOc(2017, 2026) // age = 10
        assertTrue(good10)
        assertTrue(name10.startsWith("Nhất Cát"))

        // Tuổi 20 -> Nhì Nghi (Tốt)
        val (good20, name20) = HoroscopeEngine.checkHoangOc(2007, 2026) // age = 20
        assertTrue(good20)
        assertTrue(name20.startsWith("Nhì Nghi"))

        // Tuổi 30 -> Tam Địa Sát (Xấu)
        val (good30, name30) = HoroscopeEngine.checkHoangOc(1997, 2026) // age = 30
        assertTrue(!good30)
        assertTrue(name30.startsWith("Tam Địa Sát"))

        // Tuổi 40 -> Tứ Tấn Tài (Tốt)
        val (good40, name40) = HoroscopeEngine.checkHoangOc(1987, 2026) // age = 40
        assertTrue(good40)
        assertTrue(name40.startsWith("Tứ Tấn Tài"))

        // Tuổi 50 -> Ngũ Thọ Tử (Xấu)
        val (good50, name50) = HoroscopeEngine.checkHoangOc(1977, 2026) // age = 50
        assertTrue(!good50)
        assertTrue(name50.startsWith("Ngũ Thọ Tử"))

        // Tuổi 60 -> Lục Hoang Ốc (Xấu)
        val (good60, name60) = HoroscopeEngine.checkHoangOc(1967, 2026) // age = 60
        assertTrue(!good60)
        assertTrue(name60.startsWith("Lục Hoang Ốc"))

        // Tuổi 61 -> Nhất Cát (Tốt)
        val (good61, name61) = HoroscopeEngine.checkHoangOc(1966, 2026) // age = 61
        assertTrue(good61)
        assertTrue(name61.startsWith("Nhất Cát"))
    }

    @Test
    fun testHoangDaoHoursAllSixGroups() {
        // Nhóm 0: Tý, Ngọ -> {Thân, Dậu, Tý, Sửu, Mão, Ngọ}
        val hoursTy = LunarCalendarEngine.calculateHoangDaoHours("Tý", "Giáp").map { it.chi }
        assertEquals(listOf("Tý", "Sửu", "Mão", "Ngọ", "Thân", "Dậu"), hoursTy)

        val hoursNgo = LunarCalendarEngine.calculateHoangDaoHours("Ngọ", "Giáp").map { it.chi }
        assertEquals(listOf("Tý", "Sửu", "Mão", "Ngọ", "Thân", "Dậu"), hoursNgo)

        // Nhóm 1: Sửu, Mùi -> {Tuất, Hợi, Dần, Mão, Tỵ, Thân}
        val hoursSuu = LunarCalendarEngine.calculateHoangDaoHours("Sửu", "Ất").map { it.chi }
        assertEquals(listOf("Dần", "Mão", "Tỵ", "Thân", "Tuất", "Hợi"), hoursSuu)

        val hoursMui = LunarCalendarEngine.calculateHoangDaoHours("Mùi", "Ất").map { it.chi }
        assertEquals(listOf("Dần", "Mão", "Tỵ", "Thân", "Tuất", "Hợi"), hoursMui)

        // Nhóm 2: Dần, Thân -> {Tý, Sửu, Thìn, Tỵ, Mùi, Tuất}
        val hoursDan = LunarCalendarEngine.calculateHoangDaoHours("Dần", "Bính").map { it.chi }
        assertEquals(listOf("Tý", "Sửu", "Thìn", "Tỵ", "Mùi", "Tuất"), hoursDan)

        val hoursThan = LunarCalendarEngine.calculateHoangDaoHours("Thân", "Bính").map { it.chi }
        assertEquals(listOf("Tý", "Sửu", "Thìn", "Tỵ", "Mùi", "Tuất"), hoursThan)

        // Nhóm 3: Mão, Dậu -> {Tý, Dần, Mão, Ngọ, Mùi, Dậu}
        val hoursMao = LunarCalendarEngine.calculateHoangDaoHours("Mão", "Đinh").map { it.chi }
        assertEquals(listOf("Tý", "Dần", "Mão", "Ngọ", "Mùi", "Dậu"), hoursMao)

        val hoursDau = LunarCalendarEngine.calculateHoangDaoHours("Dậu", "Đinh").map { it.chi }
        assertEquals(listOf("Tý", "Dần", "Mão", "Ngọ", "Mùi", "Dậu"), hoursDau)

        // Nhóm 4: Thìn, Tuất -> {Dần, Thìn, Tỵ, Thân, Dậu, Hợi}
        val hoursThin = LunarCalendarEngine.calculateHoangDaoHours("Thìn", "Mậu").map { it.chi }
        assertEquals(listOf("Dần", "Thìn", "Tỵ", "Thân", "Dậu", "Hợi"), hoursThin)

        val hoursTuat = LunarCalendarEngine.calculateHoangDaoHours("Tuất", "Mậu").map { it.chi }
        assertEquals(listOf("Dần", "Thìn", "Tỵ", "Thân", "Dậu", "Hợi"), hoursTuat)

        // Nhóm 5: Tỵ, Hợi -> {Sửu, Thìn, Ngọ, Mùi, Tuất, Hợi}
        val hoursTy5 = LunarCalendarEngine.calculateHoangDaoHours("Tỵ", "Kỷ").map { it.chi }
        assertEquals(listOf("Sửu", "Thìn", "Ngọ", "Mùi", "Tuất", "Hợi"), hoursTy5)

        val hoursHoi = LunarCalendarEngine.calculateHoangDaoHours("Hợi", "Kỷ").map { it.chi }
        assertEquals(listOf("Sửu", "Thìn", "Ngọ", "Mùi", "Tuất", "Hợi"), hoursHoi)
    }

    @Test
    fun testNapAmCorrection() {
        // Năm 1968 là Mậu Thân -> Đại Dịch Thổ
        val (napAm1968, hanh1968) = HoroscopeEngine.getNapAm(1968)
        assertEquals("Đại Dịch Thổ", napAm1968)
        assertEquals("Thổ", hanh1968)

        // Năm 1969 là Kỷ Dậu -> Đại Dịch Thổ
        val (napAm1969, hanh1969) = HoroscopeEngine.getNapAm(1969)
        assertEquals("Đại Dịch Thổ", napAm1969)
        assertEquals("Thổ", hanh1969)
    }

    @Test
    fun testTietKhiBoundaryDay() {
        // Ngày 04/02/2024 là ngày Lập Xuân (chuyển tiết lúc ~16:27 UTC+7)
        val dateLapXuan = LunarCalendarEngine.getFullLunarDate(4, 2, 2024)
        assertEquals("Lập Xuân", dateLapXuan.tietKhi)

        // Ngày 03/02/2024 vẫn là tiết Đại Hàn
        val dateBeforeLapXuan = LunarCalendarEngine.getFullLunarDate(3, 2, 2024)
        assertEquals("Đại Hàn", dateBeforeLapXuan.tietKhi)

        // Ngày 20/03/2024 là ngày Xuân Phân (chuyển tiết lúc ~10:06 UTC+7)
        val dateXuanPhan = LunarCalendarEngine.getFullLunarDate(20, 3, 2024)
        assertEquals("Xuân Phân", dateXuanPhan.tietKhi)
    }

    @Test
    fun testLunar30To29MonthConversion() {
        var shortMonth = 1
        val shortYear = 2024
        for (m in 1..12) {
            if (LunarCalendarEngine.getDaysInLunarMonth(m, shortYear) == 29) {
                shortMonth = m
                break
            }
        }
        assertEquals(29, LunarCalendarEngine.getDaysInLunarMonth(shortMonth, shortYear))

        val maxDays = LunarCalendarEngine.getDaysInLunarMonth(shortMonth, shortYear)
        val clampedDay = 30.coerceAtMost(maxDays)
        assertEquals(29, clampedDay)

        val (sD, sM, sY) = LunarCalendarEngine.convertLunar2Solar(clampedDay, shortMonth, shortYear)
        val backLunar = LunarCalendarEngine.convertSolar2Lunar(sD, sM, sY)
        assertEquals(29, backLunar.day)
        assertEquals(shortMonth, backLunar.month)
        assertEquals(shortYear, backLunar.year)
    }
}
