package com.lichviet.vannien.calendar

/**
 * Động cơ tính toán Tử Vi, Tinh Tú, Niên Hạn và Phong Thủy Bát Trạch
 */
object HoroscopeEngine {

    // 9 Sao Cửu Diệu
    data class StarInfo(
        val name: String,
        val type: String, // Cát tinh, Hung tinh, Trung tinh
        val description: String,
        val advice: String
    )

    // Bát Trạch Phong Thủy
    data class BatTrachResult(
        val cungMenh: String,
        val nguHanh: String,
        val quaiSo: Int,
        val huongTot: List<String>,
        val huongXau: List<String>
    )

    /**
     * Bảng tính Sao Chiếu Mệnh theo tuổi mụ và giới tính
     */
    fun calculateCuuDieuStar(birthYear: Int, currentYear: Int, isMale: Boolean): StarInfo {
        val age = currentYear - birthYear + 1 // Tuổi mụ
        val modAge = age % 9

        val starName = if (isMale) {
            when (modAge) {
                1 -> "La Hầu"
                2 -> "Thổ Tú"
                3 -> "Thủy Diệu"
                4 -> "Thái Bạch"
                5 -> "Thái Dương"
                6 -> "Vân Hớn"
                7 -> "Kế Đô"
                8 -> "Thái Âm"
                else -> "Mộc Đức" // 0
            }
        } else {
            when (modAge) {
                1 -> "Kế Đô"
                2 -> "Vân Hớn"
                3 -> "Mộc Đức"
                4 -> "Thái Âm"
                5 -> "Thổ Tú"
                6 -> "La Hầu"
                7 -> "Thái Dương"
                8 -> "Thái Bạch"
                else -> "Thủy Diệu" // 0
            }
        }

        return getStarDetail(starName)
    }

    private fun getStarDetail(starName: String): StarInfo = when (starName) {
        "Thái Dương" -> StarInfo(
            name = starName,
            type = "Đại Cát Tinh",
            description = "Sao Mặt Trời, vượng khí cực tốt, mang lại công danh, tài lộc dồi dào, thăng quan tiến chức.",
            advice = "Tận dụng năm này để khởi nghiệp, mở rộng kinh doanh, đầu tư lớn."
        )
        "Thái Âm" -> StarInfo(
            name = starName,
            type = "Cát Tinh",
            description = "Sao Mặt Trăng, chủ về danh lợi, hỷ sự, tiền tài mỹ mãn, cuộc sống gia đạo êm ấm.",
            advice = "Rất tốt cho việc kết hôn, mua sắm tài sản, xây dựng tổ ấm."
        )
        "Mộc Đức" -> StarInfo(
            name = starName,
            type = "Cát Tinh",
            description = "Sao phúc tinh hướng thiện, quý nhân phù trợ trong công việc, học hành đỗ đạt cao.",
            advice = "Giữ tâm thanh tịnh, làm nhiều việc thiện giúp đỡ người khác để phúc lộc dồi dào."
        )
        "Thái Bạch" -> StarInfo(
            name = starName,
            type = "Hung Tinh (Hạn tiền của)",
            description = "Thái Bạch quét sạch cửa nhà. Dễ hao tài tốn của, tiểu nhân quấy phá, cần giữ tiền của cẩn thận.",
            advice = "Hạn chế cho vay mượn mạo hiểm, dâng sao nghinh giải hạn vào rằm tháng Giêng."
        )
        "La Hầu" -> StarInfo(
            name = starName,
            type = "Hung Tinh (Khẩu thiệt)",
            description = "Dễ vướng khẩu thiệt thị phi, tranh chấp kiện tụng, sức khỏe cần chú ý các bệnh về mắt, máu huyết.",
            advice = "Cẩn trọng lời ăn tiếng nói, dĩ hòa vi quý, tránh can dự chuyện bao đồng."
        )
        "Kế Đô" -> StarInfo(
            name = starName,
            type = "Hung Tinh (U sầu ám muội)",
            description = "Tâm trạng bất an, chuyện tình cảm trắc trở, đi xa cẩn trọng tai nạn bất ngờ.",
            advice = "Giữ tâm lý vững vàng, thường xuyên đi lễ chùa cầu bình an, tích phúc đức."
        )
        "Thổ Tú" -> StarInfo(
            name = starName,
            type = "Trung Tinh",
            description = "Chủ về thử thách ban đầu, đi xa không lợi, có thể gặp trở ngại nhỏ nhưng kiên trì sẽ vượt qua.",
            advice = "Không nên khởi sự việc quá lớn, tập trung củng cố những gì đang có."
        )
        "Thủy Diệu" -> StarInfo(
            name = starName,
            type = "Cát Tinh mang chút thị phi",
            description = "Tài lộc dồi dào, may mắn trong kinh doanh, nhưng tránh vùng sông nước sâu.",
            advice = "Thận trọng khi đi đường thủy, nói năng giữ chừng mực trong các buổi tiệc tùng."
        )
        else -> StarInfo(
            name = "Vân Hớn",
            type = "Trung Tinh",
            description = "Mọi sự ở mức trung bình, phòng hỏa hoạn và thương tích nhỏ, kiện tụng nhẹ.",
            advice = "Đề phòng nóng nảy mất kiểm soát, giữ hòa khí gia đình và cơ quan."
        )
    }

