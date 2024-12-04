package com.example.apptshirt

data class DataBarangResponse(
    val status: String,  // Menyimpan status, misalnya "success" atau "error"
    val message: String, // Menyimpan pesan untuk memberi tahu status permintaan
    val data: DataBarang? // Data barang (opsional, bisa null jika terjadi error)
)