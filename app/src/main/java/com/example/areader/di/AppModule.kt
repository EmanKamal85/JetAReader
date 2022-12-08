package com.example.areader.di

import com.example.areader.network.BookAPI
import com.example.areader.repository.BookRepository
import com.example.areader.repository.FireRepository
import com.example.areader.utils.Constants
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent :: class)
object AppModule {
    @Singleton
    @Provides
    fun provideFireBookRepository() =
        FireRepository(bookQuery = FirebaseFirestore.getInstance().collection("books"))

    @Singleton
    @Provides
    fun provideBookRepository(api: BookAPI) = BookRepository(api)

    @Singleton
    @Provides
    fun provideBooksApi() : BookAPI{
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BookAPI :: class.java)
    }
}