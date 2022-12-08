package com.example.areader.screens.update

import android.annotation.SuppressLint
import android.os.Build
import android.os.Message
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.areader.R
import com.example.areader.components.InputField
import com.example.areader.components.ReaderTobBar
import com.example.areader.components.RoundedButton
import com.example.areader.components.showToast
import com.example.areader.data.DataOrException
import com.example.areader.model.MBook
import com.example.areader.navigation.ReaderScreens
import com.example.areader.screens.home.HomeViewModel
import com.example.areader.utils.formatDate
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.ui.ExperimentalComposeUiApi as ExperimentalComposeUiApi1

@RequiresApi(Build.VERSION_CODES.N)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ReaderUpdateScreen(navController: NavController,
                       bookItemId : String,
                       viewModel : HomeViewModel = hiltViewModel()) {

    Scaffold(topBar = {
        ReaderTobBar(title = "Update Book",
        icon = Icons.Default.ArrowBack,
        showProfile = false,
        navController = navController){
            navController.popBackStack()
        }
    }) {

        val bookInfo = produceState< DataOrException<List<MBook>, Boolean, Exception>>(
            initialValue = DataOrException(data = emptyList(), true, Exception(""))){
            value = viewModel.homeModelData.value
        }.value

        Surface(modifier = Modifier
            .fillMaxSize()
            .padding(3.dp)) {
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Log.d("Update Data", "ReaderUpdateScreen: ${viewModel.homeModelData.value}")
                if (bookInfo.loading == true){
                    LinearProgressIndicator()
                   // if (!bookInfo.data.isNullOrEmpty()){

                        bookInfo.loading = false
                    //}
                }else{
                    //Text(text = viewModel.homeModelData.value.data?.get(0)?.title.toString())
                    Surface(modifier = Modifier
                        .fillMaxWidth()
                        .padding(2.dp),
                    shape = CircleShape,
                    elevation = 4.dp) {

                        ShowBookUpdate(bookInfo = viewModel.homeModelData.value, bookItemId = bookItemId)

                    }



                        ShowSimpleForm(bookItem = viewModel.homeModelData.value.data?.first { mBook ->
                            mBook.googleBookId == bookItemId
                        }, navController = navController)


                }


            }
        }

    }




}

