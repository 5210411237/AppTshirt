package com.example.apptshirt

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Deklarasi input
        val edNama = findViewById<EditText>(R.id.ed_belakang)
        val edEmail = findViewById<EditText>(R.id.ed_email)
        val edNoHp = findViewById<EditText>(R.id.ed_noHp)
        val edAlamat = findViewById<EditText>(R.id.ed_alamat)
        val edPassword = findViewById<EditText>(R.id.ed_password)

        // Tombol "Daftar Sekarang"
        val daftarButton = findViewById<Button>(R.id.bt_daftar)
        daftarButton.setOnClickListener {
            val nama = edNama.text.toString()
            val email = edEmail.text.toString()
            val noHp = edNoHp.text.toString()
            val alamat = edAlamat.text.toString()
            val password = edPassword.text.toString()

            if (nama.isEmpty() || email.isEmpty() || noHp.isEmpty() || alamat.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Semua kolom harus diisi!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Panggil API registrasi
            registerUser(nama, email, password, noHp, alamat)
        }

        // Teks "Login" di bagian bawah
        val loginText = findViewById<TextView>(R.id.login)
        loginText.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun registerUser(nama: String, email: String, password: String, noHp: String, alamat: String) {
        val registerData = RegisterData(
            nama_user = nama,
            email_user = email,
            password_user = password,
            no_handphone_user = noHp,
            alamat_user = alamat
        )

        RetrofitClient.apiService.registerUser(registerData).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                if (response.isSuccessful) {
                    val registerResponse = response.body()
                    Toast.makeText(this@RegisterActivity, registerResponse?.message, Toast.LENGTH_SHORT).show()

                    // Berpindah ke halaman login
                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@RegisterActivity, "Registrasi gagal. Periksa data Anda.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Toast.makeText(this@RegisterActivity, "Kesalahan koneksi: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
