package com.bossed.waej.javebean

import com.google.gson.annotations.SerializedName


data class SupplierInfoResponse(
    @SerializedName("code")
    var code: Int,
    @SerializedName("data")
    var `data`: List<SupplierInfoData>,
    @SerializedName("msg")
    var msg: String,
    @SerializedName("succ")
    var succ: Boolean
)

data class SupplierInfoData(
    @SerializedName("address")
    var address: String,
    @SerializedName("contacts")
    var contacts: String,
    @SerializedName("contacts2")
    var contacts2: String,
    @SerializedName("contacts2Phone")
    var contacts2Phone: String,
    @SerializedName("contactsPhone")
    var contactsPhone: String,
    @SerializedName("createBy")
    var createBy: String,
    @SerializedName("createTime")
    var createTime: String,
    @SerializedName("id")
    var id: String,
    @SerializedName("name")
    var name: String,
    @SerializedName("remark")
    var remark: String,
    @SerializedName("shopId")
    var shopId: Int?,
    @SerializedName("status")
    var status: String,
    @SerializedName("supplierPayment")
    var supplierPayment: SupplierPayment?,
    @SerializedName("tag")
    var tag: String,
    @SerializedName("updateBy")
    var updateBy: String,
    @SerializedName("updateTime")
    var updateTime: String
) {
    constructor() : this("", "", "", "", "", "", "", "", "", "", null, "", null, "", "", "")
}
