package com.example.amac_creato.Model


import com.google.gson.annotations.SerializedName

data class GetCustomersBean(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("error")
    val error: Boolean, // false
    @SerializedName("msg")
    val msg: String // LoadSuccessfully.
) {
    data class Data(
        @SerializedName("address")
        val address: String, // addresse
        @SerializedName("city")
        val city: String, // Hassan
        @SerializedName("company_id")
        val companyId: Int, // 0
        @SerializedName("country")
        val country: String, // India
        @SerializedName("created_at")
        val createdAt: String, // 2024-03-1512:07:29
        @SerializedName("gst")
        val gst: String, // GST123456
        @SerializedName("id")
        val id: Int, // 2
        @SerializedName("mobile")
        val mobile: String, // 9216072804
        @SerializedName("name")
        val name: String, // vaibhav
        @SerializedName("pincode")
        val pincode: String, // 148101
        @SerializedName("state")
        val state: String, // Karnataka
        @SerializedName("updated_at")
        val updatedAt: String, // 2024-03-1512:07:40
        @SerializedName("user_type")
        val userType: String // Customer
    )
}