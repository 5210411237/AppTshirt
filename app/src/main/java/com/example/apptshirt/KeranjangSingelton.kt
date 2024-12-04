package com.example.apptshirt

object KeranjangSingleton {
    private val keranjang: MutableList<ModelKeranjang> = mutableListOf()

    // Menambahkan item ke keranjang
    fun addItem(item: ModelKeranjang) {
        keranjang.add(item)
    }

    // Menghapus item dari keranjang berdasarkan ModelKeranjang
    fun removeItem(item: ModelKeranjang) {
        keranjang.remove(item)
    }

    // Menghapus seluruh keranjang
    fun clearKeranjang() {
        keranjang.clear()
    }

    // Mendapatkan daftar keranjang
    fun getKeranjang(): List<ModelKeranjang> {
        return keranjang
    }
}


