package com.example.sampleapp.views.main

import com.example.sampleapp.views.cityfrag.CityFragment
import com.example.sampleapp.views.homefrag.HomeFragment
import com.example.sampleapp.views.settingsfrag.SettingsFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

const val NUM_OF_FRAGMENTS=3
class MainPagerAdapter(fa: FragmentActivity) :FragmentStateAdapter(fa){

    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return when(position)
        {
            0-> HomeFragment.newInstance()
            1->CityFragment.newInstance()
            else->SettingsFragment.newInstance()
        }
    }
}