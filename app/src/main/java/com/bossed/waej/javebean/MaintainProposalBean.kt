package com.bossed.waej.javebean

data class MaintainProposalBean(
    val code: Int,
    val `data`: MaintainData,
    val msg: String
)

data class MaintainData(
    val cateList: List<MaintainCate>,
    val itemList: List<MaintainItem>
)

data class MaintainCate(
    val createBy: String,
    val createTime: String,
    val id: Int,
    val imgUrl: String,
    val level: Int,
    val name: String,
    val parentId: Int,
    val remark: String,
    val sort: Int,
    val status: Int,
    val updateBy: String,
    val updateTime: String,
    var isSelect: Boolean
)

data class MaintainItem(
    val alias: String,
    val cateId: Int,
    var cateName: String,
    val createBy: String,
    val createTime: String,
    val id: Int,
    val marketPrice: Double,
    val mileageClean: Int,
    val mileageInspect: Int,
    val mileageReplace: Int,
    val name: String,
    val remark: String,
    val superCateId: Int,
    val updateBy: String,
    val updateTime: String,
    var isSelect: Boolean = false
)