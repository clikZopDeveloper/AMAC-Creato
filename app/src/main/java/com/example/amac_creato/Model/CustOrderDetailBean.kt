package com.example.amac_creato.Model


import com.google.gson.annotations.SerializedName

data class CustOrderDetailBean(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("msg")
    val msg: String
) {
    data class Data(
        @SerializedName("booked_qty")
        val bookedQty: Int,
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("is_order")
        val isOrder: Int,
        @SerializedName("is_packed")
        val isPacked: Int,
        @SerializedName("is_production")
        val isProduction: Int,
        @SerializedName("mst_id")
        val mstId: Int,
        @SerializedName("name")
        val name: String,
        @SerializedName("outward")
        val outward: Int,
        @SerializedName("pending_qty")
        val pendingQty: Int,
        @SerializedName("price")
        val price: String,
        @SerializedName("product_id")
        val productId: Int,
        @SerializedName("production_scan")
        val production_scan: Int,
        @SerializedName("production_barcode")
        val productionBarcode: Any,
        @SerializedName("qty")
        val qty: Int,
        @SerializedName("slot_id")
        val slotId: Int,
        @SerializedName("status")
        val status: String,
        @SerializedName("updated_at")
        val updatedAt: String,
        @SerializedName("variant")
        val variant: String
    )
}