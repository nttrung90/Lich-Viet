package com.lichviet.vannien.calendar

import java.util.Calendar
import kotlin.math.PI
import kotlin.math.floor
import kotlin.math.sin

/**
 * Thuật toán tính Âm Lịch Việt Nam chuẩn xác tuyệt đối (Múi giờ UTC+7 - Hà Nội).
 * Dựa trên thuật toán thiên văn của TS. Hồ Ngọc Đức (Viện Tin học, Đại học Leipzig).
 */
object LunarCalendarEngine {

    const val TIMEZONE = 7.0 // UTC+7

    val CAN = arrayOf("Giáp", "Ất", "Bính", "Đinh", "Mậu", "Kỷ", "Canh", "Tân", "Nhâm", "Quý")
    val CHI = arrayOf("Tý", "Sửu", "Dần", "Mão", "Thìn", "Tỵ", "Ngọ", "Mùi", "Thân", "Dậu", "Tuất", "Hợi")
    val CON_GIAP = arrayOf("Chuột", "Trâu", "Hổ", "Mèo", "Rồng", "Rắn", "Ngựa", "Dê", "Khỉ", "Gà", "Chó", "Lợn")

    val TIET_KHI = arrayOf(
        "Xuân Phân", "Thanh Minh", "Cốc Vũ", "Lập Hạ", "Tiểu Mãn", "Mang Chủng",
        "Hạ Chí", "Tiểu Thử", "Đại Thử", "Lập Thu", "Xử Thử", "Bạch Lộ",
        "Thu Phân", "Hàn Lộ", "Sương Giáng", "Lập Đông", "Tiểu Tuyết", "Đại Tuyết",
        "Đông Chí", "Tiểu Hàn", "Đại Hàn", "Lập Xuân", "Vũ Thủy", "Kinh Trập"
    )

    val TRUC = arrayOf("Kiến", "Trừ", "Mãn", "Bình", "Định", "Chấp", "Phá", "Nguy", "Thành", "Thâu", "Khai", "Bế")

    val NHI_THAP_BAT_TU = arrayOf(
        "Giác", "Cang", "Đê", "Phòng", "Tâm", "Vĩ", "Cơ",
        "Đẩu", "Ngưu", "Nữ", "Hư", "Nguy", "Thất", "Bích",
        "Khuê", "Lâu", "Vị", "Mão", "Tất", "Chủy", "Sâm",
        "Tỉnh", "Quỷ", "Liễu", "Tinh", "Trương", "Dực", "Chẩn"
    )

    data class LunarDate(
        val day: Int,
        val month: Int,
        val year: Int,
        val isLeap: Boolean,
        val canChiDay: String,
        val canChiMonth: String,
        val canChiYear: String,
        val solarDay: Int,
        val solarMonth: Int,
        val solarYear: Int,
        val dayOfWeek: String,
        val tietKhi: String,
        val isHoangDao: Boolean,
        val hoangDaoHours: List<HoangDaoHour>,
        val hyThan: String,
        val taiThan: String,
        val hacThan: String,
        val truc: String,
        val saoNhiThapBatTu: String,
        val goodStars: List<String>,
        val badStars: List<String>,
        val shouldDo: List<String>,
        val shouldAvoid: List<String>
    )

    data class LunarDayResult(
        val day: Int,
        val month: Int,
        val year: Int,
        val isLeap: Boolean = false
    )

    data class HoangDaoHour(
        val name: String,
        val chi: String,
        val timeRange: String,
        val isHoangDao: Boolean
    )

    /**
     * Chuyển đổi ngày dương lịch sang số ngày Julian (JDN)
     */
    fun jdFromDate(dd: Int, mm: Int, yy: Int): Int {
        val a = (14 - mm) / 12
        val y = yy + 4800 - a
        val m = mm + 12 * a - 3
        var jd = dd + (153 * m + 2) / 5 + 365 * y + y / 4 - y / 100 + y / 400 - 32045
        if (jd < 2299161) {
            jd = dd + (153 * m + 2) / 5 + 365 * y + y / 4 - 32083
        }
        return jd
    }

