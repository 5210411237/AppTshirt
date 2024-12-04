package com.example.apptshirt

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailBarangActivity : AppCompatActivity() {

    private lateinit var tvNamaProduk: TextView
    private lateinit var tvHargaAsli: TextView
    private lateinit var tvDeskripsiProduk: TextView
    private lateinit var imageViewProduk: ImageView
    private lateinit var btnBack: ImageView
    private lateinit var btnAktifkan: Button
    private lateinit var spinnerUkuran: Spinner
    private lateinit var btnMinus: Button
    private lateinit var btnPlus: Button
    private lateinit var tvQuantity: TextView

    private var quantity: Int = 1 // Menyimpan jumlah barang
    private var selectedSize: String? = null // Variabel global untuk menyimpan ukuran yang dipilih

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_barang)

        // Menghubungkan View dengan ID dari XML menggunakan findViewById
        tvNamaProduk = findViewById(R.id.tvNamaProduk)
        tvHargaAsli = findViewById(R.id.tvHargaAsli)
        tvDeskripsiProduk = findViewById(R.id.tvDeskripsiProduk)
        imageViewProduk = findViewById(R.id.imageViewProduk)
        btnBack = findViewById(R.id.btnBack)
        btnAktifkan = findViewById(R.id.btnAktifkan)
        spinnerUkuran = findViewById(R.id.spinnerUkuran)
        btnMinus = findViewById(R.id.btnMinus)
        btnPlus = findViewById(R.id.btnPlus)
        tvQuantity = findViewById(R.id.tvQuantity)
        setupSpinner(spinnerUkuran)

        // Retrieve product ID from Intent
        val productId = intent.getIntExtra("PRODUCT_ID", -1)
        val productName = intent.getStringExtra("NAMA_BARANG")
        val productPrice = intent.getIntExtra("HARGA_BARANG", 0)
        val productImage = intent.getStringExtra("FOTO_BARANG")
        val productDeskripsi = intent.getStringExtra("DESKRIPSI_BARANG")
        tvNamaProduk.text = productName
        tvHargaAsli.text = productPrice.toString()
        tvDeskripsiProduk.text = productDeskripsi

        Log.d("LINK", "Link Gambar: ${productImage}")

        val imageView = findViewById<ImageView>(R.id.imageViewProduk)

        val imageUrl = (productImage?.substringAfterLast("images/") ?: "")
        Glide.with(this)
            .load("http://192.168.208.159:3001/images/${imageUrl}") // Masukkan URL atau ID sumber daya gambar
            .into(imageView)

        if (productId != -1) {
            // Ambil data produk berdasarkan ID menggunakan Retrofit
            RetrofitClient.apiService.getProductById(productId)
                .enqueue(object : Callback<DataBarang> {
                    override fun onResponse(
                        call: Call<DataBarang>,
                        response: Response<DataBarang>
                    ) {
                        if (response.isSuccessful) {
                            val product = response.body()
                            if (product != null) {
                                // Set product details
                                tvNamaProduk.text = product.nama_barang
                                tvHargaAsli.text = "Rp ${product.harga_barang}"
                                tvDeskripsiProduk.text = product.deskripsi_barang

                                // Menggunakan Glide untuk memuat gambar produk
                                val imageUrl = product.foto_barang  // Ambil URL gambar dari API
                                if (imageUrl.isNotEmpty()) {
                                    Glide.with(this@DetailBarangActivity)
                                        .load(imageUrl) // Memuat gambar dari URL
                                        .into(imageViewProduk) // Menampilkan gambar di ImageView
                                } else {
                                    // Gambar default jika URL gambar tidak ditemukan
                                    Glide.with(this@DetailBarangActivity)
                                        .load(R.drawable.kaos1) // Gambar default
                                        .into(imageViewProduk)
                                }
                            } else {
                                Log.e("API_RESPONSE", "Product data is null")
                            }
                        } else {
                            Log.e("API_ERROR", "Response failed with code: ${response.code()}")
                        }
                    }

                    override fun onFailure(call: Call<DataBarang>, t: Throwable) {
                        Log.e("API_FAILURE", "Failed to fetch product details: ${t.message}")
                        t.printStackTrace()
                    }
                })
        }

            // Menambahkan fungsi untuk tombol back
        btnBack.setOnClickListener {
            finish() // Kembali ke activity sebelumnya
        }

        // Menangani event klik tombol minus
        btnMinus.setOnClickListener {
            if (quantity > 1) {
                quantity-- // Kurangi jumlah
                tvQuantity.text = quantity.toString() // Perbarui tampilan jumlah
            }
        }

        // Menangani event klik tombol plus
        btnPlus.setOnClickListener {
            quantity++ // Tambah jumlah
            tvQuantity.text = quantity.toString() // Perbarui tampilan jumlah
        }

        // Menambahkan barang ke keranjang ketika tombol "Aktifkan" ditekan
        btnAktifkan.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                val idUser = 1
                val ukuranBarang = spinnerUkuran.selectedItem?.toString() ?: ""
                val hargaBarang = tvHargaAsli.text.toString().replace("Rp ", "").toInt()
                val hargaTotalBarang = hargaBarang * quantity

                val cartId = getOrCreateActiveCart(idUser, hargaTotalBarang)
                if (cartId != null) {
                    addCartDetail(
                        idKeranjang = cartId,
                        idBarang = productId,
                        ukuranBarang = ukuranBarang,
                        jumlahBarang = quantity,
                        hargaTotalBarang = hargaTotalBarang
                    )
                } else {
                    Toast.makeText(
                        this@DetailBarangActivity,
                        "Gagal mendapatkan keranjang!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    }
    private fun setupSpinner(spinner: Spinner) {
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Validasi: pastikan tidak memilih opsi default (misal: "Pilih ukuran")
                selectedSize = if (position > 0) {
                    parent?.getItemAtPosition(position).toString()
                } else {
                    null
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedSize = null // Reset nilai jika tidak ada yang dipilih
            }
        }
    }

    private fun validateSize(): Boolean {
        return if (selectedSize == null) {
            Toast.makeText(this, "Harap memilih ukuran terlebih dahulu!", Toast.LENGTH_SHORT).show()
            false
        } else {
            true
        }
    }

    fun getSelectedSize(): String? {
        return selectedSize
    }

    fun onSubmitData() {
        if (validateSize()) {
            // Jika validasi lolos, ambil ukuran yang dipilih
            val ukuran = getSelectedSize()
            Toast.makeText(this, "Mengirim ukuran: $ukuran", Toast.LENGTH_SHORT).show()
            // Tambahkan logika untuk mengirim data ke server di sini
        }
    }

    suspend fun getOrCreateActiveCart(idUser: Int, hargaTotal: Int): Int? {
        return withContext(Dispatchers.IO) {
            try {
                val response = RetrofitClient.apiService.getActiveCart(idUser)
                if (response.isSuccessful) {
                    // Cek jika body kosong atau tidak ada keranjang aktif
                    val activeCart = response.body()?.find { it.status_keranjang == "1" }
                    if (activeCart != null) {
                        activeCart.id_keranjang
                    } else {
                        createNewCart(idUser, hargaTotal) // Buat keranjang baru jika tidak ada keranjang aktif
                    }
                } else {
                    // Jika response gagal, coba buat keranjang baru
                    createNewCart(idUser, hargaTotal)
                }
            } catch (e: Exception) {
                Log.e("CART_ERROR", e.message ?: "Unknown error")
                null
            }
        }
    }

    private suspend fun createNewCart(idUser: Int, hargaTotal: Int): Int? {
        val request = AddCartRequest(id_user = idUser, harga_total = hargaTotal)
        return withContext(Dispatchers.IO) {
            try {
                val response = RetrofitClient.apiService.addCart(request)
                if (response.isSuccessful) response.body()?.id_keranjang else null
            } catch (e: Exception) {
                Log.e("CREATE_CART_ERROR", e.message ?: "Unknown error")
                null
            }
        }
    }

    suspend fun addCartDetail(idKeranjang: Int, idBarang: Int, ukuranBarang: String, jumlahBarang: Int, hargaTotalBarang: Int) {
        try {
            val request = CartDetailRequest(idBarang, idKeranjang, ukuranBarang, jumlahBarang, hargaTotalBarang)
            val response = RetrofitClient.apiService.addCartDetail(request)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    Toast.makeText(this@DetailBarangActivity, "Barang berhasil ditambahkan ke keranjang!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@DetailBarangActivity, "Gagal menambahkan barang ke keranjang!", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            Log.e("ADD_CART_DETAIL", e.message ?: "Unknown error")
        }
    }
}




