package com.vijaydhoni.quicknotes.notes

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.material.snackbar.Snackbar
import com.vijaydhoni.quicknotes.R
import com.vijaydhoni.quicknotes.adapter.NotesRvAdapter
import com.vijaydhoni.quicknotes.authentication.GoogleAuthUiClient
import com.vijaydhoni.quicknotes.databinding.FragmentNotesBinding
import com.vijaydhoni.quicknotes.util.Constants
import com.vijaydhoni.quicknotes.util.setStatusBarColour
import com.vijaydhoni.quicknotes.viewmodels.NotesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class NotesFragment : Fragment() {
    private val binding by lazy {
        FragmentNotesBinding.inflate(layoutInflater)
    }

    @Inject
    lateinit var sharedPreferences: SharedPreferences
    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = requireContext().applicationContext,
            oneTapClient = Identity.getSignInClient(requireContext().applicationContext)
        )
    }

    private val viewModel: NotesViewModel by activityViewModels()
    private val notesRvAdapter: NotesRvAdapter by lazy {
        NotesRvAdapter()
    }

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
        setUpRv()
        getAllNotes()
        binding.addNoteBttn.setOnClickListener {
            findNavController().navigate(R.id.action_notesFragment_to_notesDetailFragment)
        }
        onRvitemClick()
        onDelete()
        logoutUserOption()
    }

    private fun logoutUserOption() {
        binding.logoutOption.setOnClickListener {
            val popupMenu = PopupMenu(
                context!!,
                binding.logoutOption
            )
            popupMenu.inflate(R.menu.menu_option)
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.logout_bttn -> {
                        logoutUser()
                        true
                    }

                    else -> {
                        false
                    }
                }
            }
            popupMenu.show()
        }
    }

    private fun logoutUser() {
        viewLifecycleOwner.lifecycleScope.launch {
            sharedPreferences.edit().putBoolean(Constants.USER_LOGIN, false).apply()
            googleAuthUiClient.signOut()
            findNavController().navigate(R.id.action_notesFragment_to_loginFragment)
        }

    }

    private fun onDelete() {
        val itemTouchHelperCallBack = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val positon = viewHolder.adapterPosition
                val note = notesRvAdapter.differ.currentList[positon]
                viewModel.deleteNote(note)
                view?.let {
                    Snackbar.make(it, "Deleted Successfully", Snackbar.LENGTH_LONG)
                        .apply {
                            setAction("Undo") {
                                viewModel.saveNote(note.title, note.description)
                            }
                        }.show()
                }
            }

        }

        ItemTouchHelper(itemTouchHelperCallBack).apply {
            attachToRecyclerView(binding.notesRv)
        }
    }

    private fun onRvitemClick() {
        notesRvAdapter.onClick = {
            val bundle = Bundle()
            bundle.putParcelable("note", it)
            findNavController().navigate(R.id.action_notesFragment_to_notesDetailFragment, bundle)
        }
    }

    private fun getAllNotes() {
        viewModel.getAllNotes().observe(viewLifecycleOwner) {
            Log.d("tag", it.toString())
            notesRvAdapter.differ.submitList(it)
        }
    }

    private fun setUpRv() {
        binding.notesRv.apply {
            adapter = notesRvAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

    }

}