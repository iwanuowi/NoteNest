package com.example.miniprojectnotebook.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.miniprojectnotebook.R
import com.example.miniprojectnotebook.data.model.Notes
import com.example.miniprojectnotebook.databinding.ItemNoteBinding
import androidx.core.os.bundleOf

class NoteAdapter : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    private var notes = emptyList<Notes>()
    private var showRestoreButton = false
    private var showDeleteButton = false
    var onRestoreClick: ((Notes) -> Unit)? = null
    var onDeleteClick: ((Notes) -> Unit)? = null

    class NoteViewHolder(
        val binding: ItemNoteBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NoteViewHolder {
        val binding = ItemNoteBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: NoteViewHolder,
        position: Int
    ) {
        val note = notes[position]

        holder.binding.tvNoteTitle.text = note.title
        holder.binding.tvNoteContent.text = note.content

        holder.binding.btnRestoreNote.visibility =
            if (showRestoreButton) View.VISIBLE else View.GONE

        holder.binding.btnDeletePermanently.visibility =
            if (showDeleteButton) View.VISIBLE else View.GONE

        holder.binding.btnRestoreNote.setOnClickListener {
            onRestoreClick?.invoke(note)
        }

        holder.binding.btnDeletePermanently.setOnClickListener {
            onDeleteClick?.invoke(note)
        }

        holder.binding.root.setOnClickListener {
            val bundle = bundleOf(
                "noteId" to note.id,
                "noteTitle" to note.title,
                "noteContent" to note.content,
                "noteColour" to note.colour,
                "noteDate" to note.date,
                "noteIsTrashed" to note.isTrashed
            )

            it.findNavController().navigate(
                R.id.action_home_to_noteDetail,
                bundle
            )
        }
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    fun updateNotes(newNotes: List<Notes>) {
        notes = newNotes
        notifyDataSetChanged()
    }

    fun showRestoreButton(show: Boolean) {
        showRestoreButton = show
        notifyDataSetChanged()

    }  fun showDeletePermanentlyButton(show: Boolean) {
        showDeleteButton = show
        notifyDataSetChanged()
    }
}
