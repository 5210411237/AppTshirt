package com.example.apptshirt

import KeranjangAdapter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class KeranjangActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var keranjangAdapter: KeranjangAdapter
    private lateinit var keranjangList: MutableList<ModelKeranjang>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_keranjang)

        recyclerView = findViewById(R.id.recyclerViewKeranjang)
        keranjangList = KeranjangSingleton.getKeranjang().toMutableList()

        keranjangAdapter = KeranjangAdapter(keranjangList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = keranjangAdapter
    }
}

