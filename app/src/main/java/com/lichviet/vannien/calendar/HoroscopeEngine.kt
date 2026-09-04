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

        val rawQuaiSo = if (isMale) {
            (11 - sum) % 9.let { if (it == 0) 9 else it }
        } else {
            (sum + 4) % 9.let { if (it == 0) 9 else it }
        }

        // Quy tắc phong thủy: "Ngũ vi trung cung"
        // Nam sinh quái số 5 quy về Khôn (2 - Thổ)
        // Nữ sinh quái số 5 quy về Cấn (8 - Thổ)
        val quaiSo = if (rawQuaiSo == 5) {
            if (isMale) 2 else 8
        } else {
            rawQuaiSo
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
     * Tính Ngũ Hành Nạp Âm của 60 Hoa Giáp
     */
    fun getNapAm(birthYear: Int): Pair<String, String> {
        val canIdx = (birthYear + 6) % 10
        val chiIdx = (birthYear + 8) % 12
        val can = LunarCalendarEngine.CAN[canIdx]
        val chi = LunarCalendarEngine.CHI[chiIdx]
        val canChi = "$can $chi"

        val canVal = (canIdx / 2) + 1
        val chiVal = when (chiIdx % 6) {
            0, 1 -> 0
            2, 3 -> 1
            else -> 2
        }
        var sum = canVal + chiVal
        if (sum > 5) sum -= 5
        val hanh = when (sum) {
            1 -> "Kim"
            2 -> "Thủy"
            3 -> "Hỏa"
            4 -> "Thổ"
            else -> "Mộc"
        }

        val napAmMap = mapOf(
            "Giáp Tý" to "Hải Trung Kim", "Ất Sửu" to "Hải Trung Kim",
            "Bính Dần" to "Lư Trung Hỏa", "Đinh Mão" to "Lư Trung Hỏa",
            "Mậu Thìn" to "Đại Lâm Mộc", "Kỷ Tỵ" to "Đại Lâm Mộc",
            "Canh Ngọ" to "Lộ Bàng Thổ", "Tân Mùi" to "Lộ Bàng Thổ",
            "Nhâm Thân" to "Kiếm Phong Kim", "Quý Dậu" to "Kiếm Phong Kim",
            "Giáp Tuất" to "Sơn Đầu Hỏa", "Ất Hợi" to "Sơn Đầu Hỏa",
            "Bính Tý" to "Giản Hạ Thủy", "Đinh Sửu" to "Giản Hạ Thủy",
            "Mậu Dần" to "Thành Đầu Thổ", "Kỷ Mão" to "Thành Đầu Thổ",
            "Canh Thìn" to "Bạch Lạp Kim", "Tân Tỵ" to "Bạch Lạp Kim",
            "Nhâm Ngọ" to "Dương Liễu Mộc", "Quý Mùi" to "Dương Liễu Mộc",
            "Giáp Thân" to "Tuyền Trung Thủy", "Ất Dậu" to "Tuyền Trung Thủy",
            "Bính Tuất" to "Ốc Thượng Thổ", "Đinh Hợi" to "Ốc Thượng Thổ",
            "Mậu Tý" to "Tích Lịch Hỏa", "Kỷ Sửu" to "Tích Lịch Hỏa",
            "Canh Dần" to "Tùng Bách Mộc", "Tân Mão" to "Tùng Bách Mộc",
            "Nhâm Thìn" to "Trường Lưu Thủy", "Quý Tỵ" to "Trường Lưu Thủy",
            "Giáp Ngọ" to "Sa Trung Kim", "Ất Mùi" to "Sa Trung Kim",
            "Bính Thân" to "Sơn Hạ Hỏa", "Đinh Dậu" to "Sơn Hạ Hỏa",
            "Mậu Tuất" to "Bình Địa Mộc", "Kỷ Hợi" to "Bình Địa Mộc",
            "Canh Tý" to "Bích Thượng Thổ", "Tân Sửu" to "Bích Thượng Thổ",
            "Nhâm Dần" to "Kim Bạch Kim", "Quý Mão" to "Kim Bạch Kim",
            "Giáp Thìn" to "Phúc Đăng Hỏa", "Ất Tỵ" to "Phúc Đăng Hỏa",
            "Bính Ngọ" to "Thiên Hà Thủy", "Đinh Mùi" to "Thiên Hà Thủy",
            "Mậu Thân" to "Đại Trạch Thổ", "Kỷ Dậu" to "Đại Trạch Thổ",
            "Canh Tuất" to "Thoa Xuyến Kim", "Tân Hợi" to "Thoa Xuyến Kim",
            "Nhâm Tý" to "Tang Đố Mộc", "Quý Sửu" to "Tang Đố Mộc",
            "Giáp Dần" to "Đại Khê Thủy", "Ất Mão" to "Đại Khê Thủy",
            "Bính Thìn" to "Sa Trung Thổ", "Đinh Tỵ" to "Sa Trung Thổ",
            "Mậu Ngọ" to "Thiên Thượng Hỏa", "Kỷ Mùi" to "Thiên Thượng Hỏa",
            "Canh Thân" to "Thạch Lựu Mộc", "Tân Dậu" to "Thạch Lựu Mộc",
            "Nhâm Tuất" to "Đại Hải Thủy", "Quý Hợi" to "Đại Hải Thủy"
        )
        val name = napAmMap[canChi] ?: "$hanh Trung"
        return Pair(name, hanh)
    }

    data class TuoiHopResult(
        val conGiap: String,
        val canChi: String,
        val napAm: String,
        val hanh: String,
        val tamHop: String,
        val lucHop: String,
        val tuHanhXung: String,
        val lucXung: String,
        val tuoiKetHon: List<String>,
        val tuoiLamAn: List<String>,
        val tuoiXongDat: List<String>
    )

    fun getTuoiHop(birthYear: Int): TuoiHopResult {
        val canIdx = (birthYear + 6) % 10
        val chiIdx = (birthYear + 8) % 12
        val conGiap = LunarCalendarEngine.CON_GIAP[chiIdx]
        val canChi = "${LunarCalendarEngine.CAN[canIdx]} ${LunarCalendarEngine.CHI[chiIdx]}"
        val (napAm, hanh) = getNapAm(birthYear)

        val tamHop = when (chiIdx) {
            0, 4, 8 -> "Thân - Tý - Thìn (Thủy cục hanh thông)"
            1, 5, 9 -> "Tỵ - Dậu - Sửu (Kim cục phát tài)"
            2, 6, 10 -> "Dần - Ngọ - Tuất (Hỏa cục rực rỡ)"
            else -> "Hợi - Mão - Mùi (Mộc cục bình an)"
        }

        val lucHop = when (chiIdx) {
            0 -> "Sửu (Tý - Sửu nhị hợp, quý nhân trợ mệnh)"
            1 -> "Tý (Sửu - Tý nhị hợp, gia đạo an yên)"
            2 -> "Hợi (Dần - Hợi nhị hợp, công danh rộng mở)"
            3 -> "Tuất (Mão - Tuất nhị hợp, tài lộc dồi dào)"
            4 -> "Dậu (Thìn - Dậu nhị hợp, vinh hiển sum vầy)"
            5 -> "Thân (Tỵ - Thân nhị hợp, sự nghiệp thăng hoa)"
            6 -> "Mùi (Ngọ - Mùi nhị hợp, nhật nguyệt quang minh)"
            7 -> "Ngọ (Mùi - Ngọ nhị hợp, phồn vinh thịnh vượng)"
            8 -> "Tỵ (Thân - Tỵ nhị hợp, vững như bàn thạch)"
            9 -> "Thìn (Dậu - Thìn nhị hợp, uy đức song toàn)"
            10 -> "Mão (Tuất - Mão nhị hợp, hòa khí sinh tài)"
            else -> "Dần (Hợi - Dần nhị hợp, phúc lộc vô biên)"
        }

        val tuHanhXung = when (chiIdx) {
            0, 6, 3, 9 -> "Tý - Ngọ - Mão - Dậu (Cần giữ hòa khí, tránh nóng nảy)"
            2, 8, 5, 11 -> "Dần - Thân - Tỵ - Hợi (Cần kiên nhẫn, dĩ hòa vi quý)"
            else -> "Thìn - Tuất - Sửu - Mùi (Nên nhường nhịn, cẩn trọng khi cộng tác)"
        }

        val lucXung = when (chiIdx) {
            0 -> "Ngọ (Tý - Ngọ chính xung)"
            1 -> "Mùi (Sửu - Mùi chính xung)"
            2 -> "Thân (Dần - Thân chính xung)"
            3 -> "Dậu (Mão - Dậu chính xung)"
            4 -> "Tuất (Thìn - Tuất chính xung)"
            5 -> "Hợi (Tỵ - Hợi chính xung)"
            6 -> "Tý (Ngọ - Tý chính xung)"
            7 -> "Sửu (Mùi - Sửu chính xung)"
            8 -> "Dần (Thân - Dần chính xung)"
            9 -> "Mão (Dậu - Mão chính xung)"
            10 -> "Thìn (Tuất - Thìn chính xung)"
            else -> "Tỵ (Hợi - Tỵ chính xung)"
        }

        val tuoiKetHon = when (chiIdx) {
            0 -> listOf("Sửu (Lục Hợp)", "Thân (Tam Hợp)", "Thìn (Tam Hợp)")
            1 -> listOf("Tý (Lục Hợp)", "Tỵ (Tam Hợp)", "Dậu (Tam Hợp)")
            2 -> listOf("Hợi (Lục Hợp)", "Ngọ (Tam Hợp)", "Tuất (Tam Hợp)")
            3 -> listOf("Tuất (Lục Hợp)", "Hợi (Tam Hợp)", "Mùi (Tam Hợp)")
            4 -> listOf("Dậu (Lục Hợp)", "Tý (Tam Hợp)", "Thân (Tam Hợp)")
            5 -> listOf("Thân (Lục Hợp)", "Sửu (Tam Hợp)", "Dậu (Tam Hợp)")
            6 -> listOf("Mùi (Lục Hợp)", "Dần (Tam Hợp)", "Tuất (Tam Hợp)")
            7 -> listOf("Ngọ (Lục Hợp)", "Hợi (Tam Hợp)", "Mão (Tam Hợp)")
            8 -> listOf("Tỵ (Lục Hợp)", "Tý (Tam Hợp)", "Thìn (Tam Hợp)")
            9 -> listOf("Thìn (Lục Hợp)", "Sửu (Tam Hợp)", "Tỵ (Tam Hợp)")
            10 -> listOf("Mão (Lục Hợp)", "Dần (Tam Hợp)", "Ngọ (Tam Hợp)")
            else -> listOf("Dần (Lục Hợp)", "Mão (Tam Hợp)", "Mùi (Tam Hợp)")
        }

        val tuoiLamAn = when (chiIdx) {
            0 -> listOf("Thìn", "Thân", "Sửu", "Hợi")
            1 -> listOf("Tỵ", "Dậu", "Tý", "Mão")
            2 -> listOf("Ngọ", "Tuất", "Hợi", "Mùi")
            3 -> listOf("Hợi", "Mùi", "Tuất", "Dần")
            4 -> listOf("Tý", "Thân", "Dậu", "Tỵ")
            5 -> listOf("Dậu", "Sửu", "Thân", "Thìn")
            6 -> listOf("Dần", "Tuất", "Mùi", "Hợi")
            7 -> listOf("Mão", "Hợi", "Ngọ", "Dần")
            8 -> listOf("Tý", "Thìn", "Tỵ", "Sửu")
            9 -> listOf("Sửu", "Tỵ", "Thìn", "Mùi")
            10 -> listOf("Dần", "Ngọ", "Mão", "Hợi")
            else -> listOf("Mão", "Mùi", "Dần", "Tuất")
        }

        val tuoiXongDat = when (chiIdx) {
            0 -> listOf("Thân", "Thìn", "Sửu")
            1 -> listOf("Tỵ", "Dậu", "Tý")
            2 -> listOf("Ngọ", "Tuất", "Hợi")
            3 -> listOf("Hợi", "Mùi", "Tuất")
            4 -> listOf("Tý", "Thân", "Dậu")
            5 -> listOf("Dậu", "Sửu", "Thân")
            6 -> listOf("Dần", "Tuất", "Mùi")
            7 -> listOf("Mão", "Hợi", "Ngọ")
            8 -> listOf("Tý", "Thìn", "Tỵ")
            9 -> listOf("Sửu", "Tỵ", "Thìn")
            10 -> listOf("Dần", "Ngọ", "Mão")
            else -> listOf("Mão", "Mùi", "Dần")
        }

        return TuoiHopResult(conGiap, canChi, napAm, hanh, tamHop, lucHop, tuHanhXung, lucXung, tuoiKetHon, tuoiLamAn, tuoiXongDat)
    }

    fun getXemNgayTotXauInfo(): String {
        return """
            1. BẢNG 12 TRỰC DÂN GIAN & VIỆC NÊN - KIÊNG:
               • Trực Kiến: Khởi đầu công việc, xuất hành tốt; tránh động thổ.
               • Trực Trừ (Rất tốt): Tẩy uế, dọn dẹp, chữa bệnh, xuất hành, giải oan.
               • Trực Mãn: Đầy đủ, cầu tài, mở kho, mở cửa hàng kinh doanh.
               • Trực Bình: Hòa hợp, lắp đặt, tu sửa, san nền; việc bình ổn.
               • Trực Định (Đại cát): Ký kết hợp đồng, cưới hỏi, vào nhà mới, an cư.
               • Trực Chấp: Nắm giữ, trồng trọt cây cối; tránh xuất tiền lớn.
               • Trực Phá: Phá dỡ công trình cũ; tránh cưới hỏi, khai trương.
               • Trực Nguy: Cẩn trọng đi lại vùng hiểm; tốt cho việc lễ Phật, cầu an.
               • Trực Thành (Đại cát): Trăm sự thành tựu, khai trương, cưới hỏi, thi cử.
               • Trực Thâu: Thu hoạch, thu hồi công nợ, tích lũy tài sản.
               • Trực Khai (Đại cát): Mở mang sự nghiệp, khai trương, động thổ.
               • Trực Bế: Ngăn chặn tà khí, đắp bờ, xây vá; tránh đi xa.

            2. CÁC NGÀY ĐẠI KỴ DÂN GIAN CẦN TRÁNH:
               • Ngày Tam Nương (Mồng 3, 7, 13, 18, 22, 27 Âm lịch): "Tam Nương sát hại, khởi sự bất thành". Tránh cưới hỏi, xuất hành, ký hợp đồng lớn.
               • Ngày Nguyệt Kỵ (Mồng 5, 14, 23 Âm lịch): "Mồng năm, mười bốn, hai ba / Đi chơi cũng thiệt nữa là đi buôn".
               • Ngày Sát Chủ & Thọ Tử: Tuyệt đối tránh động thổ, khởi công, cưới gả.

            3. HƯỚNG XUẤT HÀNH ĐÓN CÁT THẦN:
               • Hướng Hỷ Thần: Hướng đón hỷ khí, nhân duyên tốt đẹp, gia đạo hòa thuận.
               • Hướng Tài Thần: Hướng đón tài lộc, kinh doanh buôn bán thuận buồm xuôi gió.
               • Hướng Hạc Thần: Hướng hung hại, khi xuất phát nên tránh hướng thẳng này.
        """.trimIndent()
    }

    fun getNgayDepTheoTuoiInfo(birthYear: Int): String {
        val (napAm, hanh) = getNapAm(birthYear)
        val canIdx = (birthYear + 6) % 10
        val chiIdx = (birthYear + 8) % 12
        val canChi = "${LunarCalendarEngine.CAN[canIdx]} ${LunarCalendarEngine.CHI[chiIdx]}"
        val conGiap = LunarCalendarEngine.CON_GIAP[chiIdx]

        val sinhHanh = when (hanh) {
            "Kim" -> "Thổ sinh Kim (Rất tốt) và Kim tương hòa"
            "Mộc" -> "Thủy sinh Mộc (Rất tốt) và Mộc tương hòa"
            "Thủy" -> "Kim sinh Thủy (Rất tốt) và Thủy tương hòa"
            "Hỏa" -> "Mộc sinh Hỏa (Rất tốt) và Hỏa tương hòa"
            else -> "Hỏa sinh Thổ (Rất tốt) và Thổ tương hòa"
        }

        val kỵHanh = when (hanh) {
            "Kim" -> "Hỏa khắc Kim (Tránh ngày Hỏa)"
            "Mộc" -> "Kim khắc Mộc (Tránh ngày Kim)"
            "Thủy" -> "Thổ khắc Thủy (Tránh ngày Thổ)"
            "Hỏa" -> "Thủy khắc Hỏa (Tránh ngày Thủy)"
            else -> "Mộc khắc Thổ (Tránh ngày Mộc)"
        }

        val lucXungChi = LunarCalendarEngine.CHI[(chiIdx + 6) % 12]

        return """
            - Bản mệnh gia chủ: Tuổi $canChi ($conGiap) - Mệnh $napAm (Hành $hanh)

            1. NGUYÊN TẮC CHỌN NGÀY HOÀNG ĐẠO HỢP BẢN MỆNH:
               • Ngũ hành tương sinh: Ưu tiên ngày có ngũ hành $sinhHanh.
               • Ngũ hành tương khắc: $kỵHanh.
               • Tránh ngày Lục Xung: Tuyệt đối tránh ngày có Địa Chi là ngày $lucXungChi (xung trực diện tuổi $canChi).
               • Tránh ngày Thái Tuế: Tránh ngày có Chi trùng với năm sinh ($conGiap).
               • Tránh ngày Tam Nương (3, 7, 13, 18, 22, 27 Âm) và Nguyệt Kỵ (5, 14, 23 Âm).

            2. CÁC NGÀY HOÀNG ĐẠO NÊN CHỌN TRONG THÁNG:
               • Thanh Long Hoàng Đạo: Khởi sự may mắn, quý nhân che chở.
               • Hoàng Đạo Minh Đường: Mở cửa hàng, buôn bán phát tài phát lộc.
               • Hoàng Đạo Kim Quỹ: Mua đất, làm nhà, tích lũy điền sản.
               • Hoàng Đạo Kim Đường: Thăng tiến công danh, thi cử đỗ đạt.

            3. KHUNG GIỜ ĐẠI CÁT XUẤT HÀNH (LÝ THUẦN PHONG):
               • Giờ Đại An: Mọi việc êm đẹp, bình an, gia đạo an khang thịnh vượng.
               • Giờ Tốc Hỷ: Niềm vui đến mau chóng, cầu tài lộc buôn bán đại lợi.
               • Giờ Tiểu Cát: May mắn liên tiếp, gặp đối tác thiện lành, vạn sự hanh thông.
        """.trimIndent()
    }

    data class QueKinhDich(
        val ten: String,
        val queTuong: String,
        val thoanTu: String,
        val yNghia: String,
        val congDanh: String,
        val taiLoc: String,
        val tinhDuyen: String,
        val loiKhuyen: String
    )

    fun gieoQueKinhDichChiTiet(): QueKinhDich {
        val list = listOf(
            QueKinhDich(
                ten = "Quẻ 01: Thuần Càn (Bát Thuần Càn ☰☰)",
                queTuong = "Thiên hành kiện, quân tử dĩ tự cường bất tức (Trời vận hành mạnh mẽ, người quân tử noi theo tự cường không ngừng).",
                thoanTu = "Nguyên, Hanh, Lợi, Trinh (Khởi đầu vĩ đại, hanh thông suôn sẻ, điều lợi bền vững, chính đạo trường tồn).",
                yNghia = "Đại cát đại lợi. Biểu tượng của sức mạnh tột bực, chính khí ngút trời, thời cơ vàng để tạo lập sự nghiệp lẫy lừng.",
                congDanh = "Thăng tiến vượt bậc, có cơ hội lãnh đạo, được cấp trên và đối tác hết lòng tin cậy.",
                taiLoc = "Tài vận dồi dào, các khoản đầu tư sinh lời lớn, kinh doanh phát đạt.",
                tinhDuyen = "Gặp người xứng đôi vừa lứa, tình cảm chân thành, thấu hiểu sâu sắc.",
                loiKhuyen = "Dù đắc thời cũng phải giữ tâm khiêm cung, chính trực, tránh kiêu ngạo tự mãn."
            ),
            QueKinhDich(
                ten = "Quẻ 11: Địa Thiên Thái (Trời Đất Giao Hòa ☷☰)",
                queTuong = "Thiên địa giao nhi vạn vật thông (Trời đất giao cảm thì muôn loài hòa hợp, sinh sôi nảy nở).",
                thoanTu = "Tiểu vãng đại lai, cát hanh (Cái nhỏ qua đi, cái lớn tốt đẹp đến; mọi sự hanh thông tốt lành).",
                yNghia = "Quẻ Thái là quẻ bình an, hưng thịnh bậc nhất trong Kinh Dịch. Mọi bế tắc, xung đột đều được hóa giải.",
                congDanh = "Môi trường làm việc thuận hòa, đồng nghiệp đồng lòng, quý nhân nâng đỡ.",
                taiLoc = "Tài chính lưu thông hanh thông, công việc kinh doanh mở rộng vững vàng.",
                tinhDuyen = "Gia đình êm ấm, vợ chồng tương kính, có tin mừng thêm người thêm của.",
                loiKhuyen = "Tranh thủ thời vận tốt để củng cố nền tảng lâu dài, phòng xa lúc gian nan."
            ),
            QueKinhDich(
                ten = "Quẻ 14: Hỏa Thiên Đại Hữu (Ánh Lửa Giữa Trời ☲☰)",
                queTuong = "Hỏa tại thiên thượng, Đại Hữu (Lửa chiếu sáng trên trời cao, soi rọi khắp chốn, sở hữu phong phú).",
                thoanTu = "Nguyên hanh (Rất hanh thông, đại hưng vượng).",
                yNghia = "Biểu tượng của sự giàu có, quang minh chính đại, sở hữu nhiều của cải và đức độ được muôn người kính phục.",
                congDanh = "Danh tiếng vang xa, được tín nhiệm giao phó trọng trách lớn.",
                taiLoc = "Tiền của vào như nước, thu hồi vốn và công nợ thuận lợi.",
                tinhDuyen = "Mối duyên lành tươi sáng, được bạn đời chia ngọt sẻ bùi.",
                loiKhuyen = "Dùng tài sản làm việc thiện ích, chia sẻ với cộng đồng để phúc báu trường tồn."
            ),
            QueKinhDich(
                ten = "Quẻ 15: Địa Sơn Khiêm (Khiêm Tốn Sinh Đức ☷☶)",
                queTuong = "Địa trung hữu sơn, Khiêm (Trong lòng đất có núi cao, tượng trưng cho đức khiêm nhường sâu sắc).",
                thoanTu = "Khiêm hanh, quân tử hữu chung (Khiêm tốn thì hanh thông, người quân tử giữ được trọn vẹn).",
                yNghia = "Núi cao mà nép mình dưới đất, người có tài mà khiêm tốn thì đi đâu cũng được yêu mến, kính trọng.",
                congDanh = "Được đồng nghiệp ủng hộ, lãnh đạo tin cậy nhờ đức tính chân thành, khiêm tốn.",
                taiLoc = "Tài lộc vững bền, không bị kẻ xấu dòm ngó, phát triển an toàn.",
                tinhDuyen = "Tình yêu bền chặt, gia đạo kính trên nhường dưới thuận hòa.",
                loiKhuyen = "Khiêm tốn là cái gốc của mọi thắng lợi, luôn lắng nghe và học hỏi điều hay."
            ),
            QueKinhDich(
                ten = "Quẻ 42: Phong Lôi Ích (Gió Thổi Sấm Rền ☴☳)",
                queTuong = "Phong lôi, Ích; quân tử dĩ kiến thiện tắc thiên, hữu quá tắc cải.",
                thoanTu = "Lợi hữu du vãng, lợi thiệp đại xuyên (Có lợi khi tiến bước, thuận lợi vượt qua việc lớn lao).",
                yNghia = "Biểu tượng của sự gia tăng, bồi đắp, thu hoạch nhiều lợi ích, mở rộng sự nghiệp.",
                congDanh = "Được cất nhắc lên chức vụ mới, thi cử học hành đạt điểm cao.",
                taiLoc = "Lợi nhuận tăng trưởng đều đặn, ký kết được các hợp đồng giá trị.",
                tinhDuyen = "Tình cảm thêm gắn bó bền chặt, gia đình thuận hòa.",
                loiKhuyen = "Thấy điều thiện thì làm ngay, thấy lỗi lầm thì sửa đổi, sự nghiệp sẽ ngày càng rạng rỡ."
            ),
            QueKinhDich(
                ten = "Quẻ 13: Thiên Hỏa Đồng Nhân (Bạn Hữu Đồng Lòng ☰☲)",
                queTuong = "Thiên dữ hỏa, Đồng Nhân; quân tử dĩ loại tộc biện vật.",
                thoanTu = "Đồng nhân vu dã, hanh. Lợi thiệp đại xuyên (Hợp sức chốn đồng nội, hanh thông, vượt sông lớn có lợi).",
                yNghia = "Đoàn kết muôn người như một, chí lớn gặp nhau, hợp tác đôi bên cùng phát triển thịnh vượng.",
                congDanh = "Tìm được đối tác tâm huyết, làm việc nhóm đạt hiệu suất vượt trội.",
                taiLoc = "Góp vốn kinh doanh thu lợi nhuận tốt, phân minh minh bạch.",
                tinhDuyen = "Tìm thấy bạn tri kỷ, đồng điệu về tâm hồn và chí hướng.",
                loiKhuyen = "Lấy lòng chân thành đối đãi tha nhân, tránh tư lợi cục bộ nhỏ nhen."
            )
        )
        return list.random()
    }

    /**
     * Gieo quẻ Kinh Dịch
     */
    fun gieoQueKinhDich(): Pair<String, String> {
        val q = gieoQueKinhDichChiTiet()
        return Pair(q.ten, "${q.yNghia}\n\nLời khuyên: ${q.loiKhuyen}")
    }
}
