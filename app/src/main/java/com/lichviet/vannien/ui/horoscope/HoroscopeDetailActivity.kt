package com.lichviet.vannien.ui.horoscope

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.lichviet.vannien.calendar.HoroscopeEngine
import com.lichviet.vannien.calendar.LunarCalendarEngine
import com.lichviet.vannien.data.HolidayRepository
import com.lichviet.vannien.databinding.ActivityHoroscopeDetailBinding
import java.util.Calendar

class HoroscopeDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHoroscopeDetailBinding
    private var featureId = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHoroscopeDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        featureId = intent.getIntExtra("FEATURE_ID", 1)
        val title = intent.getStringExtra("TITLE") ?: "Tử Vi & Phong Thủy"
        binding.tvHoroDetailTitle.text = title

        setupBirthYearSpinner()
        setupListeners()
        computeResult()
    }

    private fun setupBirthYearSpinner() {
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val years = (currentYear downTo 1930).map { it.toString() }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, years)
        binding.spinnerBirthYear.adapter = adapter
        // Mặc định chọn năm 1990 như trên ảnh mẫu số 3!
        val defaultIndex = years.indexOf("1990").coerceAtLeast(0)
        binding.spinnerBirthYear.setSelection(defaultIndex)
    }

    private fun setupListeners() {
        binding.btnHoroBack.setOnClickListener {
            finish()
        }

        binding.btnCalculateHoro.setOnClickListener {
            computeResult()
        }
    }

    private fun computeResult() {
        val birthYearStr = binding.spinnerBirthYear.selectedItem?.toString() ?: "1990"
        val birthYear = birthYearStr.toIntOrNull() ?: 1990
        val isMale = binding.rbMale.isChecked
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val genderStr = if (isMale) "Nam mạng" else "Nữ mạng"

        val canYear = LunarCalendarEngine.CAN[(birthYear + 6) % 10]
        val chiYear = LunarCalendarEngine.CHI[(birthYear + 8) % 12]
        val conGiap = LunarCalendarEngine.CON_GIAP[(birthYear + 8) % 12]
        val (napAm, hanh) = HoroscopeEngine.getNapAm(birthYear)

        when (featureId) {
            1 -> { // Tử vi năm hiện tại
                binding.cardInputUser.visibility = View.VISIBLE
                binding.tvInputLabel.text = "Thông tin tra cứu tử vi năm $currentYear:"
                binding.layoutBirthInput.visibility = View.VISIBLE
                binding.btnCalculateHoro.visibility = View.VISIBLE
                binding.btnCalculateHoro.text = "Xem Vận Trình Năm $currentYear"

                val star = HoroscopeEngine.calculateCuuDieuStar(birthYear, currentYear, isMale)
                val (_, tamTaiMsg) = HoroscopeEngine.checkTamTai(birthYear, currentYear)
                val (_, kimLauMsg) = HoroscopeEngine.checkKimLau(birthYear, currentYear)
                val (_, hoangOcMsg) = HoroscopeEngine.checkHoangOc(birthYear, currentYear)

                binding.tvResultHeader.text = "Tử Vi Năm $currentYear - Tuổi $canYear $chiYear ($genderStr)"
                binding.tvResultContent.text = """
                    - Bản mệnh: Tuổi $canYear $chiYear (Cầm tinh con $conGiap)
                    - Ngũ hành nạp âm: $napAm (Mệnh $hanh)
                    - Tuổi mụ năm $currentYear: ${currentYear - birthYear + 1} tuổi

                    1. SAO CHIẾU MỆNH NĂM $currentYear:
                       • Tên sao: ${star.name} (${star.type})
                       • Luận giải: ${star.description}
                       • Hóa giải & Nghinh đón: ${star.advice}

                    2. ĐẠI HẠN NIÊN KHÓA:
                       • Hạn Tam Tai: $tamTaiMsg
                       • Hạn Kim Lâu (Cưới gả, việc lớn): $kimLauMsg
                       • Hạn Hoang Ốc (Động thổ, xây cất): $hoangOcMsg

                    3. VẬN HẠN TOÀN DIỆN NĂM $currentYear:
                       • Công danh - Sự nghiệp: Năm có nhiều vận hội chuyển biến tích cực. Cần kiên định với chí hướng, khéo léo trong giao tiếp với đồng nghiệp và cấp trên.
                       • Tài chính - Tài lộc: Tiền bạc luân chuyển đều đặn, có quý nhân phù trợ lúc ngặt nghèo. Nên duy trì kế hoạch chi tiêu kỷ luật, tránh đầu cơ quá tay.
                       • Tình duyên - Gia đạo: Hòa khí trong ấm ngoài êm, người độc thân có cơ duyên gặp gỡ người tâm đầu ý hợp; gia đình có nhiều hỷ sự.
                       • Sức khỏe: Chú ý giấc ngủ, dưỡng tâm an định, cẩn trọng khi đi lại sông nước hoặc di chuyển xa vào các tháng giao mùa.
                """.trimIndent()
            }

            2 -> { // Tử vi trọn đời
                binding.cardInputUser.visibility = View.VISIBLE
                binding.tvInputLabel.text = "Thông tin tra cứu tử vi trọn đời:"
                binding.layoutBirthInput.visibility = View.VISIBLE
                binding.btnCalculateHoro.visibility = View.VISIBLE
                binding.btnCalculateHoro.text = "Luận Giải Trọn Đời"

                val batTrach = HoroscopeEngine.getBatTrach(birthYear, isMale)
                val tuoiHop = HoroscopeEngine.getTuoiHop(birthYear)

                binding.tvResultHeader.text = "Tử Vi Trọn Đời - $canYear $chiYear ($genderStr)"
                binding.tvResultContent.text = """
                    - Bản mệnh: Tuổi $canYear $chiYear (Tướng tinh con $conGiap)
                    - Cung phi: Cung ${batTrach.cungMenh} - Ngũ hành: $napAm (Mệnh $hanh)

                    1. TỔNG QUAN VẬN MỆNH:
                       Người tuổi $canYear $chiYear tính tình cương trực, thông minh, giàu lòng nhân ái và tự lập từ sớm. Cuộc đời trải qua nhiều thăng trầm rèn luyện bản lĩnh trước khi đạt được thành quả bền vững.
                       • Tiền vận: Gặp nhiều thử thách, học hỏi tích lũy kiến thức và kinh nghiệm, tôi luyện ý chí.
                       • Trung vận: Sự nghiệp định hình, tài lộc vượng phát, khẳng định được vị thế trong xã hội.
                       • Hậu vận: Phúc thọ an khang, con cháu hiếu thuận, gia đạo hưng thịnh vững bền.

                    2. TÌNH DUYÊN & GIA ĐẠO:
                       • Đời sống hôn nhân hòa thuận, thấu hiểu và sẻ chia. Gia đạo lấy chữ 'Nhẫn' và chữ 'Đức' làm gốc rễ thì muôn đời hưng thịnh.
                       • Tuổi hợp kết hôn: ${tuoiHop.tuoiKetHon.joinToString(", ")}.

                    3. TUỔI LÀM ĂN & CỘNG TÁC:
                       • Hợp tác kinh doanh đại cát với các tuổi: ${tuoiHop.tuoiLamAn.joinToString(", ")}.
                       • Cần nhường nhịn, cẩn trọng khi cộng tác với tuổi xung: ${tuoiHop.lucXung}.

                    4. HƯỚNG TỐT & MÀU SẮC BẢN MỆNH:
                       • Hướng đại cát: ${batTrach.huongTot.take(2).joinToString(", ")}.
                       • Màu sắc tương sinh: Mang lại may mắn, vượng khí cho gia chủ.
                """.trimIndent()
            }

            3 -> { // Xem sao Coi hạn
                binding.cardInputUser.visibility = View.VISIBLE
                binding.tvInputLabel.text = "Tra cứu sao chiếu mệnh & niên hạn:"
                binding.layoutBirthInput.visibility = View.VISIBLE
                binding.btnCalculateHoro.visibility = View.VISIBLE
                binding.btnCalculateHoro.text = "Xem Chi Tiết Sao Hạn"

                val star = HoroscopeEngine.calculateCuuDieuStar(birthYear, currentYear, isMale)
                val (_, tamTaiMsg) = HoroscopeEngine.checkTamTai(birthYear, currentYear)
                val (_, kimLauMsg) = HoroscopeEngine.checkKimLau(birthYear, currentYear)
                val (_, hoangOcMsg) = HoroscopeEngine.checkHoangOc(birthYear, currentYear)

                binding.tvResultHeader.text = "Bảng Sao Cửu Diệu & Tam Tai - Kim Lâu - Hoang Ốc"
                binding.tvResultContent.text = """
                    1. SAO CHIẾU MỆNH NĂM $currentYear:
                       • Sao chủ quản: ${star.name} (${star.type})
                       • Tác động: ${star.description}
                       • Cách thức nghinh cát / giải hạn: ${star.advice}
                       • Lễ vật dâng sao: Hoa tươi, quả ngọt, trà nước, nến sáng theo vị trí phương vị giáng trần vào các ngày định kỳ hàng tháng. Tâm thành kính hướng thiện là gốc giải hung.

                    2. BỘ BA ĐẠI HẠN DÂN GIAN NĂM $currentYear:
                       • Hạn Tam Tai: $tamTaiMsg
                       • Hạn Kim Lâu (Cưới hỏi): $kimLauMsg
                       • Hạn Hoang Ốc (Nhà cửa): $hoangOcMsg

                    3. LỜI KHUYÊN PHONG THỦY:
                       Gặp năm sao tốt thì nỗ lực mở mang phát triển; gặp năm hạn xấu nên cẩn trọng giữ mình, tích đức làm việc thiện, phóng sinh tu dưỡng để tai qua nạn khỏi, chuyển hung hóa cát.
                """.trimIndent()
            }

            4 -> { // Bói phương đông (Gieo quẻ Kinh Dịch)
                binding.cardInputUser.visibility = View.VISIBLE
                binding.tvInputLabel.text = "Thành tâm xin quẻ Kinh Dịch linh ứng:"
                binding.layoutBirthInput.visibility = View.GONE
                binding.btnCalculateHoro.visibility = View.VISIBLE
                binding.btnCalculateHoro.text = "Gieo Quẻ Kinh Dịch Mới"

                val que = HoroscopeEngine.gieoQueKinhDichChiTiet()
                binding.tvResultHeader.text = que.ten
                binding.tvResultContent.text = """
                    - TƯỢNG QUẺ:
                      ${que.queTuong}

                    - THOÁN TỪ:
                      ${que.thoanTu}

                    - Ý NGHĨA QUẺ BÓI:
                      ${que.yNghia}

                    - CÔNG DANH & SỰ NGHIỆP:
                      ${que.congDanh}

                    - TÀI LỘC & KINH DOANH:
                      ${que.taiLoc}

                    - TÌNH CẢM & GIA ĐẠO:
                      ${que.tinhDuyen}

                    - LỜI KHUYÊN KHỔNG MINH TIÊN SINH:
                      ${que.loiKhuyen}
                """.trimIndent()
            }

            5 -> { // Xem ngày tốt - xấu
                binding.cardInputUser.visibility = View.VISIBLE
                binding.tvInputLabel.text = "Cẩm nang tra cứu ngày tốt - xấu dân gian:"
                binding.layoutBirthInput.visibility = View.GONE
                binding.btnCalculateHoro.visibility = View.GONE

                binding.tvResultHeader.text = "Cẩm Nang Chọn Ngày Tốt - Xấu Dân Gian"
                binding.tvResultContent.text = HoroscopeEngine.getXemNgayTotXauInfo()
            }

            6 -> { // Xem tuổi
                binding.cardInputUser.visibility = View.VISIBLE
                binding.tvInputLabel.text = "Chọn năm sinh để xem tuổi hợp - xung:"
                binding.layoutBirthInput.visibility = View.VISIBLE
                binding.btnCalculateHoro.visibility = View.VISIBLE
                binding.btnCalculateHoro.text = "Tra Cứu Tuổi Hợp - Xung"

                val hop = HoroscopeEngine.getTuoiHop(birthYear)

                binding.tvResultHeader.text = "Luận Giải Tuổi Hợp - Xung: Tuổi ${hop.canChi} (${hop.conGiap})"
                binding.tvResultContent.text = """
                    - Bản mệnh: Tuổi ${hop.canChi} - Mệnh ${hop.napAm} (${hop.hanh})

                    1. BỘ TAM HỢP & LỤC HỢP (QUÝ NHÂN TƯƠNG TRỢ):
                       • Tam Hợp: ${hop.tamHop}
                       • Lục Hợp: ${hop.lucHop}
                       -> Khi cộng tác hoặc gắn bó với người tuổi này, công việc dễ đạt thành tựu, gia đạo hòa thuận, phúc lộc dồi dào.

                    2. BỘ TỨ HÀNH XUNG & LỤC XUNG (CẦN CẨN TRỌNG):
                       • Tứ Hành Xung: ${hop.tuHanhXung}
                       • Lục Xung (Trực Xung): ${hop.lucXung}
                       -> Nên dĩ hòa vi quý, tránh đối đầu trực diện, giữ tâm điềm đạm khi làm việc chung.

                    3. TUỔI HỢP KẾT HÔN XÂY DỰNG GIA ĐÌNH:
                       ${hop.tuoiKetHon.joinToString(", ")}

                    4. TUỔI HỢP CỘNG TÁC LÀM ĂN PHÁT TÀI:
                       ${hop.tuoiLamAn.joinToString(", ")}

                    5. TUỔI ĐẸP XÔNG NHÀ, XÔNG ĐẤT ĐẦU NĂM:
                       ${hop.tuoiXongDat.joinToString(", ")}
                """.trimIndent()
            }

            7 -> { // Ngày đẹp theo tuổi
                binding.cardInputUser.visibility = View.VISIBLE
                binding.tvInputLabel.text = "Chọn năm sinh gia chủ để tìm ngày hoàng đạo:"
                binding.layoutBirthInput.visibility = View.VISIBLE
                binding.btnCalculateHoro.visibility = View.VISIBLE
                binding.btnCalculateHoro.text = "Tìm Ngày Đẹp Hợp Mệnh"

                binding.tvResultHeader.text = "Ngày Hoàng Đạo & Giờ Xuất Hành Hợp Tuổi $canYear $chiYear"
                binding.tvResultContent.text = HoroscopeEngine.getNgayDepTheoTuoiInfo(birthYear)
            }

            8 -> { // Phong thủy nhà ở & Thước Lỗ Ban
                binding.cardInputUser.visibility = View.VISIBLE
                binding.tvInputLabel.text = "Chọn thông tin gia chủ để xem Bát Trạch:"
                binding.layoutBirthInput.visibility = View.VISIBLE
                binding.btnCalculateHoro.visibility = View.VISIBLE
                binding.btnCalculateHoro.text = "Xem Cung Mệnh & Hướng Nhà"

                val batTrach = HoroscopeEngine.getBatTrach(birthYear, isMale)
                binding.tvResultHeader.text = "Phong Thủy Bát Trạch & Thước Lỗ Ban"
                binding.tvResultContent.text = """
                    - Cung phi Bát Trạch: Cung ${batTrach.cungMenh} - Ngũ hành: ${batTrach.nguHanh} (Quái số: ${batTrach.quaiSo})

                    1. CÁC HƯỚNG TỐT (ĐẠI CÁT):
                       ${batTrach.huongTot.joinToString("\n                       ")}

                    2. CÁC HƯỚNG XẤU (NÊN TRÁNH):
                       ${batTrach.huongXau.joinToString("\n                       ")}

                    3. QUY CHUẨN THƯỚC LỖ BAN PHONG THỦY:
                       • Thước 52.2 cm: Đo khoảng thông thủy (cửa đi, cửa sổ, giếng trời). Cung tốt: Quý Nhân, Thiên Tài, Phúc Tinh, Tể Tướng. Cung xấu: Hiểm Trì, Thiên Tai, Cô Độc, Tai Hại.
                       • Thước 42.9 cm (Dương trạch): Đo khối xây dựng (bậc cầu thang, bàn bếp, giường ngủ). Cung tốt: Tài, Nghĩa, Quan, Bản. Cung xấu: Bệnh, Ly, Kiếp, Hại.
                       • Thước 38.8 cm (Âm phần): Đo bàn thờ, tủ thờ, mộ phần gia tiên. Cung tốt: Đinh, Vượng, Nghĩa, Quan, Hưng, Tài. Cung xấu: Hại, Khổ, Tử, Thất.
                """.trimIndent()
            }

            else -> {
                binding.cardInputUser.visibility = View.VISIBLE
                binding.tvResultHeader.text = "Luận Giải Tuổi $canYear $chiYear"
                binding.tvResultContent.text = """
                    - Tam Hợp: Hợp tác làm ăn và kết duyên tốt nhất với các tuổi trong bộ tam hợp.
                    - Lục Hợp: Tuổi nhị hợp mang lại may mắn, bình an trong gia đạo.
                    - Tứ Hành Xung: Nên cẩn trọng, nhường nhịn khi kết giao với các tuổi thuộc bộ tứ hành xung.
                    - Ngày đẹp hợp tuổi: Ngày có Can Chi tương sinh với bản mệnh, rơi vào ngày Hoàng Đạo.
                """.trimIndent()
            }
        }
    }
}
