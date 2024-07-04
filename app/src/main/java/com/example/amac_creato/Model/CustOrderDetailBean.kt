package com.example.amac_creato.Model


import com.google.gson.annotations.SerializedName

data class CustOrderDetailBean(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("error")
    val error: Boolean, // false
    @SerializedName("msg")
    val msg: String // Load Successfully.
) {
    data class Data(
        @SerializedName("article_code")
        val articleCode: String, // CB 8023
        @SerializedName("booked_qty")
        val bookedQty: Int, // 20
        @SerializedName("box_id")
        val boxId: Any, // null
        @SerializedName("created_at")
        val createdAt: String, // 2024-07-01 09:50:21
        @SerializedName("id")
        val id: Int, // 30
        @SerializedName("is_order")
        val isOrder: Int, // 1
        @SerializedName("is_packed")
        val isPacked: Int, // 0
        @SerializedName("is_production")
        val isProduction: Int, // 0
        @SerializedName("mst_id")
        val mstId: Int, // 6
        @SerializedName("name")
        val name: String, // Mocha Brown CB 8023(PR)
        @SerializedName("out_qty")
        val outQty: Int, // 10
        @SerializedName("outward")
        val outward: Int, // 1
        @SerializedName("packed_scan")
        val packedScan: Int, // 0
        @SerializedName("pending_qty")
        val pendingQty: Int, // 30
        @SerializedName("price")
        val price: String, // 0.00
        @SerializedName("product_id")
        val productId: Int, // 394
        @SerializedName("production_barcode")
        val productionBarcode: String, // AMACB_1060212
        @SerializedName("production_scan")
        val productionScan: Int, // 5
        @SerializedName("qty")
        val qty: Int, // 50
        @SerializedName("slot_id")
        val slotId: Int, // 0
        @SerializedName("status")
        val status: String, // pending
        @SerializedName("updated_at")
        val updatedAt: String, // 2024-07-01 10:33:34
        @SerializedName("variant")
        val variant: String // production
    )
}