    /**
     * Kiểm tra hạn Tam Tai
     */
    fun checkTamTai(birthYear: Int, currentYear: Int): Pair<Boolean, String> {
        val chiBirth = (birthYear + 8) % 12
        val chiCurrent = (currentYear + 8) % 12

        val isTamTai = when (chiBirth) {
            8, 0, 4 -> chiCurrent in listOf(2, 3, 4) // Thân Tý Thìn gặp Dần Mão Thìn
            2, 6, 10 -> chiCurrent in listOf(8, 9, 10) // Dần Ngọ Tuất gặp Thân Dậu Tuất
            5, 9, 1 -> chiCurrent in listOf(11, 0, 1) // Tỵ Dậu Sửu gặp Hợi Tý Sửu
            11, 3, 7 -> chiCurrent in listOf(5, 6, 7) // Hợi Mão Mùi gặp Tỵ Ngọ Mùi
            else -> false
        }

        val message = if (isTamTai) {
            "Năm nay bạn phạm Tam Tai. Nên cẩn trọng trong các quyết định khởi đại sự, tránh chuyển đổi lớn đột ngột."
        } else {
            "Năm nay bạn KHÔNG phạm hạn Tam Tai. Vận khí hanh thông, thuận lợi để xúc tiến kế hoạch."
        }

        return Pair(isTamTai, message)
    }

    /**
     * Kiểm tra hạn Kim Lâu
     */
    fun checkKimLau(birthYear: Int, currentYear: Int): Pair<Boolean, String> {
        val age = currentYear - birthYear + 1
        val mod = age % 9
        return when (mod) {
            1 -> Pair(true, "Phạm Kim Lâu Thân (Kỵ bản thân gia chủ)")
            3 -> Pair(true, "Phạm Kim Lâu Thê (Kỵ người vợ/người phối ngẫu)")
            6 -> Pair(true, "Phạm Kim Lâu Tử (Kỵ con cái)")
            8 -> Pair(true, "Phạm Kim Lâu Lục Súc (Kỵ gia súc, chăn nuôi, tài sản kinh tế)")
            else -> Pair(false, "Không phạm Kim Lâu, rất tốt để cưới hỏi, làm nhà")
        }
    }

    /**
     * Kiểm tra hạn Hoang Ốc (Xây dựng nhà cửa)
     */
    fun checkHoangOc(birthYear: Int, currentYear: Int): Pair<Boolean, String> {
        val age = currentYear - birthYear + 1
        val tens = age / 10
        val units = age % 10
        val hoangOcIndex = ((tens + units - 2) % 6 + 6) % 6
        val names = arrayOf(
            "Nhất Cát (Tốt: Làm nhà có chốn an cư, hanh thông)",
            "Nhì Nghi (Tốt: Làm nhà vượng phát, giàu có)",
            "Tam Địa Sát (Xấu: Gia chủ dễ sinh bệnh tật)",
            "Tứ Tấn Tài (Tốt: Phúc lộc dồi dào, tiền của vào như nước)",
            "Ngũ Thọ Tử (Xấu: Trong nhà dễ sinh ly biệt)",
            "Lục Hoang Ốc (Xấu: Khó thành đạt, gia đạo bất hòa)"
        )
        val isGood = hoangOcIndex in listOf(0, 1, 3)
        return Pair(isGood, names[hoangOcIndex])
    }

