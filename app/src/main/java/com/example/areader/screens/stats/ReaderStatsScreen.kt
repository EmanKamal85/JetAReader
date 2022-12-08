package com.example.areader.screens.stats

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.sharp.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.areader.ReaderApp
import com.example.areader.components.ReaderTobBar
import com.example.areader.model.Item
import com.example.areader.model.MBook
import com.example.areader.navigation.ReaderScreens
import com.example.areader.screens.home.HomeViewModel
import com.example.areader.screens.search.BookRow
import com.example.areader.utils.formatDate
import com.google.firebase.auth.FirebaseAuth
import java.util.*

@RequiresApi(Build.VERSION_CODES.N)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ReaderStatsScreen(navController: NavController,
                      viewModel: HomeViewModel = hiltViewModel()) {
    var listOfBooks : List<MBook>
    val currentUser = FirebaseAuth.getInstance().currentUser


    Scaffold(topBar = {
        ReaderTobBar(title = "Book Stats",
                    icon = Icons.Default.ArrowBack,
                    showProfile = false,
                    navController = navController){
            navController.navigate(ReaderScreens.HomeScreen.name)
        }
    }) {
        Surface() {
            listOfBooks = if (viewModel.homeModelData.value.data.isNullOrEmpty()){
                emptyList()
            }else{
                viewModel.homeModelData.value.data!!.filter { mBook ->
                    (mBook.userId == currentUser?.uid)
                }
            }
            
            Column {
                Row {
                    Box(modifier = Modifier
                        .size(45.dp)
                        .padding(2.dp)){
                        Icon(imageVector = Icons.Sharp.Person,
                            contentDescription = "Icon" )
                    }

                    Text(text = "Hi, ${currentUser!!.email.toString().split("@")[0].uppercase(Locale.getDefault())}")
                }

                Card(modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                    shape = CircleShape,
                    elevation = 5.dp
                ) {

                    val readBooksList : List<MBook>
                    = if (viewModel.homeModelData.value.data.isNullOrEmpty()){
                        emptyList()
                    }else{
                        listOfBooks.filter { mBook ->
                            (mBook.userId == currentUser?.uid) && (mBook.finishedReading != null)
                        }
                    }

                    val inProgressBooks = listOfBooks.filter { mBook ->
                        (mBook.startedReading != null && mBook.finishedReading == null)
                    }
                    
                    Column(modifier = Modifier.padding(start = 25.dp, top = 4.dp, bottom = 4.dp),
                    horizontalAlignment = Alignment.Start) {
                        Text(text = "Your Stats", style = MaterialTheme.typography.h5)
                        Divider()
                        Text(text = "You're Reading: ${inProgressBooks.size} books")
                        Text(text = "You've Read: ${readBooksList.size} books")

                    }
                }
                if (viewModel.homeModelData.value.loading == true){
                    LinearProgressIndicator()
                }else{
                    LazyColumn(modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp)){
                        val finishedBooks = if (viewModel.homeModelData.value.data.isNullOrEmpty()){
                            emptyList()
                        }else{
                            viewModel.homeModelData.value.data!!.filter { mBook ->
                                (mBook.userId == currentUser?.uid && mBook.finishedReading != null)
                            }
                        }
                        items(items = finishedBooks){mBook ->
                            StatusBookRow(book = mBook)

                        }
                    }
                }
            }


        }

    }
}


@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun StatusBookRow(book: MBook, viewModel: HomeViewModel = hiltViewModel()) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .clickable {
        }
        .padding(3.dp)
        .height(100.dp),
        shape = RectangleShape,
        elevation = 5.dp) {
        Row(Modifier.padding(5.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Top) {
            val imageUrl : String = book.photoUrl.toString().ifEmpty {
                "https://ebooks-it.org/e-books/mix/_images/med_CommonsWare." +
                        "The.Busy.Coders.Guide.To.Advanced.Android.Development.Jul.2009.ISBN.0981678017.pdf.jpg"
            }
            Image(modifier = Modifier
                .padding(end = 4.dp)
                .width(80.dp)
                .fillMaxHeight()
                , painter = rememberImagePainter(data = imageUrl), contentDescription = "Book URL")
            Row(horizontalArrangement = Arrangement.SpaceBetween) {


                Column() {
                    Text(
                        text = book.title.toString(),
                        overflow = TextOverflow.Ellipsis,
                        softWrap = true
                    )
                    Text(
                        text = "Author: ${book.authors}", overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.caption,
                        fontStyle = FontStyle.Italic,
                        softWrap = true
                    )

                    Text(
                        text = "Started: ${formatDate(book.startedReading!!)}", overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.caption,
                        fontStyle = FontStyle.Italic,
                        softWrap = true
                    )

                    Text(
                        text = "Finished: ${formatDate(book.finishedReading!!)}", overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.caption,
                        fontStyle = FontStyle.Italic,
                        softWrap = true
                    )

//                Text(text = book.notes.toString(), overflow = TextOverflow.Ellipsis)
//                Text(text = book.id.toString(), overflow = TextOverflow.Ellipsis)
                }
                
                

                if (book.rating!! >= 4.0) {
                    Spacer(modifier = Modifier.fillMaxWidth(0.8f))
                    Icon(imageVector = Icons.Default.ThumbUp,
                        contentDescription = "Thumbs Up",
                    tint = Color.Green.copy(alpha = 0.5f)
                    )
                }else{
                    Box{}
                }
            }


        }

    }
}