    /**
     * Chuyển số ngày Julian sang ngày dương lịch
     */
    fun jdToDate(jd: Int): Triple<Int, Int, Int> {
        val a: Long = if (jd > 2299160) {
            val aAlpha = (((jd - 1867216.25) / 36524.25).toLong())
            jd + 1 + aAlpha - aAlpha / 4
        } else {
            jd.toLong()
        }
        val b = a + 1524
        val c = ((b - 122.1) / 365.25).toLong()
        val d = (365.25 * c).toLong()
        val e = ((b - d) / 30.6001).toLong()
        val day = (b - d - (30.6001 * e).toLong()).toInt()
        val month = (if (e < 14) e - 1 else e - 13).toInt()
        val year = (if (month > 2) c - 4716 else c - 4715).toInt()
        return Triple(day, month, year)
    }

    /**
     * Tính thời điểm điểm Sóc (New Moon) theo thuật toán thiên văn của TS. Hồ Ngọc Đức
     */
    fun getNewMoonDay(k: Int, timeZone: Double = TIMEZONE): Int {
        val t = k / 1236.85
        val t2 = t * t
        val t3 = t2 * t
        val dr = PI / 180.0

        var jd1 = 2415020.75933 + 29.53058868 * k + 0.0001178 * t2 - 0.000000155 * t3
        jd1 += 0.00033 * sin((166.56 + 132.87 * t - 0.009173 * t2) * dr)

        val m = (359.2242 + 29.10535608 * k - 0.0000333 * t2 - 0.00000347 * t3) * dr
        val mprime = (306.0253 + 385.81691806 * k + 0.0107306 * t2 + 0.00001236 * t3) * dr
        val f = (21.2964 + 390.67050646 * k - 0.0016528 * t2 - 0.00000239 * t3) * dr

        var c1 = (0.1734 - 0.000393 * t) * sin(m) + 0.0021 * sin(2 * m)
        c1 -= 0.4068 * sin(mprime) - 0.0161 * sin(2 * mprime)
        c1 -= 0.0004 * sin(3 * mprime)
        c1 += 0.0104 * sin(2 * f) - 0.0051 * sin(m + mprime)
        c1 -= 0.0074 * sin(m - mprime) + 0.0004 * sin(2 * f + m)
        c1 -= 0.0004 * sin(2 * f - m) - 0.0006 * sin(2 * f + mprime)
        c1 += 0.0010 * sin(2 * f - mprime) + 0.0005 * sin(2 * mprime + m)

        val del = if (t < -11.0) {
            0.001 + 0.000839 * t + 0.0002261 * t2 - 0.00000845 * t3 - 0.000000081 * t * t3
        } else {
            -0.000278 + 0.000265 * t + 0.000262 * t2
        }

        val jdNew = jd1 + c1 - del
        return floor(jdNew + 0.5 + timeZone / 24.0).toInt()
    }

    /**
     * Tính kinh độ Mặt Trời (tính bằng radian từ 0 đến 2*PI)
     */
    private fun sunLongitudeRadian(jdn: Double): Double {
        val t = (jdn - 2451545.0) / 36525.0
        val t2 = t * t
        val dr = PI / 180.0
        val l0 = 280.46645 + 36000.76983 * t + 0.0003032 * t2
        val m = 357.52910 + 35999.05030 * t - 0.0001559 * t2 - 0.00000048 * t * t2
        val c = (1.914600 - 0.004817 * t - 0.000014 * t2) * sin(m * dr) +
                (0.019993 - 0.000101 * t) * sin(2 * m * dr) +
                0.000290 * sin(3 * m * dr)
        var l = (l0 + c) * dr
        l -= 2 * PI * floor(l / (2 * PI))
        return l
    }

    /**
     * Tính kinh độ Mặt Trời (tính bằng cung 30 độ: 0..11) để xác định Trung Khí
     */
    fun getSunLongitudeMajor(dayNumber: Int, timeZone: Double = TIMEZONE): Int {
        val rad = sunLongitudeRadian(dayNumber - 0.5 - timeZone / 24.0)
        return floor(rad / PI * 6.0).toInt()
    }

    /**
     * Tính kinh độ Mặt Trời tại ngày JDN (0.0..360.0 độ) để tính tiết khí
     */
    fun getSunLongitude(jdn: Int, timeZone: Double = TIMEZONE): Double {
        val rad = sunLongitudeRadian(jdn - 0.5 - timeZone / 24.0)
        var deg = rad * 180.0 / PI
        deg %= 360.0
        if (deg < 0) deg += 360.0
        return deg
    }

