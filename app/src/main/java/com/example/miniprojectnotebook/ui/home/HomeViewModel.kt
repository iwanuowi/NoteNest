package com.example.miniprojectnotebook.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.miniprojectnotebook.data.model.Notes
import com.example.miniprojectnotebook.data.repo.NotesRepo
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel() {
    private val repository = NotesRepo.getInstance()

    val notes = MutableLiveData<List<Notes>>()
    val errorMsg = MutableLiveData<String>()

    fun loadNotes() {
        viewModelScope.launch {
            try {
                notes.value = repository.getNotes()
            } catch (e: Exception) {
                errorMsg.value = e.localizedMessage ?: "Failed to load notes"
            }
        }
    }
}