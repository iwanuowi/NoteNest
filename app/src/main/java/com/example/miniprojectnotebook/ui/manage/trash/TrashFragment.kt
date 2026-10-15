
package com.example.miniprojectnotebook.ui.trash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.miniprojectnotebook.data.repo.NotesRepo
import com.example.miniprojectnotebook.databinding.FragmentTrashBinding
import com.example.miniprojectnotebook.ui.adapter.NoteAdapter
import kotlinx.coroutines.launch

class TrashFragment : Fragment() {

    private var _binding: FragmentTrashBinding? = null
    private val binding get() = _binding!!

    private val repository = NotesRepo.getInstance()
    private lateinit var noteAdapter: NoteAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrashBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        noteAdapter = NoteAdapter()
        noteAdapter.showRestoreButton(true)


        noteAdapter.onRestoreClick = { note ->
            viewLifecycleOwner.lifecycleScope.launch {
                try {
                    val restoredNote = note.copy(isTrashed = false)

                    repository.updateNote(restoredNote)

                    Toast.makeText(
                        requireContext(),
                        "Note restored!",
                        Toast.LENGTH_SHORT
                    ).show()

                    val trashedNotes = repository.getTrashedNotes()
                    noteAdapter.updateNotes(trashedNotes)

                    binding.tvTrashEmpty.visibility =
                        if (trashedNotes.isEmpty()) View.VISIBLE else View.GONE

                } catch (e: Exception) {
                    Toast.makeText(
                        requireContext(),
                        e.localizedMessage ?: "Failed to restore note",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }


        binding.rvTrashNotes.layoutManager =
            LinearLayoutManager(requireContext())

        binding.rvTrashNotes.adapter = noteAdapter

        binding.btnBackHome.setOnClickListener {
          findNavController().popBackStack()
        }

        viewLifecycleOwner.lifecycleScope.launch{
            try {
                val trashedNotes = repository.getTrashedNotes()
                noteAdapter.updateNotes(trashedNotes)
                binding.tvTrashEmpty.visibility =
                    if (trashedNotes.isEmpty()) View.VISIBLE else View.GONE
            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    e.localizedMessage ?: "Failed to load Trash",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
