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
import com.example.miniprojectnotebook.databinding.FragmentHomeBinding
import com.example.miniprojectnotebook.ui.adapter.NoteAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var noteAdapter: NoteAdapter

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

        binding.rvNotes.layoutManager = LinearLayoutManager(requireContext())
        binding.rvNotes.adapter = noteAdapter

        viewModel.notes.observe(viewLifecycleOwner) { notes ->
            val activeNotes = notes.filter { !it.isTrashed }

            noteAdapter.updateNotes(activeNotes)

            binding.tvEmpty.visibility =
                if (activeNotes.isEmpty()) View.VISIBLE else View.GONE
        }

        viewModel.errorMsg.observe(viewLifecycleOwner) { message ->
            if (!message.isNullOrEmpty()) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnAddNote.setOnClickListener {
            findNavController().navigate(
                R.id.action_home_to_addNote
            )
        }

        binding.btnTrash.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_trash)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