    /**
     * Tìm ngày Sóc của tháng 11 âm lịch (tháng chứa điểm Đông Chí) theo TS. Hồ Ngọc Đức
     */
    fun getLunarMonth11(yy: Int, timeZone: Double = TIMEZONE): Int {
        val jdDec31 = jdFromDate(31, 12, yy)
        val k = floor((jdDec31 - 2415021.0769986) / 29.530588853).toInt()
        var nm = getNewMoonDay(k, timeZone)
        val sunLong = getSunLongitudeMajor(nm, timeZone)
        if (sunLong >= 9) {
            nm = getNewMoonDay(k - 1, timeZone)
        }
        return nm
    }

    /**
     * Tìm vị trí tháng nhuận (tính từ tháng 11 âm lịch năm trước)
     */
    fun getLeapMonthOffset(a11: Int, timeZone: Double = TIMEZONE): Int {
        val k = floor((a11 - 2415021.0769986) / 29.530588853 + 0.5).toInt()
        var last = 0
        var i = 1 // Bắt đầu từ tháng sau tháng 11 âm lịch
        var arc = getSunLongitudeMajor(getNewMoonDay(k + i, timeZone), timeZone)
        do {
            last = arc
            i++
            arc = getSunLongitudeMajor(getNewMoonDay(k + i, timeZone), timeZone)
        } while (arc != last && i < 14)
        return i - 1
    }

    /**
     * Chuyển đổi Dương Lịch sang Âm Lịch Việt Nam (chuẩn thuật toán TS. Hồ Ngọc Đức)
     */
    fun convertSolar2Lunar(dd: Int, mm: Int, yy: Int, timeZone: Double = TIMEZONE): LunarDayResult {
        val dayNumber = jdFromDate(dd, mm, yy)
        val k = floor((dayNumber - 2415021.0769986) / 29.530588853).toInt()
        var monthStart = getNewMoonDay(k + 1, timeZone)
        if (monthStart > dayNumber) {
            monthStart = getNewMoonDay(k, timeZone)
        }

        var a11 = getLunarMonth11(yy, timeZone)
        var b11 = a11
        var lunarYear = yy
        if (a11 >= monthStart) {
            lunarYear = yy
            a11 = getLunarMonth11(yy - 1, timeZone)
        } else {
            lunarYear = yy + 1
            b11 = getLunarMonth11(yy + 1, timeZone)
        }

        val lunarDay = dayNumber - monthStart + 1
        val diff = floor((monthStart - a11) / 29.0).toInt()
        var isLeap = false
        var lunarMonth = diff + 11

        if (b11 - a11 > 365) {
            val leapMonthDiff = getLeapMonthOffset(a11, timeZone)
            if (diff >= leapMonthDiff) {
                lunarMonth = diff + 10
                if (diff == leapMonthDiff) {
                    isLeap = true
                }
            }
        }

        if (lunarMonth > 12) {
            lunarMonth -= 12
        }
        if (lunarMonth >= 11 && diff < 4) {
            lunarYear -= 1
        }

        return LunarDayResult(lunarDay, lunarMonth, lunarYear, isLeap)
    }

    /**
     * Chuyển đổi Âm Lịch sang Dương Lịch (hỗ trợ cả tháng nhuận chuẩn xác)
     */
    fun convertLunar2Solar(
        lunarDay: Int,
        lunarMonth: Int,
        lunarYear: Int,
        isLeap: Boolean = false,
        timeZone: Double = TIMEZONE
    ): Triple<Int, Int, Int> {
        val a11: Int
        val b11: Int
        if (lunarMonth < 11) {
            a11 = getLunarMonth11(lunarYear - 1, timeZone)
            b11 = getLunarMonth11(lunarYear, timeZone)
        } else {
            a11 = getLunarMonth11(lunarYear, timeZone)
            b11 = getLunarMonth11(lunarYear + 1, timeZone)
        }

        val k = floor((a11 - 2415021.0769986) / 29.530588853 + 0.5).toInt()
        var off = lunarMonth - 11
        if (off < 0) {
            off += 12
        }

        if (b11 - a11 > 365) {
            val leapOff = getLeapMonthOffset(a11, timeZone)
            var leapMonth = leapOff - 2
            if (leapMonth < 0) {
                leapMonth += 12
            }
            if (isLeap && lunarMonth != leapMonth) {
                return Triple(0, 0, 0) // Tháng nhuận không hợp lệ
            } else if (isLeap || off >= leapOff) {
                off += 1
            }
        }

        val targetMonthStart = getNewMoonDay(k + off, timeZone)
        val targetJd = targetMonthStart + lunarDay - 1
        return jdToDate(targetJd)
    }

