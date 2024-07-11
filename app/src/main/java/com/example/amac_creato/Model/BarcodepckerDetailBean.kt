package com.example.amac_creato.Model


import com.google.gson.annotations.SerializedName

data class BarcodepckerDetailBean(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("error")
    val error: Boolean, // false
    @SerializedName("msg")
    val msg: String // Save Successfully.
) {
    data class Data(
        @SerializedName("slot_id")
        val slotId: String // 2
    )
}