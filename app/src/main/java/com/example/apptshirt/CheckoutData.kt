package com.example.apptshirt

data class CheckoutData(
    val id_checkout: Int,               // ID checkout yang baru saja dibuat
    val checkout_alamat: String,        // Alamat pengiriman
    val checkout_no_handphone: String,  // Nomor handphone penerima
    val checkout_notes: String?,        // Catatan tambahan, bersifat opsional
    val checkout_total: Double          // Total harga yang harus dibayar

)
