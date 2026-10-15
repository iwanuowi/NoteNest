package com.example.miniprojectnotebook.data.model

data class Notes (
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val colour: String = "#FFF3E0",
    val date: Long = System.currentTimeMillis(),
    @get:com.google.firebase.firestore.PropertyName("trashed")
    @set:com.google.firebase.firestore.PropertyName("trashed")
    var isTrashed: Boolean = false

)