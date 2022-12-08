package com.example.areader.screens.details

import androidx.lifecycle.ViewModel
import com.example.areader.data.Resource
import com.example.areader.model.Item
import com.example.areader.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(private val repository: BookRepository)
    : ViewModel(){

        suspend fun getBookInfo(bookId : String) : Resource<Item>{
            return repository.getBookInfoWithResource(bookId)
        }
}