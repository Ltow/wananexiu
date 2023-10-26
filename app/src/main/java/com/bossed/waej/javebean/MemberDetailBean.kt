package com.bossed.waej.javebean

data class MemberDetailBean(val name: String, val list: MutableList<MemberDetailItemBean>?)
data class MemberDetailItemBean(val name: String)
