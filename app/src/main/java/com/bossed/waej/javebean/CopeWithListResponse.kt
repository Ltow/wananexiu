package com.bossed.waej.javebean
import com.google.gson.annotations.SerializedName


data class CopeWithListResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: CopeWithListData,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("succ")
    val succ: Boolean
)

data class CopeWithListData(
    @SerializedName("balance")
    val balance: String,
    @SerializedName("discount")
    val discount: String,
    @SerializedName("id")
    val id: Any,
    @SerializedName("ledgerList")
    val ledgerList: List<Ledger>?,
    @SerializedName("moneyAdd")
    val moneyAdd: String,
    @SerializedName("moneyOwe")
    val moneyOwe: String,
    @SerializedName("moneyPay")
    val moneyPay: String,
    @SerializedName("name")
    val name: Any,
    @SerializedName("phone")
    val phone: Any,
    @SerializedName("supplierName")
    val supplierName: Any
)

data class Ledger(
    @SerializedName("balance")
    val balance: String,
    @SerializedName("discount")
    val discount: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("ledgerList")
    val ledgerList: Any,
    @SerializedName("moneyAdd")
    val moneyAdd: String,
    @SerializedName("moneyOwe")
    val moneyOwe: String,
    @SerializedName("moneyPay")
    val moneyPay: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("businessOrderSn")
    val businessOrderSn: String,
    @SerializedName("supplierName")
    val supplierName: String
)