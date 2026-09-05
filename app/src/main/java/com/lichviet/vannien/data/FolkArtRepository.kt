package com.lichviet.vannien.data

import com.lichviet.vannien.R

/**
 * Kho dữ liệu 23 tác phẩm mỹ thuật Tranh Dân Gian Đông Hồ và Tranh Tết Việt Nam
 */
object FolkArtRepository {

    data class FolkArtwork(
        val resId: Int,
        val title: String,
        val school: String,
        val meaning: String
    )

    val artworks = listOf(
        FolkArtwork(
            R.drawable.bg_folk_dam_cuoi_chuot,
            "Đám cưới chuột",
            "Tranh Dân Gian Đông Hồ",
            "Bức tranh trứ danh vừa mang tính trào lộng sâu sắc vừa thể hiện khát vọng hòa bình, dĩ hòa vi quý, chúc cho cuộc sống luôn yên vui thái bình."
        ),
        FolkArtwork(
            R.drawable.bg_folk_chan_trau,
            "Chăn trâu thổi sáo",
            "Tranh Dân Gian Đông Hồ",
            "Hình ảnh chú bé mục đồng ngồi trên lưng trâu cất tiếng sáo véo von, biểu trưng cho sự thanh bình, an nhiên tự tại và hòa mình cùng non sông đất nước."
        ),
        FolkArtwork(
            R.drawable.bg_folk_ly_ngu,
            "Lý ngư vọng nguyệt",
            "Tranh Dân Gian Đông Hồ",
            "Cá chép trông trăng đại diện cho ý chí vượt khó vươn lên, thanh cao thoát tục, hướng đến sự hoàn mỹ và đỗ đạt đăng khoa."
        ),
        FolkArtwork(
            R.drawable.bg_folk_dai_cat,
            "Gà Đại Cát",
            "Tranh Dân Gian Đông Hồ",
            "Chú gà trống oai phong biểu thị sự may mắn, thịnh vượng, xua tan bóng tối và mang lại đại cát đại lợi cho gia chủ nhân dịp năm mới."
        ),
        FolkArtwork(
            R.drawable.bg_folk_lon_am_duong,
            "Lợn đàn âm dương",
            "Tranh Dân Gian Đông Hồ",
            "Xoáy âm dương hài hòa trên mình lợn tượng trưng cho sự sinh sôi nảy nở, no đủ sung túc, gia đình hòa thuận, mùa màng bội thu."
        ),
        FolkArtwork(
            R.drawable.bg_folk_hung_dua,
            "Hứng dừa",
            "Tranh Dân Gian Đông Hồ",
            "Bức họa trữ tình hóm hỉnh ca ngợi tình yêu lứa đôi bình dị và không khí tươi vui, hạnh phúc rộn rã nơi làng quê Việt Nam."
        ),
        FolkArtwork(
            R.drawable.bg_folk_vinh_hoa,
            "Bé ôm gà (Vinh Hoa)",
            "Tranh Tết Đông Hồ",
            "Hình tượng bé trai bụ bẫm ôm gà trống chúc tụng cho gia đình có con cái khỏe mạnh, rạng danh tổ tông, vinh hoa hiển đạt."
        ),
        FolkArtwork(
            R.drawable.bg_folk_phu_quy,
            "Bé ôm vịt (Phú Quý)",
            "Tranh Tết Đông Hồ",
            "Cặp tranh đăng đối với Vinh Hoa, chúc cho gia đạo giàu sang phú quý, duyên dáng, con cháu sum vầy, trong ấm ngoài êm."
        ),
        FolkArtwork(
            R.drawable.bg_folk_tha_dieu,
            "Mục đồng thả diều",
            "Tranh Dân Gian Đông Hồ",
            "Cánh diều no gió bay liệng giữa bầu trời bao la, thể hiện tâm hồn tự do phóng khoáng, khát vọng vươn cao của tuổi thơ đồng quê."
        ),
        FolkArtwork(
            R.drawable.bg_folk_dau_vat,
            "Đấu vật đầu xuân",
            "Tranh Dân Gian Đông Hồ",
            "Không khí sôi động ngày hội làng đầu xuân, tôn vinh tinh thần thượng võ, rèn luyện sức khỏe dẻo dai và tình đoàn kết gắn bó."
        ),
        FolkArtwork(
            R.drawable.bg_folk_hoc_bai,
            "Mục đồng học bài",
            "Tranh Dân Gian Đông Hồ",
            "Dù bận rộn việc đồng áng vẫn chăm chỉ dùi mài kinh sử, ca ngợi tinh thần hiếu học - nét đẹp truyền thống ngàn đời của dân tộc."
        ),
        FolkArtwork(
            R.drawable.bg_folk_doc_sach,
            "Mục đồng đọc sách",
            "Tranh Dân Gian Đông Hồ",
            "Khắc họa nét nho nhã, say mê tri thức, khuyên răn con cháu noi gương học tập để mai sau giúp ích cho non sông."
        ),
        FolkArtwork(
            R.drawable.bg_folk_chuot_ruoc_den,
            "Chuột rước đèn",
            "Tranh Dân Gian Đông Hồ",
            "Tái hiện không khí Tết Trung Thu tưng bừng rực rỡ với đèn ông sao, trống ếch, mang lại niềm vui hân hoan rộn rã cho trẻ thơ."
        ),
        FolkArtwork(
            R.drawable.bg_folk_ca_chep,
            "Cá chép hoa sen",
            "Tranh Dân Gian Đông Hồ",
            "Liên niên hữu dư - chúc cho gia chủ quanh năm sung túc đủ đầy, sự nghiệp hanh thông, công danh vượt vũ môn hóa rồng."
        ),
        FolkArtwork(
            R.drawable.bg_folk_ba_trieu,
            "Bà Triệu cưỡi voi",
            "Tranh Lịch Sử Việt Nam",
            "Tôn vinh khí phách hiên ngang quật cường của nữ anh hùng Triệu Thị Trinh: đạp luồng sóng dữ, chém cá kình ở biển Đông."
        ),
        FolkArtwork(
            R.drawable.bg_folk_hai_ba_trung,
            "Hai Bà Trưng ra trận",
            "Tranh Lịch Sử Việt Nam",
            "Dấu son chói lọi mở đầu truyền thống bất khuất chống ngoại xâm của phụ nữ Việt Nam, khôi phục giang sơn bờ cõi."
        ),
        FolkArtwork(
            R.drawable.bg_folk_ngo_quyen,
            "Ngô Quyền Bạch Đằng Giang",
            "Tranh Lịch Sử Việt Nam",
            "Chiến thắng Bạch Đằng lừng lẫy năm 938 chôn vùi quân Nam Hán, mở ra kỷ nguyên độc lập tự chủ muôn đời cho đất nước."
        ),
        FolkArtwork(
            R.drawable.bg_folk_ech_di_hoc,
            "Thầy đồ Cóc (Ếch đi học)",
            "Tranh Dân Gian Đông Hồ",
            "Bức họa dí dỏm răn dạy đạo nghĩa thầy trò, nề nếp kỷ cương trường lớp và sự tôn sư trọng đạo nơi thôn dã xưa."
        ),
        FolkArtwork(
            R.drawable.bg_folk_nhan_nghia,
            "Gà trống Nhân Nghĩa",
            "Tranh Dân Gian Đông Hồ",
            "Hình tượng gà trống hội tụ ngũ đức của người quân tử: Văn (mào đỏ), Vũ (cựa nhọn), Dũng (chiến đấu), Nhân (chia sẻ thức ăn), Tín (gáy đúng giờ)."
        ),
        FolkArtwork(
            R.drawable.bg_folk_san_ga,
            "Sân gà đầm ấm",
            "Tranh Dân Gian Đông Hồ",
            "Đàn gà mẹ con quây quần trong nắng ấm, biểu tượng cho mái ấm gia đình thuận hòa, tình mẫu tử thiêng liêng và cuộc sống bình dị no ấm."
        ),
        FolkArtwork(
            R.drawable.bg_folk_ong_tao,
            "Ông Táo về trời",
            "Tranh Dân Gian Đông Hồ",
            "Nét đẹp phong tục cổ truyền ngày 23 tháng Chạp tiễn Táo Quân chầu trời, cầu mong gia đạo bình an, một năm mới nhiều phúc lành."
        ),
        FolkArtwork(
            R.drawable.bg_folk_choi_chim,
            "Chơi chim tao nhã",
            "Tranh Dân Gian Đông Hồ",
            "Thú vui thanh nhã của các tao nhân mặc khách, hòa cùng thiên nhiên, dưỡng tâm an định giữa những ngày xuân tươi đẹp."
        ),
        FolkArtwork(
            R.drawable.bg_folk_danh_ghen,
            "Đánh ghen dân gian",
            "Tranh Dân Gian Đông Hồ",
            "Tranh ngụ ngôn châm biếm sâu sắc của dòng tranh Đông Hồ, răn dạy đạo nghĩa vợ chồng, giữ gìn hòa khí và khuôn phép gia phong."
        )
    )
}
