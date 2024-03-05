package com.vijaydhoni.quicknotes.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.vijaydhoni.quicknotes.R
import com.vijaydhoni.quicknotes.data.Note
import com.vijaydhoni.quicknotes.databinding.FragmentNotesDetailBinding
import com.vijaydhoni.quicknotes.util.setStatusBarColour
import com.vijaydhoni.quicknotes.viewmodels.NotesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotesDetailFragment : Fragment() {
    private val binding: FragmentNotesDetailBinding by lazy {
        FragmentNotesDetailBinding.inflate(layoutInflater)
    }

    private val viewModel: NotesViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarColour(activity as AppCompatActivity, R.color.notes_screen_back)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args by navArgs<NotesDetailFragmentArgs>()
        val note = args.note
        updateUi(note)
        binding.addNoteBttn.setOnClickListener {
            saveNotes(note)
        }
        binding.backBttn.setOnClickListener {
            findNavController().navigateUp()
        }

    }

    private fun updateUi(note: Note?) {
        if (note?.title?.isNotEmpty() == true && note.description.isNotEmpty()) {
            binding.noteTitle.setText(note.title)
            binding.notesDiscr.setText(note.description)
        }
    }

    private fun saveNotes(note: Note?) {
        if (note == null) {
            val notesTitle = binding.noteTitle.text.toString()
            val discription = binding.notesDiscr.text.toString()
            if (notesTitle.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Note title cannot be empty",
                    Toast.LENGTH_LONG
                )
                    .show()
                return
            }
            if (discription.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Note description cannot be empty",
                    Toast.LENGTH_LONG
                )
                    .show()
                return
            }
            viewModel.saveNote(notesTitle, discription)
            Toast.makeText(
                requireContext(),
                "Saved",
                Toast.LENGTH_LONG
            )
                .show()
            findNavController().navigateUp()
        } else {
            val notesTitle = binding.noteTitle.text.toString()
            val discription = binding.notesDiscr.text.toString()
            if (notesTitle.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Note title cannot be empty",
                    Toast.LENGTH_LONG
                )
                    .show()
                return
            }
            if (discription.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Note description cannot be empty",
                    Toast.LENGTH_LONG
                )
                    .show()
                return
            }

            val updatedNote = Note(
                id = note.id,
                title = notesTitle,
                description = discription,
                userId = note.userId
            )
            viewModel.updateNote(updatedNote)
            Toast.makeText(
                requireContext(),
                "Note Updated",
                Toast.LENGTH_LONG
            )
                .show()
            findNavController().navigateUp()
        }
    }

}