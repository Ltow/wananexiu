package com.bossed.waej.javebean

data class NotepadBean(
    val code: Int,
    val msg: String,
    val rows: List<NotepadRow>,
    val total: Int
)

data class NotepadRow(
    val id: Int,
    val remindTime: String,
    val updateTime: String,
    val shopId: Int,
    val tenantId: Int,
    val title: String,
    val content: String,
    val userId: Int
)