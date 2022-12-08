package com.example.areader.screens.details

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.areader.components.ReaderTobBar
import com.example.areader.components.RoundedButton
import com.example.areader.data.Resource
import com.example.areader.model.Item
import com.example.areader.model.MBook
import com.example.areader.navigation.ReaderScreens
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ReaderDetailsScreen(navController: NavController, bookId : String,
        viewModel: DetailsViewModel = hiltViewModel()) {
    Scaffold(topBar = {
        ReaderTobBar(title = "Book Details",
                     navController = navController,
                     icon = Icons.Default.ArrowBack,
                     showProfile = false){
            navController.navigate(ReaderScreens.SearchScreen.name)
        }
    }) {
        Surface(modifier = Modifier
            .padding(3.dp)
            .fillMaxSize()) {
            Column(modifier = Modifier.padding(top = 12.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally) {

                val bookInfo = produceState<Resource<Item>>(initialValue = Resource.Loading()){
                    value = viewModel.getBookInfo(bookId)
                }.value
                if (bookInfo.data == null){
                    Row(horizontalArrangement = Arrangement.SpaceBetween) {
                        LinearProgressIndicator()
                        Text(text = "Loading...")
                    }
                }else{

                    //Text(text = "Book Id ${bookInfo.data.volumeInfo.title}")
                    ShowBookDetails(bookInfo, navController)
                }
            }
        }
    }
}

@Composable
fun ShowBookDetails(bookInfo: Resource<Item>, navController: NavController) {
    val bookData = bookInfo.data?.volumeInfo
    val googleBookId = bookInfo.data?.id
    
    Column(verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally) {

        Card(
            modifier = Modifier.padding(34.dp),
            shape = CircleShape, elevation = 4.dp
        ) {


            Image(
                painter = rememberImagePainter(data = bookData!!.imageLinks.thumbnail),
                contentDescription = "Book Image",
                modifier = Modifier
                    .size(90.dp)
                    .padding(1.dp)
            )
        }

        Text(
            text = bookData!!.title,
            style = MaterialTheme.typography.h6,
            overflow = TextOverflow.Ellipsis,
            maxLines = 19
        )

        Text(text = "Authors: ${bookData.authors}")

        Text(text = "Page Count: ${bookData.pageCount}")

        Text(
            text = "Categories: ${bookData.categories}",
            style = MaterialTheme.typography.subtitle1,
            overflow = TextOverflow.Ellipsis,
            maxLines = 3
        )

        Text(
            text = "Published: ${bookData.publishedDate}",
            style = MaterialTheme.typography.subtitle1
        )
        
        Spacer(modifier = Modifier.height(5.dp))

        val cleanDescription = HtmlCompat.fromHtml(bookData?.description!!, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
        val localDims = LocalContext.current.resources.displayMetrics
        
        Surface(modifier = Modifier
            .height(localDims.heightPixels.times(0.09f).dp)
            .padding(4.dp),
                shape = RectangleShape,
                border = BorderStroke(1.dp, Color.DarkGray)
        ) {

            LazyColumn{
                item {
                    if (cleanDescription.isNullOrEmpty()){
                        Log.d("Null Description", "ShowBookDetails: Null description")
                    }else{

                        Text(text = cleanDescription)
                    }
                    //Text(text = bookData.description)
                }
            }
        }

        Row(horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 6.dp)) {

            RoundedButton(label = "Save"){
                val book = MBook(
                    title = bookData.title,
                    authors = bookData.authors.toString(),
                    notes = " ",
                    photoUrl = bookData.imageLinks.thumbnail,
                    categories = bookData.categories.toString(),
                    publishDate = bookData.publishedDate,
                    rating = 0.0,
                    description = bookData.description,
                    pageCount = bookData.pageCount.toString(),
                    userId = FirebaseAuth.getInstance().currentUser?.uid.toString(),
                    googleBookId = googleBookId
                )
                saveToFirebase(book, navController)

            }

            Spacer(modifier = Modifier.width(25.dp))

            RoundedButton(label = "Cancel"){
                navController.popBackStack()

            }
        }


    }



    }

fun saveToFirebase(book: MBook, navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    val dbCollection = db.collection("books")

    if (book.toString().isNotEmpty()){
        dbCollection.add(book)
            .addOnSuccessListener {documentRef->
                val docId = documentRef.id
                dbCollection.document(docId)
                    .update(hashMapOf("id" to docId) as Map<String, Any>)
                    .addOnCompleteListener {task ->
                        if (task.isSuccessful){
                            navController.popBackStack()
                        }

                    }
                    .addOnFailureListener {
                        Log.w("Error", "saveToFirebase: Error updating doc ", it )
                    }
            }

    }else{

    }

}


