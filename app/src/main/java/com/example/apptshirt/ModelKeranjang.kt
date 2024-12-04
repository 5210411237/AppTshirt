package com.example.apptshirt

data class ModelKeranjang(
    val product: ModelProduct,
    val quantity: Int
)

data class KeranjangResponse(
    val idKeranjang: Int,
)
data class CartResponse(
    val id_keranjang: Int,
    val id_user: Int,
    val keranjang_tanggal: String,
    val harga_total: String,
    val status_keranjang: String
)

data class CartDetailRequest(
    val id_barang: Int,
    val id_keranjang: Int,
    val ukuran_barang: String,
    val jumlah_barang: Int,
    val harga_total_barang: Int
)
data class AddCartRequest(
    val id_user: Int,
    val harga_total: Int
)
