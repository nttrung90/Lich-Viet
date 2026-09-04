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

    private val dayCalendarFragment by lazy { DayCalendarFragment() }
    private val monthCalendarFragment by lazy { MonthCalendarFragment() }
    private val dateConverterFragment by lazy { DateConverterFragment() }
    private val horoscopeFragment by lazy { HoroscopeFragment() }
    private val moreFragment by lazy { MoreFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBottomNavigation()

        if (savedInstanceState == null) {
            // Mở tab Lịch Ngày đầu tiên
            loadFragment(dayCalendarFragment)
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_day -> {
                    loadFragment(dayCalendarFragment)
                    true
                }
                R.id.navigation_month -> {
                    loadFragment(monthCalendarFragment)
                    true
                }
                R.id.navigation_convert -> {
                    loadFragment(dateConverterFragment)
                    true
                }
                R.id.navigation_horoscope -> {
                    loadFragment(horoscopeFragment)
                    true
                }
                R.id.navigation_more -> {
                    loadFragment(moreFragment)
                    true
                }
                else -> false
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
