
package com.example.miniprojectnotebook.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    val loginResult = MutableLiveData<String>()

    fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    loginResult.value = "SUCCESS"
                } else {
                    loginResult.value =
                        task.exception?.localizedMessage ?: "Login failed"
                }
            }
    }
}
