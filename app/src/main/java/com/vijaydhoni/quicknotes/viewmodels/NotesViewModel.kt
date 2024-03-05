package com.vijaydhoni.quicknotes.viewmodels

import android.util.Log
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.vijaydhoni.quicknotes.data.Note
import com.vijaydhoni.quicknotes.data.NoteDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val notesDao: NoteDao,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _notesList: MutableLiveData<List<Note>> = MutableLiveData()
    val notesList: LiveData<List<Note>> get() = _notesList


    fun getAllNotes() = liveData {

        notesDao.getUserAllNotes(firebaseAuth.uid!!).collect {
            _notesList.postValue(it)
            Log.d("tag",it.toString())
            emit(it)
        }

    }


    fun saveNote(title: String, description: String) {
        viewModelScope.launch {
            val note = Note(
                id = 0,
                title = title,
                description = description,
                userId = firebaseAuth.uid!!
            )
            notesDao.insertNote(note)
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch {
            notesDao.updateNote(note)
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            notesDao.deleteNote(note)
        }
    }

}