package com.example.miniprojectnotebook.data.model

data class Notes (
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val colour: Int = 0,
    val delete: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val deletedAt: Long = 0L,
)