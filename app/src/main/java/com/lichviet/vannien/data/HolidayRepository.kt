package com.lichviet.vannien.data

/**
 * Kho dữ liệu ngày lễ truyền thống và Văn khấn cổ truyền Việt Nam
 */
object HolidayRepository {

    data class Holiday(
        val name: String,
        val isLunar: Boolean,
        val day: Int,
        val month: Int,
        val description: String
    )

    data class VanKhan(
        val title: String,
        val occasion: String,
        val content: String
    )

    private val holidays = listOf(
        // Dương lịch
        Holiday("Tết Dương Lịch", false, 1, 1, "Chào đón năm mới dương lịch"),
        Holiday("Ngày Thầy thuốc Việt Nam", false, 27, 2, "Tôn vinh y bác sĩ"),
        Holiday("Ngày Quốc tế Phụ nữ", false, 8, 3, "Kỷ niệm ngày Quốc tế Phụ nữ"),
        Holiday("Ngày Giải phóng Miền Nam", false, 30, 4, "Ngày Giải phóng Miền Nam, Thống nhất Đất nước"),
        Holiday("Ngày Quốc tế Lao động", false, 1, 5, "Ngày Quốc tế Lao động"),
        Holiday("Ngày Quốc khánh Việt Nam", false, 2, 9, "Chủ tịch Hồ Chí Minh đọc Tuyên ngôn Độc lập"),
        Holiday("Ngày Phụ nữ Việt Nam", false, 20, 10, "Kỷ niệm thành lập Hội Phụ nữ"),
        Holiday("Ngày Nhà giáo Việt Nam", false, 20, 11, "Tri ân thầy cô giáo"),
        Holiday("Ngày Quân đội Nhân dân", false, 22, 12, "Kỷ niệm thành lập Quân đội Nhân dân Việt Nam"),

        // Âm lịch
        Holiday("Tết Nguyên Đán (Mùng 1)", true, 1, 1, "Tết Cổ Truyền - Đầu năm mới"),
        Holiday("Tết Nguyên Đán (Mùng 2)", true, 2, 1, "Tết Cổ Truyền - Mùng 2 Tết"),
        Holiday("Tết Nguyên Đán (Mùng 3)", true, 3, 1, "Tết Cổ Truyền - Mùng 3 Tết"),
        Holiday("Tết Thượng Nguyên (Rằm tháng Giêng)", true, 15, 1, "Lễ Thượng Nguyên, đi chùa cầu an"),
        Holiday("Tết Hàn Thực", true, 3, 3, "Tết Bánh trôi bánh chay"),
        Holiday("Giỗ Tổ Hùng Vương", true, 10, 3, "Tưởng nhớ công đức các Vua Hùng"),
        Holiday("Lễ Phật Đản", true, 15, 4, "Kỷ niệm Đức Phật Thích Ca đản sinh"),
        Holiday("Tết Đoan Ngọ", true, 5, 5, "Tết giết sâu bọ"),
        Holiday("Lễ Vu Lan (Rằm tháng 7)", true, 15, 7, "Mùa báo hiếu cha mẹ, xá tội vong nhân"),
        Holiday("Tết Trung Thu", true, 15, 8, "Tết trông trăng, lễ hội thiếu nhi"),
        Holiday("Tết Trùng Cửu", true, 9, 9, "Ngày tết hoa cúc cổ truyền"),
        Holiday("Tết Trùng Thập", true, 10, 10, "Tết cơm mới, tết thầy thuốc"),
        Holiday("Tết Táo Quân", true, 23, 12, "Lễ tiễn ông Công ông Táo chầu trời")
    )

    fun getHoliday(solarDay: Int, solarMonth: Int, lunarDay: Int, lunarMonth: Int): String? {
        val solarHoliday = holidays.find { !it.isLunar && it.day == solarDay && it.month == solarMonth }
        if (solarHoliday != null) return solarHoliday.name

        val lunarHoliday = holidays.find { it.isLunar && it.day == lunarDay && it.month == lunarMonth }
        if (lunarHoliday != null) return lunarHoliday.name

        if (lunarDay == 1) return "Mùng Một Đầu Tháng (Sóc)"
        if (lunarDay == 15) return "Ngày Rằm Trăng Tròn (Vọng)"

        return null
    }

    val vanKhanList = listOf(
        VanKhan(
            title = "Văn khấn ngày Mùng 1 và ngày Rằm hàng tháng",
            occasion = "Cúng gia tiên tại gia đình",
            content = """
                Nam mô A Di Đà Phật! (3 lần)
                - Con kính lạy chín phương Trời, mười phương Chư Phật, Chư phật mười phương.
                - Con kính lạy Hoàng thiên Hậu Thổ chư vị Tôn thần.
                - Con kính lạy ngài Bản cảnh Thành Hoàng, ngài Bản xứ Thổ địa, ngài Bản gia Táo quân cùng chư vị Tôn thần.
                - Con kính lạy Cao Tằng Tổ Khảo, Cao Tằng Tổ Tỷ, Thúc Bá Đệ Huynh, Cô Di, Tỷ Muội họ nội họ ngoại.

                Tín chủ (chúng) con là: ...
                Ngụ tại: ...
                Hôm nay là ngày ... tháng ... năm ... (Âm lịch).
                Tín chủ con thành tâm sắm lễ, hương hoa trà quả, thắp nén tâm hương dâng lên trước án.
                Kính mời các vị Tôn thần, chư vị Hương linh giáng lâm trước án chứng giám lòng thành, thụ hưởng lễ vật.
                Cầu xin chư vị phù hộ độ trì cho toàn thể gia đạo chúng con sức khỏe dồi dào, bình an khang thái, vạn sự hanh thông.

                Nam mô A Di Đà Phật! (3 lần)
            """.trimIndent()
        ),
        VanKhan(
            title = "Văn khấn Thần Tài - Thổ Địa",
            occasion = "Cầu tài lộc, kinh doanh buôn bán",
            content = """
                Nam mô A Di Đà Phật! (3 lần)
                - Con kính lạy chín phương Trời, mười phương Chư Phật.
                - Con kính lạy ngài Đông Trù Tư Mệnh Táo Phủ Thần Quân.
                - Con kính lạy ngài Tài Thần vị tiền, Thổ Địa vị tiền.

                Tín chủ con là: ...
                Ngụ tại địa chỉ kinh doanh: ...
                Hôm nay ngày lành tháng tốt, tín chủ con thành tâm biện dâng lễ vật hương hoa phẩm oản.
                Cúi xin Thần Tài ngài ngự giáng lâm, phù hộ độ trì cho việc buôn bán hanh thông, tài lộc vượng tiến, khách vào như nước, vạn sự như ý.

                Nam mô A Di Đà Phật! (3 lần)
            """.trimIndent()
        )
    )
}
