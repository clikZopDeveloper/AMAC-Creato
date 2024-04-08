package com.example.amac_creato.Model


import com.google.gson.annotations.SerializedName

data class CustOrderListBean(
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
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("customer_id")
        val customerId: Int,
        @SerializedName("customer_name")
        val customerName: String,
        @SerializedName("description")
        val description: String,
        @SerializedName("expected_delivery_date")
        val expectedDeliveryDate: String,
        @SerializedName("expected_packing_date")
        val expectedPackingDate: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("initiate")
        val initiate: Int,
        @SerializedName("mobile")
        val mobile: String,
        @SerializedName("packer_id")
        val packerId: Int,
        @SerializedName("pincode")
        val pincode: String,
        @SerializedName("state")
        val state: String,
        @SerializedName("status")
        val status: String,
        @SerializedName("updated_at")
        val updatedAt: String,
        @SerializedName("user_id")
        val userId: Int
    )
}