@OptIn(androidx.compose.ui.ExperimentalComposeUiApi::class)
@Composable
fun RatingBar(modifier: Modifier = Modifier,
              rating: Int,
              onRatingPress: (Int) -> Unit = {}) {

    var ratingState = remember {
        mutableStateOf(rating)
    }

    var selected = remember {
        mutableStateOf(false)
    }

    val size by animateDpAsState(
        targetValue = if (selected.value) 42.dp else 34.dp,
        spring(Spring.DampingRatioMediumBouncy)
    )
    
    Row(
        modifier = Modifier.width(280.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        for (i in 1..5){
            Icon(painter = painterResource(id = R.drawable.ic_baseline_star_rate_24),
                contentDescription = "star",
            modifier = Modifier
                .width(size)
                .height(size)
                .pointerInteropFilter {
                    when (it.action) {
                        MotionEvent.ACTION_DOWN -> {
                            selected.value = true
                            onRatingPress(i)
                            ratingState.value = i
                        }
                        MotionEvent.ACTION_UP -> {
                            selected.value = false
                        }
                    }
                    true
                },
            tint = if ( i <= ratingState.value) Color(0xffffd700) else Color(0xffa2adb1)
            )
            
    }
    }

}

@RequiresApi(Build.VERSION_CODES.N)
@OptIn(androidx.compose.ui.ExperimentalComposeUiApi::class)
@Composable
fun ShowSimpleForm(bookItem: MBook?, navController: NavController) {
    val context = LocalContext.current

    val notesText = remember {
     mutableStateOf("")
    }

    val isStartedReading = remember {
        mutableStateOf(false)
    }

    val isFinishedReading = remember{
        mutableStateOf(false)
    }

    val ratingVal = remember{
        mutableStateOf(0)
    }

    SimpleForm(defaultValue = if (bookItem?.notes!!.isNullOrEmpty())"No thoughts Available"
    else bookItem.notes.toString()) {note ->
        notesText.value = note

    }


    Row(Modifier.padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start) {
        TextButton(onClick = { isStartedReading.value = true},
            enabled = bookItem.startedReading == null) {
            if (bookItem.startedReading == null){
                if (isStartedReading.value){
                    Text(
                        text = "Started Reading",
                        modifier = Modifier.alpha(0.6f),
                        color = Color.Red.copy(0.5f)
                    )
                } else{
                    Text(text = "Start Reading")
                }

            }else{
                Text(text = "Started Reading on: ${formatDate(bookItem.startedReading!!)}")//to do format date

        }
    }

        Spacer(modifier = Modifier.width(4.dp))

        TextButton(onClick = { isFinishedReading.value = true},
            enabled = bookItem.finishedReading == null) {
            if (bookItem.finishedReading == null){
                if (isFinishedReading.value){
                    Text(
                        text = "Mark as Read",
                        modifier = Modifier.alpha(0.6f),
                        color = Color.Red.copy(0.5f)
                    )
                } else{
                    Text(text = "Mark as Read")
                }

            }else{
                Text(text = "Finished Reading on: ${formatDate(bookItem.finishedReading!!)}")//to do format date

            }
        }
}

    Text(text = "Rating",
        modifier = Modifier.padding(bottom = 3.dp))
    bookItem.rating?.toInt().let {

        RatingBar(rating = it!!){ rating ->
            ratingVal.value = rating
            //bookItem.rating = rating

        }
    }
    
    Spacer(modifier = Modifier.height(25.dp))

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val changedNotes =  bookItem.notes != notesText.value
        val changedRating = bookItem.rating?.toInt() != ratingVal.value
        val isStartedTimeStamp = if (isStartedReading.value) Timestamp.now() else bookItem.startedReading
        val isFinishedTimeStamp = if (isFinishedReading.value) Timestamp.now() else bookItem.finishedReading
        val bookToUpdateBoolean = changedNotes || changedRating || isStartedReading.value || isFinishedReading.value

        val bookToUpdate = hashMapOf(
            "started_reading" to isStartedTimeStamp,
            "finished_reading" to isFinishedTimeStamp,
            "rating" to ratingVal.value,
            "notes" to notesText.value
        ).toMap()
        RoundedButton("Update"){
            if (bookToUpdateBoolean){
                FirebaseFirestore.getInstance()
                    .collection("books")
                    .document(bookItem.id!!)
                    .update(bookToUpdate)
                    .addOnCompleteListener{task ->
                        showToast(context, "Book Updated Successfully")
                        navController.navigate(ReaderScreens.HomeScreen.name)
//                        Log.d("UpdateSuccess", "ShowSimpleForm: ${task.result}")

                    }.addOnFailureListener{
                        Log.w("ErrorUpdate", "ShowSimpleForm: Error Updating Document ", it )
                    }
            }
        }
        
        Spacer(modifier = Modifier.width(50.dp))

        val openDialog = remember {
            mutableStateOf(false)
        }

        if (openDialog.value){

            ShowAlertDialog(message = stringResource(id = R.string.sure) +
                    "\n" + stringResource(id = R.string.action), openDialog){
                FirebaseFirestore.getInstance()
                    .collection("books")
                    .document(bookItem.id!!)
                    .delete()
                    .addOnCompleteListener {
                    if (it.isSuccessful){
                        openDialog.value = false
                        navController.navigate(ReaderScreens.HomeScreen.name )
                    }
                    }
                    .addOnFailureListener {

                    }
            }
        }

        RoundedButton("Delete"){
        openDialog.value = true
        }
    }

}

