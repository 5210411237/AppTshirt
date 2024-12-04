package com.example.apptshirt

data class LoginResponse(
    val message: String?,
    val data: LoginResponseData?
)

data class LoginResponseData(
    val id_user: String,
    val email_user: String,
    val nama_user: String,
    val token: String
)