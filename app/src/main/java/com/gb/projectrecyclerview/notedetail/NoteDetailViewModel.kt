package com.gb.projectrecyclerview.notedetail

import androidx.lifecycle.*
import com.gb.projectrecyclerview.database.Note
import com.gb.projectrecyclerview.database.NoteDatabaseDao
import kotlinx.coroutines.launch


class NoteDetailViewModel(
    private val noteKey: Long = 0L,
    dataSource: NoteDatabaseDao
) : ViewModel() {

    val database = dataSource


    var note = MutableLiveData<Note>()


    init {
        initializeNote()
    }

    private fun initializeNote() {
        viewModelScope.launch {
            note.value= database.getNoteWithId(noteKey)
        }
    }


    private suspend fun update(note: Note) {
         database.update(note)
    }

    fun onUpdateNote() {
        viewModelScope.launch {
            note.value?.let { update(it) }
        }
    }

    private val _navigateToNoteTracker = MutableLiveData<Boolean?>()
    val navigateToNotepTracker: LiveData<Boolean?>
        get() = _navigateToNoteTracker

    fun doneNavigating() {
        _navigateToNoteTracker.value = null
    }

    fun onClose() {
        onUpdateNote()
        _navigateToNoteTracker.value = true
    }

}