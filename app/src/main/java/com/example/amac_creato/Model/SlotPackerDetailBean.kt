package com.example.amac_creato.Model


import com.google.gson.annotations.SerializedName

data class SlotPackerDetailBean(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("msg")
    val msg: String
) {

    data class Data(
        @SerializedName("hardware_products")
        val hardwareProducts: List<HardwareProduct>,
        @SerializedName("production_products")
        val productionProducts: List<ProductionProduct>
    ) {

        data class HardwareProduct(
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
            @SerializedName("packed_scan")
            val packedScan: Int,
            @SerializedName("is_production")
            val isProduction: Int,
            @SerializedName("mst_id")
            val mstId: Int,
            @SerializedName("outward")
            val outward: Int,
            @SerializedName("pending_qty")
            val pendingQty: Int,
            @SerializedName("price")
            val price: String,
            @SerializedName("product")
            val product: String,
            @SerializedName("product_id")
            val productId: Int,
            @SerializedName("production_barcode")
            val productionBarcode: Any,
            @SerializedName("qty")
            val qty: Int,
            @SerializedName("slot_id")
            val slotId: Int,
            @SerializedName("status")
            val status: Any,
            @SerializedName("updated_at")
            val updatedAt: String,
            @SerializedName("variant")
            val variant: String
        )
        data class ProductionProduct(
            @SerializedName("barcode")
            val barcode: String,
            @SerializedName("created_at")
            val createdAt: String,
            @SerializedName("f_size")
            val fSize: String,
            @SerializedName("id")
            val id: Int,
            @SerializedName("is_packed")
            val isPacked: Int,
            @SerializedName("part_name")
            val partName: String,
            @SerializedName("po_and_article")
            val poAndArticle: String,
            @SerializedName("post_number")
            val postNumber: String,
            @SerializedName("req_mst_id")
            val reqMstId: Int,
            @SerializedName("slot_id")
            val slotId: Int,
            @SerializedName("updated_at")
            val updatedAt: String,
            @SerializedName("variant")
            val variant: String
        )

    }
}