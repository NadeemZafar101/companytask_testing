package com.example.companytask.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.companytask.repository.NoteRepository

class MainViewModelFactory(private val noteRepository: NoteRepository):ViewModelProvider.Factory {
    override fun <T :ViewModel> create(modelClass: Class<T>):T{
        return MainViewModel(noteRepository) as T
    }
}