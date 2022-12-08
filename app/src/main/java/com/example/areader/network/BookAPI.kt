package com.example.areader.network

import com.example.areader.model.Item
import com.example.areader.model.MBook
import com.example.areader.model.URLBook
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface BookAPI {

    @GET("volumes")
    suspend fun  getAllBooks(@Query("q") query : String) : URLBook

    @GET("volumes/{bookId}")
    suspend fun getBookInfo(@Path("bookId") bookId : String) : Item
}