package com.example.companytask.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.companytask.repository.NoteRepository
import com.example.companytask.roomDb.NoteEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val noteRepository: NoteRepository ):ViewModel() {
    fun getRecords(): LiveData<List<NoteEntity>>{
        return noteRepository.getRecord()
    }
    fun insertRecord(noteEntity: NoteEntity){
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.insertRecord(noteEntity)
        }
    }
    fun updateRecord(noteEntity: NoteEntity){
        viewModelScope.launch(Dispatchers.IO){
            noteRepository.updateRecord(noteEntity)
        }
    }
    fun deleteRecord(noteEntity: NoteEntity){
        viewModelScope.launch(Dispatchers.Default) {
            noteRepository.deleteRecord(noteEntity)
        }
    }

}