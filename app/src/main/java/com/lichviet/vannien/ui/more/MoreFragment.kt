package com.lichviet.vannien.ui.more

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.lichviet.vannien.R
import com.lichviet.vannien.data.FolkArtRepository
import com.lichviet.vannien.data.HolidayRepository
import com.lichviet.vannien.databinding.FragmentMoreBinding
import com.lichviet.vannien.ui.horoscope.HoroscopeDetailActivity

class MoreFragment : Fragment() {

    private var _binding: FragmentMoreBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Thước Lỗ Ban & Phong Thủy Bát Trạch
        binding.cardThuocLoBan.setOnClickListener {
            val intent = Intent(requireContext(), HoroscopeDetailActivity::class.java).apply {
                putExtra("FEATURE_ID", 8)
                putExtra("TITLE", "Thước Lỗ Ban & Phong Thủy")
            }
            startActivity(intent)
        }

        // 2. Văn khấn cổ truyền (9 bài)
        binding.cardVanKhan.setOnClickListener {
            val intent = Intent(requireContext(), HoroscopeDetailActivity::class.java).apply {
                putExtra("FEATURE_ID", 9)
                putExtra("TITLE", "Văn Khấn Cổ Truyền")
            }
            startActivity(intent)
        }

        // 3. Lịch Ngày Lễ Truyền Thống trong năm
        binding.cardHolidays.setOnClickListener {
            showHolidaysDialog()
        }

        // 4. Bộ sưu tập 23 Tranh Dân Gian
        binding.cardFolkGallery.setOnClickListener {
            showFolkArtGalleryDialog()
        }

        // 5. Cài đặt nhắc nhở Mùng 1, Rằm & Ngày lễ
        binding.cardReminders.setOnClickListener {
            showReminderSettingsDialog()
        }

        // 6. Giới thiệu ứng dụng & Bản quyền
        binding.cardAbout.setOnClickListener {
            showAboutDialog()
        }
    }

    private fun showHolidaysDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_holidays_list, null)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        val tvLunar = dialogView.findViewById<TextView>(R.id.tv_lunar_holidays_content)
        val tvSolar = dialogView.findViewById<TextView>(R.id.tv_solar_holidays_content)
        val btnClose = dialogView.findViewById<Button>(R.id.btn_holidays_close)

        val lunarSb = StringBuilder()
        HolidayRepository.holidays.filter { it.isLunar }.forEach { h ->
            lunarSb.append("• Ngày %02d/%02d ÂL: %s\n  (%s)\n\n".format(h.day, h.month, h.name, h.description))
        }
        tvLunar.text = lunarSb.toString().trimEnd()

        val solarSb = StringBuilder()
        HolidayRepository.holidays.filter { !it.isLunar }.forEach { h ->
            solarSb.append("• Ngày %02d/%02d DL: %s\n  (%s)\n\n".format(h.day, h.month, h.name, h.description))
        }
        tvSolar.text = solarSb.toString().trimEnd()

        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showFolkArtGalleryDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_folk_gallery, null)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        val ivArt = dialogView.findViewById<ImageView>(R.id.iv_gallery_art)
        val tvCounter = dialogView.findViewById<TextView>(R.id.tv_gallery_counter)
        val tvTitle = dialogView.findViewById<TextView>(R.id.tv_gallery_title)
        val tvSchool = dialogView.findViewById<TextView>(R.id.tv_gallery_school)
        val tvMeaning = dialogView.findViewById<TextView>(R.id.tv_gallery_meaning)
        val btnPrev = dialogView.findViewById<Button>(R.id.btn_gallery_prev)
        val btnNext = dialogView.findViewById<Button>(R.id.btn_gallery_next)
        val btnClose = dialogView.findViewById<Button>(R.id.btn_gallery_close)

        val artworks = FolkArtRepository.artworks
        var currentIndex = 0

        fun updateView() {
            val art = artworks[currentIndex]
            ivArt.setImageResource(art.resId)
            tvCounter.text = "${currentIndex + 1} / ${artworks.size}"
            tvTitle.text = art.title
            tvSchool.text = art.school
            tvMeaning.text = art.meaning
        }

        updateView()

        btnPrev.setOnClickListener {
            currentIndex = if (currentIndex > 0) currentIndex - 1 else artworks.size - 1
            updateView()
        }

        btnNext.setOnClickListener {
            currentIndex = (currentIndex + 1) % artworks.size
            updateView()
        }

        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showReminderSettingsDialog() {
        val prefs = requireContext().getSharedPreferences("app_reminders_prefs", Context.MODE_PRIVATE)
        val options = arrayOf(
            "Nhắc ngày Sóc (Mùng 1 Âm lịch) - 07:00 sáng",
            "Nhắc ngày Vọng (Rằm 15 Âm lịch) - 07:00 sáng",
            "Thông báo các ngày Lễ & Tết truyền thống",
            "Tự động đổi tranh dân gian trang chủ mỗi ngày"
        )
        val keys = arrayOf("remind_soc", "remind_vong", "remind_holidays", "auto_change_art")
        val checkedItems = booleanArrayOf(
            prefs.getBoolean("remind_soc", true),
            prefs.getBoolean("remind_vong", true),
            prefs.getBoolean("remind_holidays", true),
            prefs.getBoolean("auto_change_art", true)
        )

        AlertDialog.Builder(requireContext())
            .setTitle("Cài Đặt Nhắc Nhở & Thông Báo")
            .setMultiChoiceItems(options, checkedItems) { _, which, isChecked ->
                checkedItems[which] = isChecked
            }
            .setPositiveButton("Lưu Cài Đặt") { _, _ ->
                val editor = prefs.edit()
                for (i in keys.indices) {
                    editor.putBoolean(keys[i], checkedItems[i])
                }
                editor.apply()
                Toast.makeText(requireContext(), "Đã lưu cài đặt thông báo & nhắc nhở thành công!", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    private fun showAboutDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Lịch Việt (Vạn Niên)")
            .setMessage(
                """
                Ứng dụng Lịch Việt (Vạn Niên) - Phiên bản 1.2.2

                • Tác giả & Phát triển: Đội ngũ Lịch Việt
                • Thuật toán Âm Dương: Kế thừa công trình tính toán thiên văn học chính xác tuyệt đối của TS. Hồ Ngọc Đức (Múi giờ Việt Nam UTC+7).
                • Mỹ thuật Dân Gian: Tuyển tập 33 kiệt tác Tranh Dân Gian Đông Hồ, Hàng Trống & Kim Hoàng đậm đà bản sắc dân tộc.
                • Tính năng toàn diện:
                   - Lịch Ngày, Giờ Hoàng Đạo, Hướng xuất hành
                   - Lịch Tháng vạn niên, Đổi ngày Âm - Dương
                   - Tìm ngày tốt (Cưới hỏi, Làm nhà, Khai trương, Xuất hành)
                   - Tử Vi toàn tập: Vận hạn năm, Trọn đời, Cung phi Bát Trạch
                   - Thước Lỗ Ban 52.2cm, 42.9cm, 38.8cm phong thủy
                   - Tuyển tập 9 bài Văn Khấn Cổ Truyền Việt Nam
                   - Dự báo thời tiết các tỉnh thành Việt Nam

                Cảm ơn bạn đã tin tưởng và sử dụng ứng dụng Lịch Việt!
                """.trimIndent()
            )
            .setPositiveButton("Đóng", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
