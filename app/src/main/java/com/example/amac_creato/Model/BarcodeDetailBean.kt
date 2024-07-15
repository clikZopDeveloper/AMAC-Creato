package com.example.amac_creato.Model


import com.google.gson.annotations.SerializedName

data class BarcodeDetailBean(
    @SerializedName("data")
    val `data`: List<Any>,
    @SerializedName("error")
    val error: Boolean, // false
    @SerializedName("msg")
    val msg: String, // Save Successfully.
    @SerializedName("order_id")
    val orderId: String // 2
)