    /**
     * Tính đầy đủ thông tin Âm Lịch và Phong Thủy cho một ngày Dương Lịch
     */
    fun getFullLunarDate(dd: Int, mm: Int, yy: Int): LunarDate {
        val lunarRes = convertSolar2Lunar(dd, mm, yy)
        val lunarDay = lunarRes.day
        val lunarMonth = lunarRes.month
        val lunarYear = lunarRes.year
        val isLeap = lunarRes.isLeap
        val jd = jdFromDate(dd, mm, yy)

        // Can Chi Năm (theo Can Chi năm âm lịch)
        val canYearIndex = (lunarYear + 6) % 10
        val canYear = CAN[canYearIndex]
        val chiYear = CHI[(lunarYear + 8) % 12]
        val canChiYear = "$canYear $chiYear"

        // Can Chi Tháng (quy tắc Ngũ Hổ Độn)
        val canMonthIndex = ((canYearIndex % 5) * 2 + 2 + (lunarMonth - 1)) % 10
        val canMonth = CAN[canMonthIndex]
        val chiMonth = CHI[(lunarMonth + 1) % 12]
        val canChiMonth = "$canMonth $chiMonth"

        // Can Chi Ngày
        val canDay = CAN[(jd + 9) % 10]
        val chiDay = CHI[(jd + 1) % 12]
        val canChiDay = "$canDay $chiDay"

        // Thứ trong tuần
        val dayOfWeekIndex = (jd + 1) % 7
        val daysOfWeek = arrayOf("Chủ Nhật", "Thứ Hai", "Thứ Ba", "Thứ Tư", "Thứ Năm", "Thứ Sáu", "Thứ Bảy")
        val dayOfWeek = daysOfWeek[dayOfWeekIndex]

        // Tiết khí (từ 0 đến 23)
        val sunLong = getSunLongitude(jd, TIMEZONE)
        val tietKhiIndex = floor(sunLong / 15.0).toInt() % 24
        val tietKhi = TIET_KHI[tietKhiIndex]

        // Trực của ngày
        val trucIndex = (chiDayIndex(chiDay) - chiMonthIndex(chiMonth) + 12) % 12
        val truc = TRUC[trucIndex]

        // Nhị thập bát tú
        val saoIndex = (jd + 18) % 28
        val saoNhiThapBatTu = NHI_THAP_BAT_TU[saoIndex]

        // Hoàng Đạo / Hắc Đạo của ngày
        val isHoangDao = checkDayHoangDao(chiDay, chiMonth)

        // 6 Giờ Hoàng Đạo trong ngày
        val hoangDaoHours = calculateHoangDaoHours(chiDay, canDay)

        // Hướng xuất hành
        val hyThan = getHyThan(canDay)
        val taiThan = getTaiThan(canDay)
        val hacThan = getHacThan(chiDay)

        // Sao tốt và sao xấu
        val goodStars = getGoodStars(chiDay, lunarMonth)
        val badStars = getBadStars(chiDay, lunarMonth, lunarDay)

        // Việc nên làm / kiêng cữ
        val (shouldDo, shouldAvoid) = getActionRecommendations(isHoangDao, truc)

        return LunarDate(
            day = lunarDay,
            month = lunarMonth,
            year = lunarYear,
            isLeap = isLeap,
            canChiDay = canChiDay,
            canChiMonth = canChiMonth,
            canChiYear = canChiYear,
            solarDay = dd,
            solarMonth = mm,
            solarYear = yy,
            dayOfWeek = dayOfWeek,
            tietKhi = tietKhi,
            isHoangDao = isHoangDao,
            hoangDaoHours = hoangDaoHours,
            hyThan = hyThan,
            taiThan = taiThan,
            hacThan = hacThan,
            truc = truc,
            saoNhiThapBatTu = saoNhiThapBatTu,
            goodStars = goodStars,
            badStars = badStars,
            shouldDo = shouldDo,
            shouldAvoid = shouldAvoid
        )
    }

    private fun chiDayIndex(chi: String): Int = CHI.indexOf(chi).coerceAtLeast(0)
    private fun chiMonthIndex(chi: String): Int = CHI.indexOf(chi).coerceAtLeast(0)

