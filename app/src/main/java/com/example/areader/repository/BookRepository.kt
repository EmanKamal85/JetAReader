package com.example.areader.repository

import android.provider.ContactsContract.Data
import com.example.areader.data.DataOrException
import com.example.areader.data.Resource
import com.example.areader.model.Item
import com.example.areader.network.BookAPI
import dagger.Provides
import javax.inject.Inject
import javax.inject.Singleton

class BookRepository @Inject constructor(private val api : BookAPI){
    private val dataOrException =
        DataOrException<List<Item>, Boolean, Exception>()

    private val dataOrExceptionInfo =
        DataOrException<Item, Boolean, Exception>()
    suspend fun getAllBooks(searchQuery : String) : DataOrException<List<Item>,
            Boolean, Exception>{
        try {
            dataOrException.loading = true
            dataOrException.data = api.getAllBooks(searchQuery).items
            if (dataOrException.data!!.isNotEmpty()) dataOrException.loading = false


        }catch (e : Exception){
            dataOrException.e = e
        }
        return dataOrException

    }

    suspend fun getAllBooksWithResources(searchQuery: String) : Resource<List<Item>>{
        return try {
            Resource.Loading(data = true)
            val itemList = api.getAllBooks(searchQuery).items
            if (itemList.isNotEmpty()){
                Resource.Loading(data = false)

            }
            Resource.Success(data = itemList)

        }catch (exception : Exception){
            Resource.Error(exception.message.toString())

        }

    }



    suspend fun getBookInfo(bookId : String) : DataOrException<Item, Boolean, Exception>{
        val response = try {
            dataOrExceptionInfo.loading = true
            dataOrExceptionInfo.data = api.getBookInfo(bookId)
            if (dataOrExceptionInfo.data.toString().isNotEmpty()){
                dataOrExceptionInfo.loading = false
            }else{}
        }catch (e : Exception){
            dataOrExceptionInfo.e = e
        }
        return dataOrExceptionInfo
    }

    suspend fun getBookInfoWithResource(bookId: String) : Resource<Item>{
        val response = try {
            Resource.Loading(data = true)
            api.getBookInfo(bookId)

        }catch (exception : Exception){
           return Resource.Error("An error occurred ${exception.message.toString()}")
        }
        Resource.Loading(data = false)
        return Resource.Success(data = response)
    }


}