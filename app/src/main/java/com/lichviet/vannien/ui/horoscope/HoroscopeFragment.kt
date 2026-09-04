package com.lichviet.vannien.ui.horoscope

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.lichviet.vannien.R
import com.lichviet.vannien.databinding.FragmentHoroscopeBinding
import java.util.Calendar

class HoroscopeFragment : Fragment() {

    private var _binding: FragmentHoroscopeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHoroscopeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentYear = Calendar.getInstance().get(Calendar.YEAR)

        val features = listOf(
            HoroscopeFeature(1, "Tử vi $currentYear", R.drawable.ic_horo_year),
            HoroscopeFeature(2, "Tử vi trọn đời", R.drawable.ic_horo_lifetime),
            HoroscopeFeature(3, "Xem sao\nCoi hạn", R.drawable.ic_horo_stars),
            HoroscopeFeature(4, "Bói phương\nđông", R.drawable.ic_horo_divination),
            HoroscopeFeature(5, "Xem ngày\ntốt - xấu", R.drawable.ic_horo_good_bad),
            HoroscopeFeature(6, "Xem tuổi", R.drawable.ic_horo_age),
            HoroscopeFeature(7, "Ngày đẹp\ntheo tuổi", R.drawable.ic_horo_lucky_day),
            HoroscopeFeature(8, "Phong thủy\nnhà ở", R.drawable.ic_horo_feng_shui)
        )

        val adapter = HoroscopeCardAdapter(features) { feature ->
            val intent = Intent(requireContext(), HoroscopeDetailActivity::class.java).apply {
                putExtra("FEATURE_ID", feature.id)
                putExtra("TITLE", feature.title.replace("\n", " "))
            }
            startActivity(intent)
        }

        binding.rvHoroscopeFeatures.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvHoroscopeFeatures.adapter = adapter

        binding.btnHoroscopeCrown.setOnClickListener {
            val intent = Intent(requireContext(), HoroscopeDetailActivity::class.java).apply {
                putExtra("FEATURE_ID", 1)
                putExtra("TITLE", "Tử Vi $currentYear")
            }
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
