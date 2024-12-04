package com.example.apptshirt

import HomeFragment
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.apptshirt.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Use ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Handle window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Check if a fragment is opened from the intent
        val openFragment = intent.getStringExtra("open_fragment")
        if (openFragment != null) {
            when (openFragment) {
                "home" -> replaceFragment(HomeFragment(), "home")
                "riwayat" -> replaceFragment(RiwayatFragment(), "riwayat")
                "profile" -> replaceFragment(SettingsFragment(), "profile")
                else -> replaceFragment(HomeFragment(), "home")
            }
        } else {
            // Load default fragment
            replaceFragment(HomeFragment(), "home")
        }

        // Setup Bottom Navigation
        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    replaceFragment(HomeFragment(), "home")
                    true
                }
                R.id.riwayat -> {
                    replaceFragment(RiwayatFragment(), "riwayat")
                    true
                }
                R.id.profile -> {
                    replaceFragment(SettingsFragment(), "profile")
                    true
                }
                else -> false
            }
        }
    }

    // Function to replace fragment
    private fun replaceFragment(fragment: Fragment, tag: String) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment) // Ganti ke fragment_container
        fragmentTransaction.addToBackStack(tag)
        fragmentTransaction.commit()
    }
}