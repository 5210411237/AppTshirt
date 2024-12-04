package com.example.apptshirt

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.Path

interface ApiInterface {
    // Endpoint untuk mendapatkan daftar barang
    @GET("barang") // Sesuaikan endpoint API Anda
    fun getBarangList(): Call<List<DataBarang>>

// kalo di ganti barang zize muncul tetapi barang tidak  muncul, kalo product sebaliknya
    @GET("product/{id}")
    fun getProductById(@Path("id") id: Int): Call<DataBarang>

    // Endpoint untuk mendapatkan keranjang berdasarkan ID pengguna
    @GET("keranjang/{id_user}")
    fun getKeranjangByUser(@Path("id_user") userId: Int): Call<ModelKeranjang>

    // Endpoint untuk checkout
    @POST("checkout")
    fun createCheckout(@Body checkoutData: CheckoutData): Call<CheckoutResponse>

    // Endpoint untuk mendapatkan detail checkout berdasarkan ID checkout
    @GET("checkout/{id_checkout}")
    fun getCheckoutDetail(@Path("id_checkout") checkoutId: Int): Call<CheckoutData>

    // Endpoint untuk login
    @POST("user/login")
    fun login(@Body loginData: LoginData): Call<LoginResponse>

    // Endpoint untuk registrasi pengguna baru
    @POST("user/register")
    fun registerUser(@Body registerData: RegisterData): Call<RegisterResponse>

    @GET("keranjang/{id_user}")
    suspend fun getActiveCart(@Path("id_user") idUser: Int): Response<List<CartResponse>>

    @POST("keranjang")
    suspend fun addCart(@Body request: AddCartRequest): Response<CartResponse>

    @POST("detailkeranjang")
    suspend fun addCartDetail(@Body request: CartDetailRequest): Response<Void>

}