    /**
     * Xác định ngày Hoàng Đạo hay Hắc Đạo
     */
    private fun checkDayHoangDao(chiDay: String, chiMonth: String): Boolean {
        val monthGroup = when (chiMonth) {
            "Dần", "Thân" -> 0
            "Mão", "Dậu" -> 1
            "Thìn", "Tuất" -> 2
            "Tỵ", "Hợi" -> 3
            "Tý", "Ngọ" -> 4
            else -> 5 // Sửu, Mùi
        }
        val hoangDaoDays = arrayOf(
            arrayOf("Tý", "Sửu", "Thìn", "Tỵ", "Mùi", "Tuất"),
            arrayOf("Dần", "Mão", "Ngọ", "Mùi", "Dậu", "Tý"),
            arrayOf("Thìn", "Tỵ", "Thân", "Dậu", "Hợi", "Dần"),
            arrayOf("Ngọ", "Mùi", "Tuất", "Hợi", "Sửu", "Thìn"),
            arrayOf("Thân", "Dậu", "Tý", "Sửu", "Mão", "Ngọ"),
            arrayOf("Tuất", "Hợi", "Dần", "Mão", "Tỵ", "Thân")
        )
        return hoangDaoDays[monthGroup].contains(chiDay)
    }

    /**
     * Tính 6 Giờ Hoàng Đạo trong 12 giờ của ngày
     */
    private fun calculateHoangDaoHours(chiDay: String, canDay: String): List<HoangDaoHour> {
        val chiDayIdx = chiDayIndex(chiDay)
        val hoangDaoMask = when (chiDayIdx % 6) {
            0 -> intArrayOf(1, 1, 0, 0, 1, 1, 0, 1, 0, 0, 1, 0) // Tý, Ngọ
            1 -> intArrayOf(0, 0, 1, 1, 0, 1, 0, 0, 1, 1, 0, 1) // Sửu, Mùi
            2 -> intArrayOf(1, 1, 0, 0, 1, 1, 0, 1, 0, 0, 1, 0) // Dần, Thân
            3 -> intArrayOf(1, 0, 1, 1, 0, 0, 1, 1, 0, 1, 0, 0) // Mão, Dậu
            4 -> intArrayOf(0, 0, 1, 0, 1, 1, 0, 0, 1, 1, 0, 1) // Thìn, Tuất
            else -> intArrayOf(0, 1, 0, 0, 1, 0, 1, 1, 0, 0, 1, 1) // Tỵ, Hợi
        }

        val hourCanStart = when (canDay) {
            "Giáp", "Kỷ" -> 0 // Giáp Tý
            "Ất", "Canh" -> 2 // Bính Tý
            "Bính", "Tân" -> 4 // Mậu Tý
            "Đinh", "Nhâm" -> 6 // Canh Tý
            else -> 8 // Nhâm Tý
        }

        val timeRanges = arrayOf(
            "23h-1h", "1h-3h", "3h-5h", "5h-7h", "7h-9h", "9h-11h",
            "11h-13h", "13h-15h", "15h-17h", "17h-19h", "19h-21h", "21h-23h"
        )

        val result = mutableListOf<HoangDaoHour>()
        for (i in 0 until 12) {
            if (hoangDaoMask[i] == 1) {
                val hourCan = CAN[(hourCanStart + i) % 10]
                val hourChi = CHI[i]
                result.add(
                    HoangDaoHour(
                        name = "$hourCan $hourChi",
                        chi = hourChi,
                        timeRange = timeRanges[i],
                        isHoangDao = true
                    )
                )
            }
        }
        return result
    }

    /**
     * Hướng Hỷ Thần
     */
    private fun getHyThan(canDay: String): String = when (canDay) {
        "Giáp", "Kỷ" -> "Đông Bắc"
        "Ất", "Canh" -> "Tây Bắc"
        "Bính", "Tân" -> "Tây Nam"
        "Đinh", "Nhâm" -> "Chính Nam"
        else -> "Đông Nam" // Mậu, Quý
    }

    /**
     * Hướng Tài Thần
     */
    private fun getTaiThan(canDay: String): String = when (canDay) {
        "Giáp", "Kỷ" -> "Chính Nam"
        "Ất", "Canh" -> "Tây Nam"
        "Bính", "Tân" -> "Chính Đông"
        "Đinh", "Nhâm" -> "Chính Tây"
        else -> "Chính Bắc" // Mậu, Quý
    }

