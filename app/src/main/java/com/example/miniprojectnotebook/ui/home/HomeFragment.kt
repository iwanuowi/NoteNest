package com.example.miniprojectnotebook.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.miniprojectnotebook.R
import com.example.miniprojectnotebook.data.model.Notes
import com.example.miniprojectnotebook.databinding.FragmentHomeBinding
import com.example.miniprojectnotebook.ui.adapter.NoteAdapter
import android.text.Editable
import android.text.TextWatcher
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.google.firebase.auth.FirebaseAuth

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var noteAdapter: NoteAdapter
    private var  allNotes = emptyList<Notes>()
    private var selectedColour = "All colours"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        noteAdapter = NoteAdapter()

        val colourOptions = arrayOf(
            "All colours",
            "Red",
            "Blue",
            "Green",
            "Yellow",
            "Pink"
        )

        val spinnerAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            colourOptions
        )

        spinnerAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        binding.spinnerSortColour.adapter = spinnerAdapter

        binding.spinnerSortColour.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedColour = colourOptions[position]
                    searchNotes(binding.etSearch.text.toString())
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }

        binding.rvNotes.layoutManager = LinearLayoutManager(requireContext())
        binding.rvNotes.adapter = noteAdapter

        viewModel.notes.observe(viewLifecycleOwner) { notes ->
            allNotes = notes.filter { !it.isTrashed }
            searchNotes(binding.etSearch.text.toString())
        }

        viewModel.errorMsg.observe(viewLifecycleOwner) { message ->
            if (!message.isNullOrEmpty()) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }

        binding.etSearch.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                searchNotes(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding.btnAddNote.setOnClickListener {
            findNavController().navigate(
                R.id.action_home_to_addNote
            )
        }

        binding.btnTrash.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_trash)
        }

        binding.btnLogout.setOnClickListener {
            androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("Log out?")
                .setMessage("Are you sure you want to log out?")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Logout") { _, _ ->
                    FirebaseAuth.getInstance().signOut()

                    findNavController().navigate(
                        R.id.action_home_to_login
                    )
                }
                .show()
        }
        findNavController()
            .currentBackStackEntry
            ?.savedStateHandle
            ?.getLiveData<Boolean>("noteDeleted")
            ?.observe(viewLifecycleOwner) { deleted ->

                if (deleted) {
                    viewModel.loadNotes()

                    findNavController()
                        .currentBackStackEntry
                        ?.savedStateHandle
                        ?.remove<Boolean>("noteDeleted")
                }
            }

        viewModel.loadNotes()
    }

    private fun searchNotes(query: String) {
        val filteredNotes = allNotes.filter { note ->

            val matchesSearch =
                note.title.contains(query, ignoreCase = true) ||
                        note.content.contains(query, ignoreCase = true)

            val matchesColour = when (selectedColour) {
                "Red" -> note.colour == "#FFCDD2"
                "Blue" -> note.colour == "#BBDEFB"
                "Green" -> note.colour == "#C8E6C9"
                "Yellow" -> note.colour == "#FFF9C4"
                "Pink" -> note.colour == "#F8BBD0"
                else -> true
            }

            matchesSearch && matchesColour
        }

        noteAdapter.updateNotes(filteredNotes)

        binding.tvEmpty.text = when {
            filteredNotes.isNotEmpty() -> ""
            selectedColour != "All colours" ->
                "No $selectedColour notes found."
            query.isNotEmpty() ->
                "No matching notes found."
            else ->
                "No notes yet.\nCreate your first note!"
        }

        binding.tvEmpty.visibility =
            if (filteredNotes.isEmpty()) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
