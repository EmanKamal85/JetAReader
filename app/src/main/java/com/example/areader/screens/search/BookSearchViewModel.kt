package com.example.areader.screens.search

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.areader.data.DataOrException
import com.example.areader.data.Resource
import com.example.areader.model.Item
import com.example.areader.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookSearchViewModel @Inject constructor(private val repository: BookRepository) : ViewModel() {


     val listOfBooks : MutableState<DataOrException<List<Item>, Boolean, Exception>>
    = mutableStateOf(DataOrException(null, true, Exception("")))

    var listOfBooksWithResource :List<Item> by mutableStateOf(listOf())
    var isLoading : Boolean by mutableStateOf(true)

    init {
        searchBooks("android")
    }

     fun searchBooks(query: String) {
//        viewModelScope.launch{
//            if (query.isEmpty()){
//                return@launch
//            }
//
//            listOfBooks.value.loading = true
//            listOfBooks.value = repository.getAllBooks(query)
//            if (listOfBooks.value.data!!.isNotEmpty()) listOfBooks.value.loading = false
//            Log.d("DATA", "searchBooks: ${listOfBooks.value.data.toString()}")
//
//        }

         viewModelScope.launch (Dispatchers.Default){

             if (query.isEmpty()){
                 return@launch
             }
             try {
                 when(val response = repository.getAllBooksWithResources(query)){
                     is Resource.Success -> {
                         listOfBooksWithResource = response.data!!
                         isLoading = false
                     }
                     is Resource.Error -> {
                         Log.d( "Error", "searchBooks: Failed Getting Books")
                         isLoading = false
                     }
                     else -> {isLoading = false}
                 }

             }catch (exception : Exception){
                 isLoading = false
                 Log.d("loadingBooksException", "searchBooks: ${exception.message.toString()}")

             }
         }

    }
}