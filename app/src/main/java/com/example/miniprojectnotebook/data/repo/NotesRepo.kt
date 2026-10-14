package com.example.miniprojectnotebook.data.repo

import com.example.miniprojectnotebook.data.model.Notes
import com.google.firebase.firestore.FirebaseFirestore

interface NotesRepo {
    suspend fun getNotes(): List<Notes>

    suspend fun addNote(note: Notes)

    suspend fun updateNote(note: Notes)

    suspend fun deleteNote(note: Notes)

    companion object {
        fun getInstance() = NotesRepoImpl(
            FirebaseFirestore.getInstance()
        )
    }
}