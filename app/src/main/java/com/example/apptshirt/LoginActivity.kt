package com.example.apptshirt

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val emailEditText = findViewById<EditText>(R.id.etEmailLogin)
        val passwordEditText = findViewById<EditText>(R.id.etPasswordLogin)
        val loginButton = findViewById<Button>(R.id.btnLogin)
        val registerTextView = findViewById<TextView>(R.id.daftar_register)

        registerTextView.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginUser(email, password)
            } else {
                Toast.makeText(this, "Email dan Password harus diisi", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loginUser(email: String, password: String) {
        // Show loading indicator
        val loadingDialog = ProgressDialog(this).apply {
            setMessage("Sedang login...")
            setCancelable(false)
            show()
        }

        val loginRequest = LoginData(email_user = email, password_user = password)

        // Log JSON data dikirim ke server
        Log.d("LoginActivity", "Data login dikirim: ${Gson().toJson(loginRequest)}")

        RetrofitClient.apiService.login(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                loadingDialog.dismiss()

                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null && loginResponse.data?.token?.isNotEmpty() == true) {
                        Log.d("LoginActivity", "Login sukses: Token diterima.")

                        // Simpan token dan data user ke SharedPreferences
                        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
                        sharedPreferences.edit().apply {
                            putString("token", loginResponse.data.token)
                            putString("user_id", loginResponse.data.id_user)
                            putString("user_email", loginResponse.data.email_user)
                            putString("user_name", loginResponse.data.nama_user)
                            apply()
                        }

                        Toast.makeText(
                            this@LoginActivity,
                            loginResponse.message ?: "Login berhasil!",
                            Toast.LENGTH_SHORT
                        ).show()

                        // Navigasi ke MainActivity
                        val homeIntent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(homeIntent)
                        finish()
                    } else {
                        Log.e("LoginActivity", "Response body is null or token missing.")
                        Toast.makeText(
                            this@LoginActivity,
                            "Login gagal: Data response tidak valid.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    handleError(response)
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                loadingDialog.dismiss()
                Log.e("LoginActivity", "Login error: ${t.message}", t)

                val errorMessage = when {
                    t is java.net.SocketTimeoutException ->
                        "Koneksi timeout. Pastikan server berjalan dan dapat diakses."
                    t is java.net.UnknownHostException ->
                        "Tidak dapat terhubung ke server. Periksa alamat server dan koneksi internet Anda."
                    t is java.net.ConnectException ->
                        "Gagal terhubung ke server. Pastikan server berjalan."
                    else -> "Terjadi kesalahan jaringan: ${t.message}"
                }

                Toast.makeText(
                    this@LoginActivity,
                    errorMessage,
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    private fun handleError(response: Response<LoginResponse>) {
        val errorBody = response.errorBody()?.string()
        Log.e("LoginActivity", "Raw error body: $errorBody")

        try {
            val gson = Gson()
            val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)
            val errorMessage = when (response.code()) {
                401 -> "Email atau password salah"
                403 -> "Akses ditolak"
                404 -> "Server tidak ditemukan"
                500 -> "Terjadi kesalahan pada server"
                else -> errorResponse.message ?: "Login gagal: Terjadi kesalahan."
            }

            Toast.makeText(
                this,
                errorMessage,
                Toast.LENGTH_LONG
            ).show()
        } catch (e: JsonSyntaxException) {
            Log.e("LoginActivity", "Error parsing JSON: ${e.message}")
            Toast.makeText(
                this,
                "Login gagal: ${response.message()}",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    data class ErrorResponse(
        val message: String?,
        val details: String?
    )
}