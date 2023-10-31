package com.example.companytask.repository

import androidx.lifecycle.LiveData
import com.example.companytask.roomDb.NoteDao
import com.example.companytask.roomDb.NoteEntity

class NoteRepository(private val noteDao: NoteDao){

    fun getRecord(): LiveData<List<NoteEntity>>{
        return  noteDao.getRecord()
    }
   suspend fun insertRecord(noteEntity: NoteEntity){
      return noteDao.insertRecord(noteEntity)
    }
    suspend fun updateRecord(noteEntity: NoteEntity){
        return noteDao.updateRecord(noteEntity)
    }
    suspend fun deleteRecord(noteEntity: NoteEntity){
        return noteDao.deleteRecord(noteEntity)
    }


}