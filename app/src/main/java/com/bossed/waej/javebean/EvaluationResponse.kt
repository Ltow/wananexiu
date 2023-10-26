package com.bossed.waej.javebean

import com.google.gson.annotations.SerializedName


data class EvaluationResponse(
    @SerializedName("code")
    var code: Int?,
    @SerializedName("msg")
    var msg: String?,
    @SerializedName("rows")
    var rows: List<EvaluationRow>,
    @SerializedName("total")
    var total: Int?
)

data class EvaluationRow(
    @SerializedName("attitudeScore")
    var attitudeScore: String?,
    @SerializedName("content")
    var content: String?,
    @SerializedName("environmentScore")
    var environmentScore: String?,
    @SerializedName("id")
    var id: String?,
    @SerializedName("imgs")
    var imgs: String?,
    @SerializedName("isShow")
    var isShow: String?,
    @SerializedName("nickName")
    var nickName: String?,
    @SerializedName("orderId")
    var orderId: String?,
    @SerializedName("packageId")
    var packageId: Any?,
    @SerializedName("packageName")
    var packageName: String?,
    @SerializedName("parentId")
    var parentId: String?,
    @SerializedName("promType")
    var promType: String?,
    @SerializedName("prouctId")
    var prouctId: Any?,
    @SerializedName("replayCount")
    var replayCount: String?,
    @SerializedName("userId")
    var userId: String?,
    @SerializedName("userImage")
    var userImage: String?,
    @SerializedName("createTime")
    var createTime: String?,
    @SerializedName("userSocre")
    var userSocre: String?,
    @SerializedName("replyList")
    var replyList: List<ReplyList>
)

data class ReplyList(
    @SerializedName("attitudeScore")
    var attitudeScore: String?,
    @SerializedName("content")
    var content: String?,
    @SerializedName("createTime")
    var createTime: String?,
    @SerializedName("environmentScore")
    var environmentScore: String?,
    @SerializedName("id")
    var id: String?,
    @SerializedName("imgs")
    var imgs: Any?,
    @SerializedName("isShow")
    var isShow: String?,
    @SerializedName("nickName")
    var nickName: String?,
    @SerializedName("orderId")
    var orderId: Any?,
    @SerializedName("packageId")
    var packageId: Any?,
    @SerializedName("packageName")
    var packageName: Any?,
    @SerializedName("parentId")
    var parentId: String?,
    @SerializedName("promType")
    var promType: String?,
    @SerializedName("prouctId")
    var prouctId: Any?,
    @SerializedName("replayCount")
    var replayCount: String?,
    @SerializedName("replyList")
    var replyList: Any?,
    @SerializedName("userId")
    var userId: String?,
    @SerializedName("userImage")
    var userImage: String?,
    @SerializedName("userSocre")
    var userSocre: String?
) {
    constructor() : this(
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null
    )
}