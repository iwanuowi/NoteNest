package com.example.miniprojectnotebook.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.miniprojectnotebook.databinding.FragmentNoteDetailBinding
import com.example.miniprojectnotebook.R
import com.example.miniprojectnotebook.data.model.Notes
import com.example.miniprojectnotebook.data.repo.NotesRepo
import kotlinx.coroutines.launch

class NoteDetailFragment : Fragment() {

    private var _binding: FragmentNoteDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoteDetailBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val title = arguments?.getString("noteTitle") ?: ""
        val content = arguments?.getString("noteContent") ?: ""

        binding.tvDetailTitle.text = title
        binding.tvDetailContent.text = content

        findNavController()
            .currentBackStackEntry
            ?.savedStateHandle
            ?.getLiveData<Bundle>("updatedNote")
            ?.observe(viewLifecycleOwner) { bundle ->

                binding.tvDetailTitle.text =
                    bundle.getString("noteTitle", "")

                binding.tvDetailContent.text =
                    bundle.getString("noteContent", "")

                arguments?.putAll(bundle)

                findNavController()
                    .currentBackStackEntry
                    ?.savedStateHandle
                    ?.remove<Bundle>("updatedNote")
            }

        binding.btnEditNote.setOnClickListener {
            val bundle = Bundle().apply {
                putString("noteId", arguments?.getString("noteId"))
                putString("noteTitle", title)
                putString("noteContent", content)
                putString("noteColour", arguments?.getString("noteColour"))
                putLong(
                    "noteDate",
                    arguments?.getLong("noteDate") ?: System.currentTimeMillis()
                )
                putBoolean(
                    "noteIsTrashed",
                    arguments?.getBoolean("noteIsTrashed") ?: false
                )
            }
            findNavController().navigate(
                R.id.action_noteDetail_to_editNote,
                bundle
            )
        }

        binding.btnDeleteNote.setOnClickListener {
            val noteId = arguments?.getString("noteId") ?: return@setOnClickListener

            val note = Notes(
                id = noteId,
                title = arguments?.getString("noteTitle") ?: "",
                content = arguments?.getString("noteContent") ?: "",
                colour = arguments?.getString("noteColour") ?: "#FFF3E0",
                date = arguments?.getLong("noteDate")
                    ?: System.currentTimeMillis(),
                isTrashed = true
            )

            viewLifecycleOwner.lifecycleScope.launch {
                try {
                    NotesRepo.getInstance().updateNote(note)
                    findNavController().previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("noteDeleted", true)

                    Toast.makeText(
                        requireContext(),
                        "Note moved to Trash",
                        Toast.LENGTH_SHORT
                    ).show()

                    findNavController().popBackStack()

                } catch (e: Exception) {
                    Toast.makeText(
                        requireContext(),
                        e.localizedMessage ?: "Failed to move note to Trash",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
