package com.example.miniprojectnotebook

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.miniprojectnotebook.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.firestore.firestore

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = Firebase.firestore

        // Create a new note
        val note = hashMapOf(
            "title" to "My First Note",
            "content" to "Remember to finish my Android project.",
            "createdAt" to System.currentTimeMillis()
        )

        // Save the note with a generated ID
        db.collection("notes")
            .add(note)
            .addOnSuccessListener { documentReference ->
                Log.d(
                    "firestore",
                    "Note added with ID: ${documentReference.id}"
                )
            }
            .addOnFailureListener { e ->
                Log.e("firestore", "Error adding note", e)
            }
        db.collection("notes").addSnapshotListener(MetadataChanges.INCLUDE) { snap, e ->
            if (e != null) { Log.e("FS", "LISTEN FAILED", e); return@addSnapshotListener }
            Log.d("FS", "docs=${snap?.size()} fromCache=${snap?.metadata?.isFromCache} pending=${snap?.metadata?.hasPendingWrites()}")
        }
    }
}