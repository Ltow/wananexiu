package com.bossed.waej.javebean

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName


data class RecommendResponse(
    @SerializedName("code")
    val code: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("outStr")
    val outStr: String,
    @SerializedName("result")
    val result: RecommendResult?
)

data class RecommendResult(
    @SerializedName("ds1")
    val ds1: List<RecommendDs1>?,
    @SerializedName("ds2")
    val ds2: List<RecommendDs2>
)

data class RecommendDs1(
    @SerializedName("vehicleId")
    val vehicleId: String,
    @SerializedName("保养项目ID")
    val 保养项目ID: String,
    @SerializedName("保养项目名称")
    val 保养项目名称: String,
    @SerializedName("保养项目类型名称")
    val 保养项目类型名称: String,
    @SerializedName("工时费")
    val 工时费: String,
    @SerializedName("更换标识")
    val 更换标识: String,
    @SerializedName("获取")
    val 获取: String,
    @SerializedName("isSel")
    var isSel: Boolean = true,
    @SerializedName("isJYitem")
    var isJYItem: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readByte() != 0.toByte(),
        parcel.readString().toString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(vehicleId)
        parcel.writeString(保养项目ID)
        parcel.writeString(保养项目名称)
        parcel.writeString(保养项目类型名称)
        parcel.writeString(工时费)
        parcel.writeString(更换标识)
        parcel.writeString(获取)
        parcel.writeByte(if (isSel) 1 else 0)
        parcel.writeString(isJYItem)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RecommendDs1> {
        override fun createFromParcel(parcel: Parcel): RecommendDs1 {
            return RecommendDs1(parcel)
        }

        override fun newArray(size: Int): Array<RecommendDs1?> {
            return arrayOfNulls(size)
        }
    }
}

data class RecommendDs2(
    @SerializedName("checkList")
    val checkList: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("maintMileageTitle")
    val maintMileageTitle: String,
    @SerializedName("mustList")
    val mustList: String,
    @SerializedName("repairPrice")
    val repairPrice: String,
    @SerializedName("stdMaintId")
    val stdMaintId: String,
    @SerializedName("stdMaintName")
    val stdMaintName: String,
    @SerializedName("stdMaintTypeName")
    val stdMaintTypeName: String,
    @SerializedName("vehicleId")
    val vehicleId: String
)