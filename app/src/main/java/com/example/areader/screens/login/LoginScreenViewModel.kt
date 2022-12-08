package com.example.areader.screens.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.areader.model.MUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class LoginScreenViewModel : ViewModel() {

    val loadingState = MutableStateFlow(LoadingState.IDLE)
    private val auth : FirebaseAuth = Firebase.auth

    private val _loading = MutableLiveData(false)
    val loading : LiveData<Boolean> = _loading

    fun signInWithEmailAndPassword(email : String, password : String, home : () -> Unit)
    = viewModelScope.launch{
        try {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener{task ->
                    if (task.isSuccessful){
                        Log.d("LoginSuccessful", "signInWithEmailAndPassword: LoginSuccessful ${task.result}")
                        //TODO("take them home")
                        home()
                    }else{
                        Log.d("SignInTaskFailed", "signInWithEmailAndPassword: SignInTaskFailed ${task.result}")
                    }
                }
        }catch (ex : Exception){
            Log.d("SignInException", "signInWithEmailAndPassword: SignInException ${ex.message}")
        }

    }

    fun createUserWithEmailAndPassword(email : String,
                                       password : String,
                                       home : () -> Unit){

        if (_loading.value == false){
            _loading.value = true
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener{task ->
                    if (task.isSuccessful){
                        val displayName = task.result.user?.email?.split('@')?.get(0)
                        createUser(displayName)
                        home()
                    }else{
                        Log.d("CreateAccountFailed", "createUserWithEmailAndPassword: CreateAccountFailed ${task.result}")
                    }
                    _loading.value = false
                }
        }
    }

    private fun createUser(displayName: String?) {
        val userId = auth.currentUser?.uid
        val user = MUser(
            id = null,
        userId = userId.toString(),
        displayName = displayName.toString(),
        avatarUrl = "",
        quote = "No Quote",
        profession = "Developer").toMap()

        FirebaseFirestore.getInstance().collection("users")
            .add(user)
    }
}

    