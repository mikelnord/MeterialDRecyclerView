package com.gb.projectrecyclerview.notetracker

import android.app.Application
import androidx.lifecycle.*
import com.gb.projectrecyclerview.database.Note
import com.gb.projectrecyclerview.database.NoteDatabaseDao
import kotlinx.coroutines.launch


class NoteTrackerViewModel(
    val database: NoteDatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    private var tonote = MutableLiveData<Note?>()
    val notes = database.getAllNotes()


    private suspend fun delete(note: Note) {
        database.delete(note)
    }

    fun onDeleteNote(note: Note) {
        viewModelScope.launch {
            delete(note)
        }
    }

        fun onClear() {
        viewModelScope.launch {
            clear()
            tonote.value = null
        }
    }

    suspend fun clear() {
        database.clear()
    }

    fun onInsertNote() {
        viewModelScope.launch {
            val newNote = Note()
            insert(newNote)
            tonote.value=database.getTonote()
            tonote.value.let { _navigateToNoteDetail.value = tonote.value?.noteId }
        }
    }

    private suspend fun insert(note: Note) {
        database.insert(note)
    }

    private val _navigateToNoteDetail = MutableLiveData<Long?>()
    val navigateToNoteDetail
        get() = _navigateToNoteDetail

    fun onNoteClicked(id: Long) {
        _navigateToNoteDetail.value = id
    }

    fun doneNavigating() {
        _navigateToNoteDetail.value = null
    }

}