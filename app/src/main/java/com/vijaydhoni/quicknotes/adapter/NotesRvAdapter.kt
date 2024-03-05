package com.vijaydhoni.quicknotes.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.vijaydhoni.quicknotes.data.Note
import com.vijaydhoni.quicknotes.databinding.NotesRvItemBinding

class NotesRvAdapter : RecyclerView.Adapter<NotesRvAdapter.NotesRvViewholder>() {

    private val callback = object : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.description == newItem.description
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, callback)




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesRvViewholder {
        return NotesRvViewholder(
            NotesRvItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: NotesRvViewholder, position: Int) {
        val note = differ.currentList[position]
        holder.bind(note)
        holder.itemView.setOnClickListener {
            onClick?.invoke(note)
        }
    }

    var onClick: ((Note) -> Unit)? = null
    inner class NotesRvViewholder(private val binding: NotesRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(note: Note) {
            binding.notesTitle.text = note.title
            binding.notesDiscription.text = note.description
        }

    }


}