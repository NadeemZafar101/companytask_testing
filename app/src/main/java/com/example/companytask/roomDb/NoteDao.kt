package com.example.companytask.roomDb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface NoteDao {
    @Insert
    suspend fun insertRecord(noteEntity: NoteEntity)

    @Update
    suspend fun updateRecord(noteEntity: NoteEntity)

    @Delete
    suspend fun deleteRecord(noteEntity: NoteEntity)

    @Query("SELECT * FROM note")
    fun getRecord(): LiveData<List<NoteEntity>>

}