package com.example.areader.repository

import com.example.areader.data.DataOrException
import com.example.areader.model.MBook
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FireRepository @Inject constructor(
    private val bookQuery : Query
) {

    suspend fun getAllBooksFromDatabase() : DataOrException<List<MBook>, Boolean, Exception> {
        val dataOrException = DataOrException<List<MBook>, Boolean, Exception>()

        try {
            dataOrException.loading = true
            dataOrException.data = bookQuery.get().await().documents.map {
                it.toObject(MBook :: class.java)!!
            }
            if (!dataOrException.data.isNullOrEmpty()) dataOrException.loading = false
        }catch (exception : FirebaseFirestoreException){
            dataOrException.e = exception
        }

        return dataOrException
    }
}