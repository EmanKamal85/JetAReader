package com.example.areader.screens.login



data class LoadingState (val status : Status, val message : String? = null){

    companion object{
        val LOADING = LoadingState(Status.LOADING)
        val SUCCESS = LoadingState(Status.SUCCESS)
        val FAIL = LoadingState(Status.FAIL)
        val IDLE = LoadingState(Status.IDLE)
    }

    enum class Status{
        LOADING,
        SUCCESS,
        FAIL,
        IDLE
    }



}
