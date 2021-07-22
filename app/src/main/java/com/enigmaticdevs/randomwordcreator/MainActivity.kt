package com.enigmaticdevs.randomwordcreator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.enigmaticdevs.randomwordcreator.databinding.ActivityMainBinding
import com.enigmaticdevs.randomwordcreator.fragments.FragmentCreator
import com.enigmaticdevs.randomwordcreator.fragments.FragmentSaved
import com.enigmaticdevs.randomwordcreator.adapters.CustomFragmentPagerAdapter

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val adapter = CustomFragmentPagerAdapter(supportFragmentManager)
        // Add fragments here
        adapter.addFragment(FragmentCreator(), "Creator")
        adapter.addFragment(FragmentSaved(), "Saved")
        binding.viewpager.adapter = adapter
        binding.tabs.setupWithViewPager(binding.viewpager)
    }


}