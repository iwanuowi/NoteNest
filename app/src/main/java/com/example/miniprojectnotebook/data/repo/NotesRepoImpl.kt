package com.example.miniprojectnotebook.data.repo

import com.example.miniprojectnotebook.data.model.Notes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class NotesRepoImpl(
    private val db: FirebaseFirestore
) : NotesRepo {

    private fun getNotesCollection() =
        db.collection("users")
            .document(
                FirebaseAuth.getInstance().currentUser?.uid
                    ?: throw IllegalStateException("User is not logged in")
            )
            .collection("notes")

    override suspend fun getNotes(): List<Notes> {
        return getNotesCollection().get().await().documents.mapNotNull {
                it.toObject(Notes::class.java)?.copy(id = it.id)
            }
    }

    override suspend fun addNote(note: Notes) {
        val document = getNotesCollection().document()
        document.set(note.copy(id = document.id)).await()
    }

    override suspend fun updateNote(note: Notes) {
        getNotesCollection().document(note.id).set(note).await()
    }

    override suspend fun deleteNote(note: Notes) {
        getNotesCollection().document(note.id).delete().await()
    }
}
