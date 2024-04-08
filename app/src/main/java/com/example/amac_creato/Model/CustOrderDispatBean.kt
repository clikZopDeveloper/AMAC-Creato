package com.example.amac_creato.Model


import com.google.gson.annotations.SerializedName

data class CustOrderDispatBean(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("error")
    val error: Boolean, // false
    @SerializedName("msg")
    val msg: String // LoadSuccessfully.
) {
    data class Data(
        @SerializedName("address")
        val address: String, // 123/4
        @SerializedName("city")
        val city: String, // Panchkula
        @SerializedName("created_at")
        val createdAt: String, // 2024-03-2816:58:47
        @SerializedName("customer_name")
        val customerName: String, // Dummy
        @SerializedName("dispatcher_id")
        val dispatcherId: Int, // 3
        @SerializedName("expected_delivery_date")
        val expectedDeliveryDate: String, // 2024-03-31
        @SerializedName("expected_packing_date")
        val expectedPackingDate: String, // 2024-03-28
        @SerializedName("id")
        val id: Int, // 1
        @SerializedName("mobile")
        val mobile: String, // 9876543210
        @SerializedName("name")
        val name: String, // slot1
        @SerializedName("packer_id")
        val packerId: Int, // 1
        @SerializedName("pincode")
        val pincode: String, // 134113
        @SerializedName("remarks")
        val remarks: String, // Test
        @SerializedName("requirement_id")
        val requirementId: Int, // 1
        @SerializedName("state")
        val state: String // Haryana
    )
}