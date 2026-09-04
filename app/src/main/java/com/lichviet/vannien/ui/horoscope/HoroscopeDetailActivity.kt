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

        when (featureId) {
            1 -> { // Tử vi năm hiện tại
                binding.cardInputUser.visibility = View.VISIBLE
                val star = HoroscopeEngine.calculateCuuDieuStar(birthYear, currentYear, isMale)
                val (isTamTai, tamTaiMsg) = HoroscopeEngine.checkTamTai(birthYear, currentYear)
                val (_, kimLauMsg) = HoroscopeEngine.checkKimLau(birthYear, currentYear)
                val (_, hoangOcMsg) = HoroscopeEngine.checkHoangOc(birthYear, currentYear)

                binding.tvResultHeader.text = "Tử Vi Năm $currentYear - Tuổi $canYear $chiYear ($genderStr)"
                binding.tvResultContent.text = """
                    - Tuổi mụ: ${currentYear - birthYear + 1} tuổi (Cầm tinh con $conGiap)
                    - Sao chiếu mệnh năm $currentYear: ${star.name} (${star.type})
                      ${star.description}
                      -> Lời khuyên: ${star.advice}

                    - Hạn Tam Tai: $tamTaiMsg
                    - Hạn Kim Lâu: $kimLauMsg
                    - Hạn Hoang Ốc: $hoangOcMsg

                    - Tổng quan vận trình năm $currentYear:
                      + Công danh sự nghiệp: Có nhiều cơ hội thăng tiến, mở rộng quan hệ đối tác nếu biết nắm bắt thời cơ.
                      + Tài lộc tiền bạc: Nguồn thu ổn định, cần quản lý chi tiêu chặt chẽ, tránh đầu tư rủi ro cao.
                      + Sức khỏe: Duy trì lối sống lành mạnh, cẩn thận đi lại khi chuyển mùa.
                      + Tình cảm - Gia đạo: Gia đình hòa thuận, yêu thương gắn kết.
                """.trimIndent()
            }

            2 -> { // Tử vi trọn đời
                binding.cardInputUser.visibility = View.VISIBLE
                binding.tvResultHeader.text = "Tử Vi Trọn Đời - $canYear $chiYear ($conGiap)"
                binding.tvResultContent.text = """
                    - Bản mệnh: Tuổi $canYear $chiYear thuộc mệnh ngũ hành tương sinh, cốt cách thanh cao, thông minh, lanh lợi.
                    - Cuộc sống: Thời tiền vận có chút thăng trầm, thử thách để tôi luyện ý chí. Đến trung vận sự nghiệp vững vàng, tài lộc hanh thông. Hậu vận an nhàn, hưởng phúc lộc cùng con cháu.
                    - Tình duyên: Người tuổi $chiYear sống trọng tình nghĩa, chung thủy. Đời sống hôn nhân hòa thuận, hạnh phúc viên mãn.
                    - Tuổi hợp làm ăn: Hợp với các tuổi trong tam hợp và lục hợp.
                    - Hướng tốt xuất hành: Đông Nam, Tây Bắc.
                """.trimIndent()
            }

            3 -> { // Xem sao Coi hạn
                binding.cardInputUser.visibility = View.VISIBLE
                val star = HoroscopeEngine.calculateCuuDieuStar(birthYear, currentYear, isMale)
                val (_, tamTaiMsg) = HoroscopeEngine.checkTamTai(birthYear, currentYear)
                val (_, kimLauMsg) = HoroscopeEngine.checkKimLau(birthYear, currentYear)
                val (_, hoangOcMsg) = HoroscopeEngine.checkHoangOc(birthYear, currentYear)

                binding.tvResultHeader.text = "Bảng Sao Hạn Cửu Diệu & Tứ Hạn Đại Sự"
                binding.tvResultContent.text = """
                    1. Sao chiếu mệnh: ${star.name} (${star.type})
                       ${star.description}
                       -> Hướng dẫn dâng sao nghinh giải hạn: Làm lễ vào các ngày rằm hoặc ngày giáng trần của sao chầu trời, thắp nến theo số lượng quy định, tâm thành hướng thiện.

                    2. Niên hạn Tam Tai:
                       $tamTaiMsg

                    3. Hạn Kim Lâu (Cưới hỏi, sự nghiệp):
                       $kimLauMsg

                    4. Hạn Hoang Ốc (Động thổ xây nhà):
                       $hoangOcMsg
                """.trimIndent()
            }

            4 -> { // Bói phương đông (Gieo quẻ Kinh Dịch)
                binding.cardInputUser.visibility = View.GONE
                val (queTen, queYnghia) = HoroscopeEngine.gieoQueKinhDich()
                binding.tvResultHeader.text = "Quẻ Kinh Dịch Hôm Nay"
                binding.tvResultContent.text = """
                    $queTen

                    Ý nghĩa quẻ bói:
                    $queYnghia

                    Lời khuyên Khổng Minh:
                    Người có đức lớn thì trời đất tương trợ, làm việc gì cũng cần giữ tâm chính trực, kiên định với mục tiêu thiện lành.
                """.trimIndent()
            }

            8 -> { // Phong thủy nhà ở & Thước Lỗ Ban
                binding.cardInputUser.visibility = View.VISIBLE
                val batTrach = HoroscopeEngine.getBatTrach(birthYear, isMale)
                binding.tvResultHeader.text = "Phong Thủy Bát Trạch & Thước Lỗ Ban"
                binding.tvResultContent.text = """
                    - Cung phi Bát Trạch: Cung ${batTrach.cungMenh} - Ngũ hành: ${batTrach.nguHanh} (Quái số: ${batTrach.quaiSo})

                    - CÁC HƯỚNG TỐT (ĐẠI CÁT):
                      ${batTrach.huongTot.joinToString("\n                      ")}

                    - CÁC HƯỚNG XẤU (NÊN TRÁNH):
                      ${batTrach.huongXau.joinToString("\n                      ")}

                    - QUY CHUẨN THƯỚC LỖ BAN PHONG THỦY:
                      + Thước 52.2 cm: Đo khoảng thông thủy (cửa đi, cửa sổ, chiều cao giếng trời). Cung tốt: Quý Nhân, Hiểm Trì, Thiên Tai, Thiên Tài, Phúc Tinh.
                      + Thước 42.9 cm (Dương trạch): Đo khối xây dựng (bậc cầu thang, bàn bếp, giường ngủ). Cung tốt: Tài, Nghĩa, Quan, Bản.
                      + Thước 38.8 cm (Âm phần): Đo bàn thờ, tủ thờ, đồ thờ cúng gia tiên.
                """.trimIndent()
            }

            9 -> { // Văn khấn cổ truyền
                binding.cardInputUser.visibility = View.GONE
                binding.tvResultHeader.text = "Văn Khấn Cổ Truyền Việt Nam"
                val vanKhan1 = HolidayRepository.vanKhanList[0]
                val vanKhan2 = HolidayRepository.vanKhanList[1]
                binding.tvResultContent.text = """
                    ${vanKhan1.title.uppercase()}
                    (${vanKhan1.occasion})
                    ${vanKhan1.content}

                    ------------------------------------------

                    ${vanKhan2.title.uppercase()}
                    (${vanKhan2.occasion})
                    ${vanKhan2.content}
                """.trimIndent()
            }

            else -> { // Xem tuổi, ngày đẹp
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
