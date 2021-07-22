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

    private suspend fun insert(note: Note) {
        database.insert(note)
    }


    private suspend fun update(note: Note) {
        database.update(note)
    }

    fun onClear() {
        viewModelScope.launch {
            clear()
            tonote.value = null
        }
    }

    suspend fun clear() {
        database.clear()
        _showSnackbarEvent.value = true
    }


    private var _showSnackbarEvent = MutableLiveData<Boolean>()

    val showSnackBarEvent: LiveData<Boolean>
        get() = _showSnackbarEvent

    fun doneShowingSnackbar() {
        _showSnackbarEvent.value = false
    }

    private val _navigateToNoteDataQuality = MutableLiveData<Long?>()
    val navigateToSleepDataQuality
        get() = _navigateToNoteDataQuality

    fun onNoteClicked(id: Long){
        _navigateToNoteDataQuality.value = id
    }

    fun onNoteDataQualityNavigated() {
        _navigateToNoteDataQuality.value = null
    }

}