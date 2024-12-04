import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.apptshirt.KeranjangSingleton
import com.example.apptshirt.ModelKeranjang
import com.example.apptshirt.R

class KeranjangAdapter(private val keranjangList: MutableList<ModelKeranjang>) : RecyclerView.Adapter<KeranjangAdapter.KeranjangViewHolder>() {

    // ViewHolder untuk menampilkan item keranjang
    class KeranjangViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgProduk: ImageView = itemView.findViewById(R.id.imgProduk)
        val tvNamaProduk: TextView = itemView.findViewById(R.id.keranjang_nama_produk)
        val tvUkuran: TextView = itemView.findViewById(R.id.Ukurannya)
        val tvJumlah: TextView = itemView.findViewById(R.id.JumlahProdukKeranjang)
        val tvHarga: TextView = itemView.findViewById(R.id.harga_produk)
        val btnDelete: ImageView = itemView.findViewById(R.id.delete_icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KeranjangViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_keranjang, parent, false)
        return KeranjangViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: KeranjangViewHolder, position: Int) {
        val currentItem = keranjangList.getOrNull(position) ?: return
        val product = currentItem.product ?: return

        // Mengatur data produk pada tampilan
        holder.tvNamaProduk.text = product.title ?: "Nama Produk Tidak Tersedia"
        holder.tvUkuran.text = "Ukuran: ${product.size ?: "Tidak Tersedia"}"
        holder.tvJumlah.text = "x${currentItem.quantity}"

        // Menangani harga total
        val cleanPrice = product.price.replace("Rp", "").replace(",", "").trim()
        val totalPrice = (cleanPrice.toIntOrNull() ?: 0) * currentItem.quantity
        holder.tvHarga.text = "Rp $totalPrice"

        // Gambar produk - Menggunakan Glide untuk memuat gambar dari URL
        Glide.with(holder.itemView.context)
            .load(product.imageResource) // imageResource here should be the URL
            .into(holder.imgProduk)

        // Tombol hapus
        holder.btnDelete.setOnClickListener {
            if (position >= 0 && position < keranjangList.size) {
                // Menghapus item dari keranjang Singleton
                KeranjangSingleton.removeItem(currentItem) // Menghapus item tertentu dari Singleton
                keranjangList.removeAt(position)           // Menghapus item dari list lokal
                notifyItemRemoved(position)                // Mengupdate RecyclerView
            } else {
                Log.e("KeranjangAdapter", "Invalid position: $position")
            }
        }
    }

    override fun getItemCount(): Int {
        return keranjangList.size
    }
}
