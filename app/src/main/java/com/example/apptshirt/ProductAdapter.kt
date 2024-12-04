package com.example.apptshirt

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ProductAdapter(
    private val context: Context,
    private val barangList: List<DataBarang>,
    private val onItemClick: (DataBarang) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    companion object {
            private const val BASE_URL = "http://192.168.208.159:3001/images/"
    }

    // ViewHolder sebagai inner class
    inner class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val productImage: ImageView = view.findViewById(R.id.productImage)
        val productTitle: TextView = view.findViewById(R.id.productTitle)
        val productPrice: TextView = view.findViewById(R.id.productPrice)

        // Fungsi untuk bind data
        fun bind(barang: DataBarang) {
            productTitle.text = barang.nama_barang
            productPrice.text = barang.harga_barang.toString()

            val imageUrl = BASE_URL + barang.foto_barang.substringAfterLast("images/")
            Glide.with(context)
                .load(imageUrl)
                .into(productImage)

            // Klik listener untuk item
            itemView.setOnClickListener {
                onItemClick(barang)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val inflater = LayoutInflater.from(context)
        val itemView = inflater.inflate(R.layout.item_produk, parent, false)
        return ProductViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val barang = barangList[position]
        holder.bind(barang)
    }

    override fun getItemCount(): Int = barangList.size
}
