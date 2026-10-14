package com.example.miniprojectnotebook.ui.manage.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.miniprojectnotebook.data.model.Notes
import com.example.miniprojectnotebook.data.repo.NotesRepo
import com.example.miniprojectnotebook.databinding.FragmentAddNoteBinding
import kotlinx.coroutines.launch

class AddNoteFragment : Fragment() {

    private var _binding: FragmentAddNoteBinding? = null
    private val binding get() = _binding!!

    private val repository = NotesRepo.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddNoteBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSaveNote.setOnClickListener {
            val title = binding.etNoteTitle.text.toString().trim()
            val content = binding.etNoteContent.text.toString().trim()

            if (title.isEmpty()) {
                binding.etNoteTitle.error = "Enter a note title"
                return@setOnClickListener
            }

            binding.btnSaveNote.isEnabled = false

            viewLifecycleOwner.lifecycleScope.launch {
                try {
                    val note = Notes(
                        title = title,
                        content = content
                    )

                    repository.addNote(note)

                    Toast.makeText(
                        requireContext(),
                        "Note saved!",
                        Toast.LENGTH_SHORT
                    ).show()

                    findNavController().popBackStack()

                } catch (e: Exception) {
                    Toast.makeText(
                        requireContext(),
                        e.localizedMessage ?: "Failed to save note",
                        Toast.LENGTH_LONG
                    ).show()
                } finally {
                    _binding?.btnSaveNote?.isEnabled = true
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
