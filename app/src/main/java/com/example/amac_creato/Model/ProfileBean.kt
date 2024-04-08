package com.example.amac_creato.Model


import com.google.gson.annotations.SerializedName

data class ProfileBean(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("error")
    val error: Boolean, // false
    @SerializedName("msg")
    val msg: String // Load Successfully
) {
    data class Data(
        @SerializedName("created_at")
        val createdAt: String, // 2024-03-28 13:51:03
        @SerializedName("id")
        val id: Int, // 3
        @SerializedName("last_login")
        val lastLogin: String, // 2024-03-29 12:31:59
        @SerializedName("mobile")
        val mobile: String, // 9999999999
        @SerializedName("name")
        val name: String, // vaibhav
        @SerializedName("password")
        val password: Any, // null
        @SerializedName("t_password")
        val tPassword: String, // 987654
        @SerializedName("token")
        val token: String, // P4y64QLcx7M8
        @SerializedName("updated_at")
        val updatedAt: String, // 2024-03-29 12:31:59
        @SerializedName("user_type")
        val userType: String // dispatcher
    )
}