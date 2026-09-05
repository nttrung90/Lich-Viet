package com.lichviet.vannien.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.lichviet.vannien.R
import com.lichviet.vannien.databinding.ActivityMainBinding
import com.lichviet.vannien.ui.convert.DateConverterFragment
import com.lichviet.vannien.ui.day.DayCalendarFragment
import com.lichviet.vannien.ui.horoscope.HoroscopeFragment
import com.lichviet.vannien.ui.month.MonthCalendarFragment
import com.lichviet.vannien.ui.more.MoreFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    companion object {
        private const val TAG_DAY = "tag_day"
        private const val TAG_MONTH = "tag_month"
        private const val TAG_CONVERT = "tag_convert"
        private const val TAG_HOROSCOPE = "tag_horoscope"
        private const val TAG_MORE = "tag_more"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBottomNavigation()

        if (savedInstanceState == null) {
            // Mở tab Lịch Ngày đầu tiên
            loadFragment(TAG_DAY) { DayCalendarFragment() }
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            if (item.itemId == binding.bottomNavigation.selectedItemId && supportFragmentManager.findFragmentById(R.id.fragment_container) != null) {
                return@setOnItemSelectedListener true
            }
            when (item.itemId) {
                R.id.navigation_day -> {
                    loadFragment(TAG_DAY) { DayCalendarFragment() }
                    true
                }
                R.id.navigation_month -> {
                    loadFragment(TAG_MONTH) { MonthCalendarFragment() }
                    true
                }
                R.id.navigation_convert -> {
                    loadFragment(TAG_CONVERT) { DateConverterFragment() }
                    true
                }
                R.id.navigation_horoscope -> {
                    loadFragment(TAG_HOROSCOPE) { HoroscopeFragment() }
                    true
                }
                R.id.navigation_more -> {
                    loadFragment(TAG_MORE) { MoreFragment() }
                    true
                }
                else -> false
            }
        }
    }

    private fun loadFragment(tag: String, creator: () -> Fragment) {
        val fragment = supportFragmentManager.findFragmentByTag(tag) ?: creator()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment, tag)
            .commitAllowingStateLoss()
    }
}
