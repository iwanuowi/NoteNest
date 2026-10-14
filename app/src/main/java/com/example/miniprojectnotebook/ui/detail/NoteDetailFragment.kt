package com.example.miniprojectnotebook.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.miniprojectnotebook.databinding.FragmentNoteDetailBinding

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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
