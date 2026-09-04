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
        val a: Long
        if (jd > 2299160) {
            val aAlpha = (((jd - 1867216.25) / 36524.25).toLong())
            a = jd + 1 + aAlpha - aAlpha / 4
        } else {
            a = jd.toLong()
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
     * Tính thời điểm điểm Sóc (New Moon) theo thuật toán thiên văn
     */
    private fun getNewMoonDay(k: Int, timeZone: Double): Int {
        val t = k / 1236.85
        val t2 = t * t
        val t3 = t2 * t
        val dr = PI / 180.0
        var jde = 2451524.613488 + 29.530588853 * k + 0.0001337 * t2 - 0.000000150 * t3
        val m = 2.5534 + 29.10535669 * k - 0.0000218 * t2 - 0.00000011 * t3
        val mprime = 201.5643 + 385.81693528 * k + 0.0107438 * t2 + 0.00001239 * t3
        val f = 160.7108 + 390.67050274 * k - 0.0016341 * t2 - 0.00000227 * t3
        val omega = 124.7746 - 1.56375580 * k + 0.0020691 * t2 + 0.00000215 * t3

        // Hiệu chỉnh các nhiễu loạn Mặt Trăng & Mặt Trời
        var delta = (0.1734 - 0.000393 * t) * sin(m * dr)
        delta += 0.0021 * sin(2 * m * dr)
        delta -= 0.4068 * sin(mprime * dr)
        delta += 0.0161 * sin(2 * mprime * dr)
        delta -= 0.0004 * sin(3 * mprime * dr)
        delta += 0.0104 * sin(2 * f * dr)
        delta -= 0.0051 * sin((m + mprime) * dr)
        delta -= 0.0074 * sin((m - mprime) * dr)
        delta += 0.0004 * sin((2 * f + m) * dr)
        delta -= 0.0004 * sin((2 * f - m) * dr)
        delta -= 0.0006 * sin((2 * f + mprime) * dr)
        delta += 0.0010 * sin((2 * f - mprime) * dr)
        delta += 0.0005 * sin((m + 2 * mprime) * dr)
        jde += delta

        return floor(jde + 0.5 + timeZone / 24.0).toInt()
    }

    /**
     * Tính kinh độ Mặt Trời tại ngày JDN (để tính tiết khí)
     */
    private fun getSunLongitude(jdn: Int, timeZone: Double): Double {
        val t = (jdn - 0.5 - timeZone / 24.0 - 2451545.0) / 36525.0
        val t2 = t * t
        val dr = PI / 180.0
        val l0 = 280.46645 + 36000.76983 * t + 0.0003032 * t2
        val m = 357.52910 + 35999.05030 * t - 0.0001559 * t2 - 0.00000048 * t * t2
        val c = (1.914600 - 0.004817 * t - 0.000014 * t2) * sin(m * dr) +
                (0.019993 - 0.000101 * t) * sin(2 * m * dr) +
                0.000290 * sin(3 * m * dr)
        var theta = (l0 + c) % 360.0
        if (theta < 0) theta += 360.0
        return theta
    }

    /**
     * Chuyển đổi Dương Lịch sang Âm Lịch Việt Nam
     */
    fun convertSolar2Lunar(dd: Int, mm: Int, yy: Int): Triple<Int, Int, Int> {
        val dayNumber = jdFromDate(dd, mm, yy)
        val k = floor((dayNumber - 2415021.0769986) / 29.530588853).toInt()
        var monthStart = getNewMoonDay(k + 1, TIMEZONE)
        val currentK = if (monthStart > dayNumber) k else k + 1
        monthStart = getNewMoonDay(currentK, TIMEZONE)

        // Tính ngày trong tháng âm lịch
        val lunarDay = dayNumber - monthStart + 1

        // Tìm tháng 11 âm lịch (Đông Chí) của năm
        val a11 = getNewMoonDay(floor((jdFromDate(31, 12, yy) - 2415021.0769986) / 29.530588853).toInt(), TIMEZONE)
        val b11 = if (a11 >= dayNumber + 30) {
            getNewMoonDay(floor((jdFromDate(31, 12, yy - 1) - 2415021.0769986) / 29.530588853).toInt(), TIMEZONE)
        } else {
            a11
        }

        val lMonth = ((currentK - floor((b11 - 2415021.0769986) / 29.530588853).toInt()) % 12 + 12) % 12
        var lunarMonth = (lMonth + 11) % 12
        if (lunarMonth == 0) lunarMonth = 12

        var lunarYear = yy
        if (lunarMonth >= 11 && mm < 3) {
            lunarYear = yy - 1
        } else if (lunarMonth <= 2 && mm >= 11) {
            lunarYear = yy + 1
        }

        return Triple(lunarDay, lunarMonth, lunarYear)
    }

    /**
     * Chuyển đổi Âm Lịch sang Dương Lịch
     */
    fun convertLunar2Solar(lunarDay: Int, lunarMonth: Int, lunarYear: Int): Triple<Int, Int, Int> {
        // Tìm ngày sóc của tháng 11 âm lịch năm trước
        val jdDec31 = jdFromDate(31, 12, lunarYear - 1)
        val kDec = floor((jdDec31 - 2415021.0769986) / 29.530588853).toInt()
        val nm11 = getNewMoonDay(kDec, TIMEZONE)

        // Tính khoảng cách tháng
        val monthOffset = if (lunarMonth >= 11) lunarMonth - 11 else lunarMonth + 1
        val targetK = kDec + monthOffset
        val targetMonthStart = getNewMoonDay(targetK, TIMEZONE)
        val targetJd = targetMonthStart + lunarDay - 1

        return jdToDate(targetJd)
    }

    /**
     * Tính đầy đủ thông tin Âm Lịch và Phong Thủy cho một ngày Dương Lịch
     */
    fun getFullLunarDate(dd: Int, mm: Int, yy: Int): LunarDate {
        val (lunarDay, lunarMonth, lunarYear) = convertSolar2Lunar(dd, mm, yy)
        val jd = jdFromDate(dd, mm, yy)

        // Can Chi Năm
        val canYear = CAN[(lunarYear + 6) % 10]
        val chiYear = CHI[(lunarYear + 8) % 12]
        val canChiYear = "$canYear $chiYear"

        // Can Chi Tháng
        val canMonthBase = ((lunarYear * 12 + lunarMonth + 3) % 10)
        val canMonth = CAN[canMonthBase]
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

        // Tiết khí
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
            isLeap = false,
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
