package com.example.miniprojectnotebook.data.model

data class Notes (
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val colour: String = "#FFF3E0",
    val date: Long = System.currentTimeMillis(),
    val isTrashed: Boolean = false
)