package com.example.miniprojectnotebook.data.repo

import com.example.miniprojectnotebook.data.model.Notes
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class NotesRepoImpl(
    private val db: FirebaseFirestore
): NotesRepo {

    private val notes = db.collection("notes")

    override suspend fun getNotes(): List<Notes> {
        return notes.get().await().documents.mapNotNull {
            it.toObject(Notes::class.java)?.copy(id = it.id)
        }
    }

    override suspend fun addNote(note: Notes) {
        val doc = notes.document()
        doc.set(note.copy(id = doc.id)).await()
    }

    override suspend fun updateNote(note: Notes) {
    notes.document(note.id).set(note).await()
    }

    override suspend fun deleteNote(note: Notes) {
        notes.document(note.id).delete().await()
    }

}