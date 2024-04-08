package com.example.amac_creato.Model


import com.google.gson.annotations.SerializedName

data class GetCustomerBean(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("msg")
    val msg: String
) {
    data class Data(
        @SerializedName("address")
        val address: String,
        @SerializedName("city")
        val city: String,
        @SerializedName("company_id")
        val companyId: Int,
        @SerializedName("country")
        val country: String,
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("gst")
        val gst: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("mobile")
        val mobile: String,
        @SerializedName("name")
        val name: String,
        @SerializedName("pincode")
        val pincode: String,
        @SerializedName("state")
        val state: String,
        @SerializedName("updated_at")
        val updatedAt: Any,
        @SerializedName("user_type")
        val userType: String
    )
}