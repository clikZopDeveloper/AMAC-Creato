package com.example.amac_creato.Model


import com.google.gson.annotations.SerializedName

data class BoxListBean(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("msg")
    val msg: String
) {
    data class Data(
        @SerializedName("barcode")
        val barcode: String,
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("remarks")
        val remarks: String,
        @SerializedName("slot_id")
        val slotId: Int,
        @SerializedName("updated_at")
        val updatedAt: String ,
        @SerializedName("is_dispatched")
        val isDispatched: Int
    )
}