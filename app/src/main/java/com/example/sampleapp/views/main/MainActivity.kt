package com.example.sampleapp.views.main

import com.example.sampleapp.R
import com.example.sampleapp.databinding.ActivityMainBinding
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.maps.model.LatLng
import timber.log.Timber


class MainActivity : AppCompatActivity(), MainInterface {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainPagerAdapter: MainPagerAdapter
    private lateinit var info: LatLng

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()

        info = LatLng(0.0, 0.0)
        setupAdapter()
        binding.mainViewPager.isUserInputEnabled = false
        setupViewPager()
    }

    private fun setupViewPager() {

        binding.bottomNavigation.setOnNavigationItemSelectedListener {

            when (it.itemId) {
                R.id.home -> {
                    it.isChecked = true
                    binding.mainViewPager.setCurrentItem(0, false)

                }
                R.id.city -> {
                    it.isChecked = true
                    binding.mainViewPager.setCurrentItem(1, false)
                }
                R.id.settings -> {
                    it.isChecked = true
                    binding.mainViewPager.setCurrentItem(2, false)
                }

            }

            true
        }

        binding.mainViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                binding.bottomNavigation.menu.getItem(position).isChecked = true
            }

            override fun onPageScrollStateChanged(state: Int) {

            }

        })
    }

    private fun setupAdapter() {
        mainPagerAdapter = MainPagerAdapter(this)
        binding.mainViewPager.adapter = mainPagerAdapter
    }

    private fun setupBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        Timber.d("homeFrag yes")
    }

    override fun setPage(page: Int, data: Any) {
        binding.mainViewPager.currentItem = 1
        info = data as LatLng
    }

    override fun getData(): LatLng {
        return info
    }

    override fun onResume() {
        super.onResume()
    }


}