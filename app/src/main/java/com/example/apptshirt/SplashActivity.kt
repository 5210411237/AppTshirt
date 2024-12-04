package com.example.apptshirt

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)

        // Set padding for edge-to-edge view
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Mengecek apakah pengguna sudah login
        val sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("is_logged_in", false)

        // Delay selama 2 detik, lalu pindahkan ke halaman yang sesuai
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = if (isLoggedIn) {
                // Jika sudah login, menuju HomeActivity (atau MainActivity)
                Intent(this, MainActivity::class.java)
            } else {
                // Jika belum login, menuju LoginActivity
                Intent(this, LoginActivity::class.java)
            }
            startActivity(intent)
            finish() // Menutup SplashActivity agar tidak kembali
        }, 2000)
    }
}
