package com.example.areader.screens.login

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.areader.R
import com.example.areader.components.EmailInput
import com.example.areader.components.PasswordInput
import com.example.areader.components.ReaderLogo
import com.example.areader.navigation.ReaderScreens
import com.google.firebase.firestore.auth.User

@Composable
fun ReaderLoginScreen(navController: NavController,
                      viewModel: LoginScreenViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val showLoginForm = rememberSaveable {
        mutableStateOf(true)
    }
   Surface(modifier = Modifier.fillMaxSize()) {
       Column(
           horizontalAlignment = Alignment.CenterHorizontally
       ) {
           ReaderLogo()
           if (showLoginForm.value){
               UserForm(loading = false, isCreateAccount = false){
                       email, password ->
                   //ToDo FB Login
                   viewModel.signInWithEmailAndPassword(email, password){
                       navController.navigate(ReaderScreens.HomeScreen.name)
                   }

               }
           }else{
               UserForm(loading = false, isCreateAccount = true){
                       email, password ->
                   //ToDp FB CreateAccount
                   viewModel.createUserWithEmailAndPassword(email, password){
                       navController.navigate(ReaderScreens.HomeScreen.name)
                   }
               }
           }
           Spacer(modifier = Modifier.height(15.dp))

           Row(modifier = Modifier.padding(15.dp),
               verticalAlignment = Alignment.CenterVertically,
               horizontalArrangement = Arrangement.Center) {

               val text = if (showLoginForm.value) "Sign Up" else "Login"
               Text(text = "New User?")
               Text(text = text,
                   modifier = Modifier
                       .clickable {
                           showLoginForm.value = !showLoginForm.value
                       }
                       .padding(start = 5.dp),
                   fontWeight = FontWeight.Bold,
                   color = MaterialTheme.colors.secondaryVariant)

           }
       }

   }

}


@OptIn(ExperimentalComposeUiApi::class)
@Preview
@Composable
fun UserForm(loading : Boolean = false,
             isCreateAccount : Boolean = false,
             onDone : (String, String) -> Unit = {email, password -> }){
    val email = rememberSaveable { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }
    val valid = remember(email.value, password.value){
        email.value.trim().isNotEmpty() && password.value.trim().isNotEmpty()
    }
    val passwordVisibility = rememberSaveable { mutableStateOf(false) }
    val passwordFocus = FocusRequester.Default
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(modifier = Modifier
        .height(250.dp)
        .background(MaterialTheme.colors.background)
        .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally) {

        if (isCreateAccount) Text(text = stringResource(id = R.string.create_account),
        modifier = Modifier.padding(4.dp)) else Text("")
        EmailInput(emailState = email,
                   enabled = !loading,
                    onAction = KeyboardActions { passwordFocus.requestFocus() }
        )

        PasswordInput(modifier = Modifier.focusRequester(passwordFocus),
                      passwordState = password,
                      labelId = "Password",
                      enabled = !loading,
                      passwordVisibility = passwordVisibility,
                      onAction = KeyboardActions{
                          if (!valid) return@KeyboardActions

                          onDone(email.value.trim(), password.value.trim())
                      })

        SubmitButton(
            textId = if (isCreateAccount) "Create Account" else "Login",
            loading = loading,
            validInputs = valid
        ){
        onDone(email.value.trim(), password.value.trim())
            keyboardController?.hide()
        }
    }
}

@Composable
fun SubmitButton(textId: String,
                 loading: Boolean,
                 validInputs: Boolean,
                 onClick : () -> Unit) {
    Button(onClick = onClick,
    modifier = Modifier
        .padding(3.dp)
        .fillMaxWidth(),
    enabled = !loading && validInputs,
    shape = CircleShape) {
        if (loading) CircularProgressIndicator(modifier = Modifier.size(25.dp))
        else Text(text = textId, modifier = Modifier.padding(5.dp))

    }
}

