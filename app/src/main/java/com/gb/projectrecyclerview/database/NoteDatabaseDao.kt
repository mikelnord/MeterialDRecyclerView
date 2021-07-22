package com.gb.projectrecyclerview.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface NoteDatabaseDao {
    @Insert
    suspend fun insert(note: Note)

    @Update
    suspend fun update(note: Note)

    @Query("DELETE FROM note_table")
    suspend fun clear()

    @Query("SELECT * FROM note_table ORDER BY noteId DESC")
    fun getAllNotes(): LiveData<List<Note>>

    @Query("SELECT * from note_table WHERE noteId = :key")
    suspend fun get(key: Long): Note?

    @Query("SELECT * from note_table WHERE noteId = :key")
    fun getNightWithId(key: Long): LiveData<Note>

}
