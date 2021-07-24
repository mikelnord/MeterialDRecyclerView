package com.gb.projectrecyclerview.notedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gb.projectrecyclerview.database.NoteDatabaseDao


class NoteDetailViewModelFactory(
    private val noteKey: Long,
    private val dataSource: NoteDatabaseDao
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteDetailViewModel::class.java)) {
            return NoteDetailViewModel(noteKey, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}