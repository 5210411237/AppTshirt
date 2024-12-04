import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ViewFlipper
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apptshirt.DataBarang
import com.example.apptshirt.DetailBarangActivity
import com.example.apptshirt.ProductAdapter
import com.example.apptshirt.R
import com.example.apptshirt.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private lateinit var productList: List<DataBarang>
    private lateinit var viewFlipper: ViewFlipper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        viewFlipper = view.findViewById(R.id.slider)

        viewFlipper.setOnClickListener {
            viewFlipper.showNext()
        }

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(context, 2) // Set the number of columns

        // Mengambil data dari API menggunakan Retrofit
        RetrofitClient.apiService.getBarangList().enqueue(object : Callback<List<DataBarang>> {
            override fun onResponse(
                call: Call<List<DataBarang>>,
                response: Response<List<DataBarang>>
            ) {
                if (response.isSuccessful) {
                    // Menyimpan data yang diterima ke dalam productList
                    productList = response.body() ?: emptyList()

                    // Inisialisasi adapter dan set data ke RecyclerView
                    productAdapter = ProductAdapter(requireContext(), productList) { product ->
                        val intent = Intent(requireContext(), DetailBarangActivity::class.java).apply {
                            putExtra("PRODUCT_ID", product.id_barang)
                            putExtra("NAMA_BARANG", product.nama_barang)
                            putExtra("HARGA_BARANG", product.harga_barang)
                            putExtra("DESKRIPSI_BARANG", product.deskripsi_barang)
                            putExtra("STOK_BARANG", product.stok_barang)
                            putExtra("FOTO_BARANG", product.foto_barang)
                            putExtra("SIZE_BARANG", product.size)
                        }
                        startActivity(intent)
                    }
                    recyclerView.adapter = productAdapter

                }
            }

            override fun onFailure(call: Call<List<DataBarang>>, t: Throwable) {
                // Tangani kegagalan request API (misalnya, koneksi gagal)
                t.printStackTrace()
            }
        })

        return view
    }
}


