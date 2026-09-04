package com.lichviet.vannien.ui.more

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
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

        binding.cardThuocLoBan.setOnClickListener {
            val intent = Intent(requireContext(), HoroscopeDetailActivity::class.java).apply {
                putExtra("FEATURE_ID", 8)
                putExtra("TITLE", "Thước Lỗ Ban & Phong Thủy")
            }
            startActivity(intent)
        }

        binding.cardVanKhan.setOnClickListener {
            val intent = Intent(requireContext(), HoroscopeDetailActivity::class.java).apply {
                putExtra("FEATURE_ID", 9)
                putExtra("TITLE", "Văn Khấn Cổ Truyền")
            }
            startActivity(intent)
        }

        binding.cardReminders.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Cài Đặt Nhắc Nhở")
                .setMessage("Đã bật tự động thông báo nhắc nhở ngày Sóc (Mùng 1) và ngày Vọng (Rằm 15) hàng tháng vào lúc 07:00 sáng.")
                .setPositiveButton("Đồng ý", null)
                .show()
        }

        binding.cardAbout.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Lịch Việt (Vạn Niên)")
                .setMessage("Ứng dụng Lịch Việt (Vạn Niên) phiên bản 1.0.0.\n\nHỗ trợ thiết bị Android 8.0 trở lên.\nTích hợp thuật toán tính Âm Dương thiên văn học chuẩn xác tuyệt đối của TS. Hồ Ngọc Đức (UTC+7).\nĐầy đủ tính năng: Lịch ngày, Lịch tháng, Đổi ngày, Tử vi toàn tập, Thời tiết, Phong thủy, Văn khấn cổ truyền.")
                .setPositiveButton("Đóng", null)
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
