package com.example.amac_creato.Model


import com.google.gson.annotations.SerializedName

data class SlotPackerListBean(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("msg")
    val msg: String
) {
    data class Data(
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("customer_name")
        val customerName: String,
        @SerializedName("dispatcher_id")
        val dispatcherId: Any,
        @SerializedName("expected_delivery_date")
        val expectedDeliveryDate: String,
        @SerializedName("expected_packing_date")
        val expectedPackingDate: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("name")
        val name: String,
        @SerializedName("packer_id")
        val packerId: Int,
        @SerializedName("remarks")
        val remarks: String,
        @SerializedName("requirement_id")
        val requirementId: Int
    )
}