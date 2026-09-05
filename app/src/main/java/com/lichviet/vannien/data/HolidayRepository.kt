package com.lichviet.vannien.data

import com.lichviet.vannien.calendar.LunarCalendarEngine
import java.util.Calendar

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

    val holidays = listOf(
        // Dương lịch
        Holiday("Tết Dương Lịch", false, 1, 1, "Chào đón ngày đầu tiên của năm mới dương lịch"),
        Holiday("Ngày Thầy thuốc Việt Nam", false, 27, 2, "Tôn vinh và tri ân các y bác sĩ, cán bộ ngành y"),
        Holiday("Ngày Quốc tế Phụ nữ", false, 8, 3, "Kỷ niệm và tôn vinh phụ nữ trên toàn thế giới"),
        Holiday("Ngày Giải phóng Miền Nam", false, 30, 4, "Ngày Giải phóng Miền Nam, Thống nhất Đất nước (1975)"),
        Holiday("Ngày Quốc tế Lao động", false, 1, 5, "Ngày hội của giai cấp công nhân và người lao động"),
        Holiday("Ngày Quốc khánh Việt Nam", false, 2, 9, "Chủ tịch Hồ Chí Minh đọc Tuyên ngôn Độc lập tại Ba Đình (1945)"),
        Holiday("Ngày Phụ nữ Việt Nam", false, 20, 10, "Kỷ niệm ngày thành lập Hội Liên hiệp Phụ nữ Việt Nam (1930)"),
        Holiday("Ngày Nhà giáo Việt Nam", false, 20, 11, "Ngày tri ân công đức của các thế hệ thầy cô giáo"),
        Holiday("Ngày Quân đội Nhân dân", false, 22, 12, "Kỷ niệm thành lập Quân đội Nhân dân Việt Nam (1944)"),

        // Âm lịch
        Holiday("Tết Nguyên Đán (Mùng 1)", true, 1, 1, "Tết Cổ Truyền - Đầu năm mới, sum họp gia đình, chúc tết tổ tiên"),
        Holiday("Tết Nguyên Đán (Mùng 2)", true, 2, 1, "Tết Cổ Truyền - Chúc tết họ hàng nội ngoại"),
        Holiday("Tết Nguyên Đán (Mùng 3)", true, 3, 1, "Tết Cổ Truyền - Mùng một tết cha, mùng hai tết mẹ, mùng ba tết thầy"),
        Holiday("Tết Thượng Nguyên (Rằm tháng Giêng)", true, 15, 1, "Lễ Thượng Nguyên, cả năm được rằm tháng Giêng, đi chùa cầu an"),
        Holiday("Tết Hàn Thực", true, 3, 3, "Tết Bánh trôi bánh chay, nhớ về nguồn cội tổ tiên"),
        Holiday("Giỗ Tổ Hùng Vương", true, 10, 3, "Quốc giỗ tưởng nhớ công đức dựng nước của các Vua Hùng"),
        Holiday("Lễ Phật Đản", true, 15, 4, "Kỷ niệm ngày Đức Phật Thích Ca Mâu Ni đản sinh"),
        Holiday("Tết Đoan Ngọ", true, 5, 5, "Tết giết sâu bọ, ăn bánh tro, hoa quả đầu mùa cầu sức khỏe"),
        Holiday("Lễ Vu Lan (Rằm tháng Bảy)", true, 15, 7, "Mùa báo hiếu công ơn cha mẹ, xá tội vong nhân, tích đức làm thiện"),
        Holiday("Tết Trung Thu", true, 15, 8, "Tết trông trăng, lễ hội phá cỗ, rước đèn sum vầy của thiếu nhi"),
        Holiday("Tết Trùng Cửu", true, 9, 9, "Ngày tết hoa cúc cổ truyền, đăng cao thưởng ngoạn mùa thu"),
        Holiday("Tết Trùng Thập", true, 10, 10, "Tết cơm mới mừng vụ mùa bội thu, tết của các thầy thuốc"),
        Holiday("Tết Táo Quân", true, 23, 12, "Lễ tiễn ông Công ông Táo chầu trời báo cáo việc trần gian"),
        Holiday("Tất Niên Đêm 30 Tết", true, 30, 12, "Lễ trừ tịch cúng tất niên, tiễn năm cũ, nghinh đón tân xuân")
    )

    fun getHoliday(solarDay: Int, solarMonth: Int, lunarDay: Int, lunarMonth: Int, solarYear: Int = 0): String? {
        val matched = mutableListOf<String>()

        holidays.filter { !it.isLunar && it.day == solarDay && it.month == solarMonth }
            .forEach { matched.add(it.name) }

        holidays.filter { it.isLunar && it.day == lunarDay && it.month == lunarMonth && it.day != 30 }
            .forEach { matched.add(it.name) }

        // Kiểm tra Tất Niên (ngày cuối cùng của năm âm lịch: 30 Tết hoặc 29 Tết nếu tháng Chạp thiếu)
        if (lunarMonth == 12) {
            if (lunarDay == 30) {
                matched.add("Tất Niên Đêm 30 Tết")
            } else if (lunarDay == 29 && solarYear > 0) {
                val nextCal = Calendar.getInstance().apply {
                    set(solarYear, solarMonth - 1, solarDay)
                    add(Calendar.DAY_OF_MONTH, 1)
                }
                val tomorrowLunar = LunarCalendarEngine.convertSolar2Lunar(
                    nextCal.get(Calendar.DAY_OF_MONTH),
                    nextCal.get(Calendar.MONTH) + 1,
                    nextCal.get(Calendar.YEAR)
                )
                if (tomorrowLunar.day == 1 && tomorrowLunar.month == 1) {
                    matched.add("Tất Niên (Giao Thừa 29 Tết)")
                }
            }
        }

        if (matched.isNotEmpty()) {
            return matched.joinToString(" • ")
        }

        if (lunarDay == 1) return "Mùng Một Đầu Tháng (Sóc)"
        if (lunarDay == 15) return "Ngày Rằm Trăng Tròn (Vọng)"

        return null
    }

    val vanKhanList = listOf(
        VanKhan(
            title = "Văn khấn ngày Mùng 1 và ngày Rằm hàng tháng",
            occasion = "Kính cúng Thần linh, Thổ địa và Gia tiên tại gia đình",
            content = """
                Nam mô A Di Đà Phật! (3 lần, 3 lạy)

                - Con kính lạy chín phương Trời, mười phương Chư Phật, Chư phật mười phương.
                - Con kính lạy Hoàng thiên Hậu Thổ chư vị Tôn thần.
                - Con kính lạy ngài Đông Trù Tư Mệnh Táo Phủ Thần Quân.
                - Con kính lạy ngài Bản cảnh Thành Hoàng, ngài Bản xứ Thổ địa, ngài Bản gia Ngũ phương Long mạch Tài thần.
                - Con kính lạy các cụ Cao Tằng Tổ Khảo, Cao Tằng Tổ Tỷ, Thúc Bá Đệ Huynh, Cô Di Tỷ Muội họ nội họ ngoại.

                Tín chủ (chúng) con là: ...
                Ngụ tại địa chỉ: ...

                Hôm nay là ngày ... tháng ... năm ... (Âm lịch).
                Tín chủ con thành tâm sắm biện hương hoa, trà quả, trầu cau, thắp nén tâm hương dâng lên trước án.
                Chúng con kính mời chư vị Tôn thần cai quản trong khu vực này, cùng chư vị Hương linh gia tiên nội ngoại tiền tổ giáng lâm trước án, chứng giám lòng thành, thụ hưởng lễ vật.

                Cầu xin chư vị phù hộ độ trì cho gia đình chúng con: toàn gia an lạc, già trẻ bình an, tứ thời không hạn ách hung tai, tám tiết hưởng phúc lành thịnh vượng, công việc hanh thông, sở cầu như ý, sở nguyện tòng tâm.

                Giải tấm lòng thành, cúi xin chứng giám.
                Nam mô A Di Đà Phật! (3 lần, 3 lạy)
            """.trimIndent()
        ),
        VanKhan(
            title = "Văn khấn Thần Tài - Thổ Địa",
            occasion = "Cầu buôn may bán đắt, tài lộc hanh thông cho cửa hàng, công ty",
            content = """
                Nam mô A Di Đà Phật! (3 lần, 3 lạy)

                - Con kính lạy chín phương Trời, mười phương Chư Phật, Chư phật mười phương.
                - Con kính lạy ngài Hoàng Thiên Hậu Thổ chư vị Tôn thần.
                - Con kính lạy ngài Đông Trù Tư Mệnh Táo Phủ Thần Quân.
                - Con kính lạy ngài Tài Thần vị tiền, Triệu Công Minh Đại Tôn Thần.
                - Con kính lạy ngài Thổ Địa, Thần Tài cùng các ngài Thần linh cai quản khu vực này.

                Tín chủ con là: ...
                Ngụ tại / Cơ sở kinh doanh tại: ...

                Hôm nay ngày lành tháng tốt, tín chủ con thành tâm sắm sửa hương hoa trà quả, kim ngân lễ mạo, dâng lên trước án Thần Tài - Thổ Địa.
                Kính xin các ngài giáng lâm án tọa, thụ hưởng lễ vật, soi xét lòng thành.

                Cúi xin Thần Tài ngài phù hộ độ trì cho chúng con:
                Kinh doanh thuận buồm xuôi gió, khách hàng nườm nượp, tiền tài phát đạt, buôn may bán đắt, lộc tài tăng tiến, giải trừ vận hạn, vạn sự hanh thông như ý.

                Chúng con người trần mắt thịt, nếu có điều chi thiếu sót kính xin lượng thứ.
                Nam mô A Di Đà Phật! (3 lần, 3 lạy)
            """.trimIndent()
        ),
        VanKhan(
            title = "Văn khấn Giao Thừa (Lễ Trừ Tịch đêm 30 Tết)",
            occasion = "Nghinh đón tân niên, tống cựu nghênh tân ngoài trời và trong nhà",
            content = """
                Nam mô A Di Đà Phật! (3 lần, 3 lạy)

                - Con kính lạy chín phương Trời, mười phương Chư Phật, Chư phật mười phương.
                - Con kính lạy Đức Đương Niên Hành Khiển, Chí Đức Tôn Thần.
                - Con kính lạy ngài Bản cảnh Thành Hoàng chư vị Đại Vương.
                - Con kính lạy ngài Đông Trù Tư Mệnh Táo Phủ Thần Quân.
                - Con kính lạy ngài Bản gia Ngũ Phương Ngũ Thổ, Long Mạch Tài Thần.
                - Con kính lạy các cụ Tổ Tiên nội ngoại, chư vị Hương linh dòng họ ...

                Giờ phút Giao Thừa thiêng liêng năm cũ vừa qua, năm mới vừa tới.
                Tín chủ con là: ... cùng toàn thể gia quyến.
                Ngụ tại: ...

                Nhân khắc Giao Thừa Lễ Trừ Tịch, tống cựu nghênh tân, toàn gia chúng con thành tâm sắm lễ hương hoa, phù tửu trà quả dâng lên trước án.
                Kính cẩn thỉnh mời Quan Đương Niên tân quan hạ trần nhậm vụ, quan cựu thoái quy, cùng chư vị Tôn thần bản xứ và Tổ tiên chứng giám lòng thành thụ hưởng lễ vật.

                Cúi xin các ngài ban phúc giáng tường, phù hộ cho toàn gia một năm mới:
                Xuân đa kiết khánh, hạ bảo bình an, thu thính tam đa, đông nghinh bách phúc. Sức khỏe dồi dào, gia đạo hưng long, sở cầu tất ứng, sở nguyện tòng tâm.

                Nam mô A Di Đà Phật! (3 lần, 3 lạy)
            """.trimIndent()
        ),
        VanKhan(
            title = "Văn khấn Mùng 1 Tết Nguyên Đán",
            occasion = "Lễ tạ tổ tiên đầu năm mới - Buổi sáng sớm Mùng 1 Tết",
            content = """
                Nam mô A Di Đà Phật! (3 lần, 3 lạy)

                - Con kính lạy chín phương Trời, mười phương Chư Phật.
                - Con kính lạy Hoàng thiên Hậu Thổ chư vị Tôn thần.
                - Con kính lạy ngài Kim Niên Đương Cai Thái Tuế Chí Đức Tôn Thần.
                - Con kính lạy các cụ Cao Tằng Tổ Khảo, Cao Tằng Tổ Tỷ, bá thúc huynh đệ họ ...

                Hôm nay là ngày Mùng Một tháng Giêng, tiết xuân đầu năm mới Nguyên Đán.
                Tín chủ con là: ...
                Cùng toàn gia đình cư ngụ tại: ...

                Đầu năm mới, tín chủ con thành kính sửa soạn cỗ bàn thanh tịnh, hương hoa trà quả, kính dâng trước linh tọa gia tiên.
                Kính lạy chư vị Thần linh bản xứ, cùng các bậc Tiền nhân tổ tiên họ tộc giáng phó từ đường, thụ hưởng lễ bạc tâm thành.

                Cúi xin Tổ tiên phù hộ độ trì cho con cháu trong năm mới:
                Học hành tấn tới, công tác vẻ vang, buôn bán phát tài, gia đạo thuận hòa, trong ấm ngoài êm, bình an may mắn suốt 12 tháng bốn mùa.

                Nam mô A Di Đà Phật! (3 lần, 3 lạy)
            """.trimIndent()
        ),
        VanKhan(
            title = "Văn khấn Khai Trương (Cửa hàng, Công ty)",
            occasion = "Mở cửa hàng buôn bán đầu xuân hoặc ngày khai trương kinh doanh",
            content = """
                Nam mô A Di Đà Phật! (3 lần, 3 lạy)

                - Con kính lạy chín phương Trời, mười phương Chư Phật, Chư phật mười phương.
                - Con kính lạy Quan Đương Niên Hành Khiển chư vị Tôn thần.
                - Con kính lạy ngài Bản Cảnh Thành Hoàng chư vị Đại Vương.
                - Con kính lạy ngài Thổ Địa, Thần Tài vị tiền.
                - Con kính lạy chư vị Thần linh, Tiền chủ Hậu chủ ngụ tại đất này.

                Tín chủ con là: ...
                Hôm nay ngày ... tháng ... năm ... (Âm lịch), giờ hoàng đạo cát khánh.
                Tín chủ con mở tiệm kinh doanh / khai trương văn phòng tại địa chỉ: ...

                Tín chủ con lòng thành sắm biện lễ vật hương hoa trà quả, trầu cau kim ngân kính dâng trước án.
                Kính xin chư vị Tôn thần bản thổ, Tài Thần, Thổ Địa quang lâm chứng giám, thụ hưởng lễ vật.

                Cúi xin các ngài phù hộ độ trì cho cửa hàng chúng con buôn may bán đắt, khách vào tấp nập, tài lộc dồi dào, thuận buồm xuôi gió, vạn sự hanh thông, trăm điều đại cát.

                Nam mô A Di Đà Phật! (3 lần, 3 lạy)
            """.trimIndent()
        ),
        VanKhan(
            title = "Văn khấn Động Thổ (Khởi công làm nhà, sửa chữa)",
            occasion = "Xin phép Thần linh, Thổ địa trước khi cuốc móng, đào móng, xây dựng",
            content = """
                Nam mô A Di Đà Phật! (3 lần, 3 lạy)

                - Con kính lạy chín phương Trời, mười phương Chư Phật.
                - Con kính lạy Hoàng Thiên Hậu Thổ chư vị Tôn thần.
                - Con kính lạy ngài Bản cảnh Thành Hoàng chư vị Đại Vương.
                - Con kính lạy ngài Định Phúc Táo Quân, ngài Bản Gia Thổ Địa Long Mạch Tôn Thần.
                - Con kính lạy các vị Thần linh cai quản trong khu vực này.

                Tín chủ con là: ...
                Ngụ tại: ...
                Hôm nay ngày ... tháng ... năm ... (Âm lịch), giờ lành tháng tốt.

                Tín chủ con khởi công động thổ xây cất / tu sửa ngôi nhà tại mảnh đất: ...
                Chúng con sắm sửa lễ vật hương hoa phù tửu, lòng thành dâng cúng.
                Kính cẩn thỉnh mời chư vị Tôn thần giáng lâm trước án thụ hưởng lễ vật, chứng giám tấc lòng.

                Cúi xin chư vị Thần linh phù trợ cho việc thi công được hanh thông thuận lợi:
                Thợ thuyền bình an, không gặp trắc trở hung tai, công trình vững chãi bền lâu, gia chủ hưng vượng, con cháu đời đời hưởng phúc lành.

                Nam mô A Di Đà Phật! (3 lần, 3 lạy)
            """.trimIndent()
        ),
        VanKhan(
            title = "Văn khấn Nhập Trạch (Lễ vào nhà mới)",
            occasion = "Kính cúng Táo Quân, Thần Linh và Gia Tiên khi dọn vào nhà mới",
            content = """
                Nam mô A Di Đà Phật! (3 lần, 3 lạy)

                - Con kính lạy chín phương Trời, mười phương Chư Phật.
                - Con kính lạy Hoàng Thiên Hậu Thổ chư vị Tôn thần.
                - Con kính lạy ngài Đông Trù Tư Mệnh Táo Phủ Thần Quân.
                - Con kính lạy ngài Bản Xứ Thổ Địa, Long Mạch Tài Thần.
                - Con kính lạy tổ tiên dòng họ ... cùng chư vị Hương linh tiền tổ.

                Tín chủ con là: ...
                Hôm nay ngày lành tháng tốt, ngày ... tháng ... năm ... (Âm lịch).
                Tín chủ chúng con thành tâm dọn về nơi cư ngụ mới tại địa chỉ: ...

                Chúng con thành tâm sắm sửa lễ vật hương hoa trà quả, thắp nén tâm hương dâng trước linh đài.
                Kính cẩn xin phép chư vị Thần linh bản thổ cho phép gia đình chúng con được nhập trạch cư trú nơi đây.
                Kính thỉnh Gia tiên nội ngoại cùng về an vị hương hỏa tại ngôi nhà mới.

                Cúi xin chư vị phù trì cho gia đạo:
                Người yên vật thịnh, an cư lạc nghiệp, sức khỏe trường thọ, tài lộc dồi dào, vạn sự hanh thông như ý cát tường.

                Nam mô A Di Đà Phật! (3 lần, 3 lạy)
            """.trimIndent()
        ),
        VanKhan(
            title = "Văn khấn Táo Quân (23 tháng Chạp tiễn Táo về trời)",
            occasion = "Lễ tiễn Tôn Thần Táo Quân chầu trời báo cáo ngọc hoàng ngày 23 tháng Chạp",
            content = """
                Nam mô A Di Đà Phật! (3 lần, 3 lạy)

                - Con kính lạy chín phương Trời, mười phương Chư Phật.
                - Con kính lạy ngài Đông Trù Tư Mệnh Định Phúc Táo Phủ Thần Quân.
                - Con kính lạy ngài Thổ Công, Thổ Địa, Long Mạch chư vị Tôn thần.

                Tín chủ con là: ...
                Ngụ tại: ...
                Hôm nay là ngày 23 tháng Chạp năm ..., tín chủ con thành tâm sắm lễ cá chép, mũ mão hoa quả, trầu cau hương trầm dâng lên trước án.

                Tín chủ con kính cẩn tiễn đưa ngài Định Phúc Táo Phủ Thần Quân cưỡi cá chép chầu trời, bẩm báo với Ngọc Hoàng Thượng Đế những việc thiện ác của toàn gia trong năm vừa qua.

                Cúi xin Táo Ngài giáng phúc lưu ân, tấu xin Ngọc Hoàng ban cho toàn gia chúng con một năm mới:
                Gia đạo thái hòa, sức khỏe an khang, tài lộc vượng tiến, trăm sự tốt lành, giảm trừ tai ương họa ách.

                Chúng con lễ bạc tâm thành, cúi xin chứng giám.
                Nam mô A Di Đà Phật! (3 lần, 3 lạy)
            """.trimIndent()
        ),
        VanKhan(
            title = "Văn khấn Lễ Vu Lan (Rằm tháng Bảy)",
            occasion = "Mùa Vu Lan Báo Hiếu công đức cha mẹ tổ tiên và Lễ xá tội vong nhân",
            content = """
                Nam mô Bổn Sư Thích Ca Mâu Ni Phật! (3 lần)
                Nam mô Đại Hiếu Mục Kiền Liên Bồ Tát! (3 lần)

                - Con kính lạy mười phương Chư Phật, Chư đại Bồ Tát.
                - Con kính lạy Hoàng Thiên Hậu Thổ, chư vị Tôn Thần bản xứ cai quản.
                - Con kính lạy Tổ Tiên nội ngoại họ ... và chư vị Tiền nhân.

                Tín chủ con là: ...
                Ngụ tại: ...
                Hôm nay nhân tiết Vu Lan Báo Hiếu, ngày Rằm tháng Bảy âm lịch.
                Tín chủ chúng con thành tâm sắm sanh hoa quả lễ vật, trai đàn thanh tịnh dâng lên trước án.

                Chúng con tưởng nhớ công đức cù lao chín chữ, sinh thành dưỡng dục của cha mẹ, ông bà, tổ tiên nhiều đời nhiều kiếp.
                Cúi xin Tam Bảo từ bi, chư vị Tôn thần gia hộ cho:
                - Cha mẹ hiện tiền: Phước thọ tăng long, thân tâm an lạc, tiêu trừ nghiệp chướng.
                - Tổ tiên, cha mẹ quá vãng: Sớm siêu sinh tịnh độ, thoát khỏi u đồ, quy về cõi Phật thanh tịnh an vui.
                Đồng thời nguyện cầu cho muôn loài chúng sinh đều được nương nhờ Phật pháp, ấm no hòa bình.

                Nam mô A Di Đà Phật! (3 lần, 3 lạy)
            """.trimIndent()
        )
    )
}