    /**
     * Hướng Hạc Thần (nên tránh)
     */
    private fun getHacThan(chiDay: String): String = when (chiDay) {
        "Tý", "Sửu" -> "Tây Nam"
        "Dần", "Mão" -> "Chính Nam"
        "Thìn", "Tỵ" -> "Đông Nam"
        "Ngọ", "Mùi" -> "Chính Bắc"
        "Thân", "Dậu" -> "Đông Bắc"
        else -> "Tây Bắc" // Tuất, Hợi
    }

    /**
     * Danh sách Sao Tốt
     */
    private fun getGoodStars(chiDay: String, lunarMonth: Int): List<String> {
        val stars = mutableListOf<String>()
        stars.add("Thiên Đức: Tốt mọi việc, hóa giải hung sát")
        stars.add("Nguyệt Đức: Cầu tài lộc, kinh doanh may mắn")
        if (lunarMonth in listOf(1, 4, 7, 10)) {
            stars.add("Sinh Khí: Khởi công, động thổ cát lợi")
        }
        if (chiDay in listOf("Tý", "Ngọ", "Mão", "Dậu")) {
            stars.add("Thiên Hỷ: Cưới hỏi, cầu tự, đoàn tụ gia đình")
        }
        stars.add("Phúc Sinh: Gặp quý nhân phù trợ, bình an")
        return stars
    }

    /**
     * Danh sách Sao Xấu
     */
    private fun getBadStars(chiDay: String, lunarMonth: Int, lunarDay: Int): List<String> {
        val bad = mutableListOf<String>()
        if (lunarDay in listOf(5, 14, 23)) {
            bad.add("Nguyệt Kỵ: Mùng năm, mười bốn, hai ba - đi chơi còn thiệt nữa là đi buôn")
        }
        if (lunarDay in listOf(3, 7, 13, 18, 22, 27)) {
            bad.add("Tam Nương: Tránh khai trương, cưới gả, ký kết lớn")
        }
        if (chiDay in listOf("Thìn", "Tuất")) {
            bad.add("Đại Hao: Tránh đầu tư mạo hiểm, hao tài tốn của")
        } else {
            bad.add("Tiểu Hồng Sa: Cần cẩn trọng khi đi đường xa")
        }
        return bad
    }

    /**
     * Việc nên làm và kiêng cữ theo Hoàng Đạo và Trực
     */
    private fun getActionRecommendations(isHoangDao: Boolean, truc: String): Pair<List<String>, List<String>> {
        val shouldDo = mutableListOf<String>()
        val shouldAvoid = mutableListOf<String>()

        if (isHoangDao) {
            shouldDo.add("Cúng tế, cầu tài, cầu an")
            shouldDo.add("Xuất hành, mở cửa hàng, giao dịch ký kết")
            shouldDo.add("Gặp gỡ đối tác, dạm ngõ, ăn hỏi")
        } else {
            shouldDo.add("Làm việc thiện, tu dưỡng, an dưỡng")
            shouldDo.add("Thu dọn nhà cửa, an định tâm trí")
        }

        when (truc) {
            "Kiến", "Khai" -> {
                shouldDo.add("Khai trương, bắt đầu công việc mới")
                shouldAvoid.add("Động thổ, an táng")
            }
            "Trừ", "Phá" -> {
                shouldDo.add("Trừ phục, dọn dẹp, chữa bệnh")
                shouldAvoid.add("Cưới hỏi, xuất hành xa")
            }
            "Thành", "Định" -> {
                shouldDo.add("Ký hợp đồng, cưới hỏi, vào nhà mới")
                shouldAvoid.add("Tranh chấp, kiện tụng")
            }
            else -> {
                shouldDo.add("Các công việc thường nhật thuận lợi")
                shouldAvoid.add("Vội vàng quyết định những việc đại sự")
            }
        }

        return Pair(shouldDo, shouldAvoid)
    }

    /**
     * Lấy Can Chi cho một giờ cụ thể trong ngày
     */
    fun getCanChiHour(hour: Int, canDay: String): String {
        val chiIndex = ((hour + 1) / 2) % 12
        val hourChi = CHI[chiIndex]
        val hourCanStart = when (canDay) {
            "Giáp", "Kỷ" -> 0
            "Ất", "Canh" -> 2
            "Bính", "Tân" -> 4
            "Đinh", "Nhâm" -> 6
            else -> 8
        }
        val hourCan = CAN[(hourCanStart + chiIndex) % 10]
        return "$hourCan $hourChi"
    }
}
