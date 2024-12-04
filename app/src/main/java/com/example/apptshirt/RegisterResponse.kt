package com.example.apptshirt

data class RegisterResponse(
    val message: String,
    val user: UserData
)


data class UserData(
    val id_user: Int,
    val nama_user: String,
    val email_user: String,
    val password_user: String,
    val no_handphone_user: String,
    val alamat_user: String
)