    /**
     * Tính Cung Mệnh Bát Trạch theo năm sinh & giới tính
     */
    fun getBatTrach(birthYear: Int, isMale: Boolean): BatTrachResult {
        var sum = birthYear.toString().map { it.toString().toInt() }.sum()
        while (sum > 9) {
            sum = sum.toString().map { it.toString().toInt() }.sum()
        }

        val quaiSo = if (isMale) {
            (11 - sum) % 9.let { if (it == 0) 9 else it }
        } else {
            (sum + 4) % 9.let { if (it == 0) 9 else it }
        }

        return when (quaiSo) {
            1 -> BatTrachResult(
                "Khảm (Thủy)", "Thủy", quaiSo,
                listOf("Đông Nam (Sinh Khí)", "Đông (Thiên Y)", "Nam (Diên Niên)", "Bắc (Phục Vị)"),
                listOf("Tây Nam (Tuyệt Mệnh)", "Đông Bắc (Ngũ Quỷ)", "Tây Bắc (Lục Sát)", "Tây (Họa Hại)")
            )
            2 -> BatTrachResult(
                "Khôn (Thổ)", "Thổ", quaiSo,
                listOf("Đông Bắc (Sinh Khí)", "Tây (Thiên Y)", "Tây Bắc (Diên Niên)", "Tây Nam (Phục Vị)"),
                listOf("Bắc (Tuyệt Mệnh)", "Đông Nam (Ngũ Quỷ)", "Nam (Lục Sát)", "Đông (Họa Hại)")
            )
            3 -> BatTrachResult(
                "Chấn (Mộc)", "Mộc", quaiSo,
                listOf("Nam (Sinh Khí)", "Bắc (Thiên Y)", "Đông Nam (Diên Niên)", "Đông (Phục Vị)"),
                listOf("Tây (Tuyệt Mệnh)", "Tây Bắc (Ngũ Quỷ)", "Đông Bắc (Lục Sát)", "Tây Nam (Họa Hại)")
            )
            4 -> BatTrachResult(
                "Tốn (Mộc)", "Mộc", quaiSo,
                listOf("Bắc (Sinh Khí)", "Nam (Thiên Y)", "Đông (Diên Niên)", "Đông Nam (Phục Vị)"),
                listOf("Đông Bắc (Tuyệt Mệnh)", "Tây Nam (Ngũ Quỷ)", "Tây (Lục Sát)", "Tây Bắc (Họa Hại)")
            )
            6 -> BatTrachResult(
                "Càn (Kim)", "Kim", quaiSo,
                listOf("Tây (Sinh Khí)", "Đông Bắc (Thiên Y)", "Tây Nam (Diên Niên)", "Tây Bắc (Phục Vị)"),
                listOf("Nam (Tuyệt Mệnh)", "Đông (Ngũ Quỷ)", "Bắc (Lục Sát)", "Đông Nam (Họa Hại)")
            )
            7 -> BatTrachResult(
                "Đoài (Kim)", "Kim", quaiSo,
                listOf("Tây Bắc (Sinh Khí)", "Tây Nam (Thiên Y)", "Đông Bắc (Diên Niên)", "Tây (Phục Vị)"),
                listOf("Đông (Tuyệt Mệnh)", "Nam (Ngũ Quỷ)", "Đông Nam (Lục Sát)", "Bắc (Họa Hại)")
            )
            8 -> BatTrachResult(
                "Cấn (Thổ)", "Thổ", quaiSo,
                listOf("Tây Nam (Sinh Khí)", "Tây Bắc (Thiên Y)", "Tây (Diên Niên)", "Đông Bắc (Phục Vị)"),
                listOf("Đông Nam (Tuyệt Mệnh)", "Bắc (Ngũ Quỷ)", "Đông (Lục Sát)", "Nam (Họa Hại)")
            )
            else -> BatTrachResult(
                "Ly (Hỏa)", "Hỏa", quaiSo,
                listOf("Đông (Sinh Khí)", "Đông Nam (Thiên Y)", "Bắc (Diên Niên)", "Nam (Phục Vị)"),
                listOf("Tây Bắc (Tuyệt Mệnh)", "Tây (Ngũ Quỷ)", "Tây Nam (Lục Sát)", "Đông Bắc (Họa Hại)")
            )
        }
    }

    /**
     * Gieo quẻ Kinh Dịch
     */
    fun gieoQueKinhDich(): Pair<String, String> {
        val queList = listOf(
            Pair("Quẻ Càn (Thuần Càn)", "Đại cát đại lợi, vạn sự hanh thông, chính khí ngút trời."),
            Pair("Quẻ Khôn (Thuần Khôn)", "Hòa thuận sinh tài, thuận theo thiên thời, bền bỉ có thành công lớn."),
            Pair("Quẻ Thái (Địa Thiên Thái)", "Vạn vật sinh sôi, khó khăn tiêu tan, thời cơ vàng để phát triển."),
            Pair("Quẻ Đồng Nhân (Thiên Hỏa Đồng Nhân)", "Đoàn kết hợp tác, bạn hữu tương trợ, đồng lòng gặt hái thành quả lớn."),
            Pair("Quẻ Đại Hữu (Hỏa Thiên Đại Hữu)", "Tài lộc bội thu, danh vọng rạng rỡ, quang minh chính đại.")
        )
        return queList.random()
    }
}
