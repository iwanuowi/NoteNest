package com.example.miniprojectnotebook.ui.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.miniprojectnotebook.R
import com.example.miniprojectnotebook.data.model.Notes
import com.example.miniprojectnotebook.data.repo.NotesRepo
import com.example.miniprojectnotebook.databinding.FragmentEditNoteBinding
import kotlinx.coroutines.launch

class EditNoteFragment : Fragment() {

    private var _binding: FragmentEditNoteBinding? = null
    private val binding get() = _binding!!

    private val repository = NotesRepo.getInstance()
    private var currentNote: Notes? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditNoteBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val noteId = arguments?.getString("noteId") ?: return
        val title = arguments?.getString("noteTitle") ?: ""
        val content = arguments?.getString("noteContent") ?: ""

        currentNote = Notes(
            id = noteId,
            title = title,
            content = content,
            colour = arguments?.getString("noteColour") ?: "#FFF3E0",
            date = arguments?.getLong("noteDate")
                ?: System.currentTimeMillis(),
            isTrashed = arguments?.getBoolean("noteIsTrashed") ?: false
        )

        binding.etEditTitle.setText(title)
        binding.etEditContent.setText(content)

        when (currentNote?.colour) {
            "#FFCDD2" -> binding.rbEditRed.isChecked = true
            "#BBDEFB" -> binding.rbEditBlue.isChecked = true
            "#C8E6C9" -> binding.rbEditGreen.isChecked = true
            "#FFF9C4" -> binding.rbEditYellow.isChecked = true
            "#F8BBD0" -> binding.rbEditPink.isChecked = true
        }

        binding.btnUpdateNote.setOnClickListener {
            val newTitle = binding.etEditTitle.text.toString().trim()
            val newContent = binding.etEditContent.text.toString().trim()

            if (newTitle.isEmpty()) {
                binding.etEditTitle.error = "Enter a note title"
                return@setOnClickListener
            }

            val oldNote = currentNote ?: return@setOnClickListener

            val newColour = when (
                binding.rgEditNoteColour.checkedRadioButtonId
            ) {
                R.id.rbEditRed -> "#FFCDD2"
                R.id.rbEditBlue -> "#BBDEFB"
                R.id.rbEditGreen -> "#C8E6C9"
                R.id.rbEditYellow -> "#FFF9C4"
                R.id.rbEditPink -> "#F8BBD0"
                else -> oldNote.colour
            }

            binding.btnUpdateNote.isEnabled = false

            viewLifecycleOwner.lifecycleScope.launch {
                try {
                    val updatedNote = oldNote.copy(
                        title = newTitle,
                        content = newContent,
                        colour = newColour,
                        date = oldNote.date
                    )

                    repository.updateNote(updatedNote)

                    Toast.makeText(
                        requireContext(),
                        "Note updated!",
                        Toast.LENGTH_SHORT
                    ).show()

                    val bundle = Bundle().apply {
                        putString("noteId", updatedNote.id)
                        putString("noteTitle", updatedNote.title)
                        putString("noteContent", updatedNote.content)
                        putString("noteColour", updatedNote.colour)
                        putLong("noteDate", updatedNote.date)
                        putBoolean("noteIsTrashed", updatedNote.isTrashed)
                    }

                    findNavController().previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("updatedNote", bundle)

                    findNavController().popBackStack()

                } catch (e: Exception) {
                    Toast.makeText(
                        requireContext(),
                        e.localizedMessage ?: "Failed to update note",
                        Toast.LENGTH_LONG
                    ).show()
                } finally {
                    _binding?.btnUpdateNote?.isEnabled = true
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
