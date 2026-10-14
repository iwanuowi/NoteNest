package com.example.miniprojectnotebook.ui.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class RegisterViewModel: ViewModel() {
    private val auth = FirebaseAuth.getInstance()

    val registerResult = MutableLiveData<String>()

    fun register(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful)  {
                    registerResult.value = "SUCCESS"
                } else {
                    registerResult.value =
                        task.exception?.localizedMessage ?: "Registration failed"
                }
            }
    }
}