@Composable
fun ShowAlertDialog(
    message: String,
    openDialog : MutableState<Boolean>,
    onYesPressed : () -> Unit
    ) {
    AlertDialog(onDismissRequest = {openDialog.value = false},
                title = { Text(text = "Delete")},
                text = { Text(text = message)},
                buttons = {
                    Row(modifier = Modifier.padding(9.dp),
                    horizontalArrangement = Arrangement.Center) {
                        TextButton(onClick = {onYesPressed.invoke()}) {
                            Text(text = "Yes")                            
                        }
                        
                        TextButton(onClick = {openDialog.value = false}) {
                            Text(text = "No")
                        }
                    }
                })

}


@OptIn(androidx.compose.ui.ExperimentalComposeUiApi::class)
@Composable
fun SimpleForm(modifier: Modifier = Modifier,
               loading : Boolean = false,
               defaultValue : String = " Great Book",
               onSearch : (String) -> Unit) {
    Column() {
        val textFieldValue = rememberSaveable { mutableStateOf(defaultValue) }
        val keyboardController = LocalSoftwareKeyboardController.current
        val valid = remember(textFieldValue.value) {
            textFieldValue.value.trim().isNotEmpty()
        }

        InputField(modifier = modifier
            .fillMaxWidth()
            .padding(3.dp)
            .height(140.dp)
            .background(Color.White, shape = CircleShape),
            valueState = textFieldValue,
            labelId = "Enter Your Thoughts",
            enabled = true,
            isSingleLine = false,
            onAction = KeyboardActions {
                if (!valid)return@KeyboardActions
                onSearch(textFieldValue.value.trim())
                keyboardController?.hide()
            }
        )



    }
}

@Composable
fun ShowBookUpdate(bookInfo: DataOrException<List<MBook>, Boolean, Exception>,
                   bookItemId : String) {

    val bookItem = bookInfo.data?.first { mBook ->
        mBook.googleBookId == bookItemId
    }
    Row() {
        Spacer(modifier = Modifier.width(45.dp))
        if (bookInfo.data != null){
            Column(modifier = Modifier.padding(4.dp),
                verticalArrangement = Arrangement.Center) {
                CardListItem(bookItem = bookItem, onPressDetails = {})

            }
        }


    }

}

@Composable
fun CardListItem(bookItem: MBook?, onPressDetails: () -> Unit) {
   Card(
       modifier = Modifier
           .padding(4.dp)
           .clickable { onPressDetails.invoke() }
           .clip(
               RoundedCornerShape(20.dp)
           ),
       elevation = 20.dp
   ) {
       Row() {
           Image(
               painter = rememberImagePainter(data = bookItem?.photoUrl),
               contentDescription = "Book Item Image",
               modifier = Modifier
                   .size(100.dp)
                   .clip(RoundedCornerShape(topStart = 120.dp, topEnd = 20.dp))
                   .padding(4.dp)
           )
           Column() {
               Text(
                   text = bookItem?.title.toString(),
                   style = MaterialTheme.typography.h6,
                   fontWeight = FontWeight.SemiBold,
                   maxLines = 2,
                   overflow = TextOverflow.Ellipsis,
                   modifier = Modifier
                       .padding(start = 8.dp, end = 8.dp)
                       .width(120.dp)
               )

               Text(
                   text = bookItem?.authors.toString(),
                   style = MaterialTheme.typography.body2,
                   fontWeight = FontWeight.SemiBold,
                   maxLines = 2,
                   overflow = TextOverflow.Ellipsis,
                   modifier = Modifier
                       .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
                       .width(120.dp)
               )

               Text(
                   text = bookItem?.publishDate.toString(),
                   style = MaterialTheme.typography.body2,
                   fontWeight = FontWeight.SemiBold,
                   maxLines = 2,
                   overflow = TextOverflow.Ellipsis,
                   modifier = Modifier
                       .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
                       .width(120.dp)
               )
           }
       }

   }
}
