package com.example.areader.screens.home

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.areader.data.DataOrException
import com.example.areader.model.MBook
import com.example.areader.repository.FireRepository
import com.google.firebase.FirebaseException
import com.google.firebase.firestore.FirebaseFirestoreException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository : FireRepository)
    : ViewModel()
{

    val homeModelData : MutableState<DataOrException<List<MBook>, Boolean, Exception>>
    = mutableStateOf(DataOrException(listOf(), true, Exception("")))

    init {
        getAllBooksFromDatabase()
    }

    private fun getAllBooksFromDatabase() {
        viewModelScope.launch {
            homeModelData.value.loading = true
            homeModelData.value = repository.getAllBooksFromDatabase()
            if (!homeModelData.value.data.isNullOrEmpty()) homeModelData.value.loading = false
            
        }

        Log.d("Query Books", "getAllBooksFromDatabase: ${homeModelData.value.data.toString()}")
    }

}