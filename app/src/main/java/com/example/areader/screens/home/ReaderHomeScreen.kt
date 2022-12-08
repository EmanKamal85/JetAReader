package com.example.areader.screens.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.areader.components.FABContent
import com.example.areader.components.ListCard
import com.example.areader.components.ReaderTobBar
import com.example.areader.components.TitleSection
import com.example.areader.model.MBook
import com.example.areader.navigation.ReaderScreens
import com.google.firebase.auth.FirebaseAuth



@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ReaderHomeScreen(navController: NavController,
                     viewModel: HomeViewModel = hiltViewModel()) {
    Scaffold(topBar = { ReaderTobBar(title = "A.Reader", navController = navController )},
        floatingActionButton = {
        FABContent{
            navController.navigate(ReaderScreens.SearchScreen.name)
        }
    }){
        //content
        Surface(modifier = Modifier.fillMaxSize()) {
            //home content
            HomeContent(navController, viewModel)

        }
    }
}

@Composable
fun HomeContent(navController: NavController, viewModel: HomeViewModel) {

    var listOfBooks = emptyList<MBook>()
    val userId = FirebaseAuth.getInstance().currentUser?.uid

    if (!viewModel.homeModelData.value.data.isNullOrEmpty()){
        listOfBooks = viewModel.homeModelData.value.data!!.toList().filter { mBook ->
            mBook.userId == userId
        }
    }
//    val listOfBooks = listOf(
//        MBook(id = "1", title = "Book1", authors = "author1", notes = null),
//        MBook(id = "2", title = "Book2", authors = "author2", notes = null),
//        MBook(id = "3", title = "Book3", authors = "author3", notes = null),
//        MBook(id = "4", title = "Book4", authors = "author4", notes = null),
//        MBook(id = "5", title = "Book5", authors = "author5", notes = null)
//
//    )

    val email = FirebaseAuth.getInstance().currentUser?.email
    val currentUserName =if (!email.isNullOrEmpty()){
        email.split("@")[0]
    }else{
        "N/A"
    }
    Column(modifier = Modifier.padding(2.dp),
        verticalArrangement = Arrangement.SpaceEvenly) {
        Row(modifier = Modifier.align(Alignment.Start)) {
            TitleSection(label = "You're Reading \n" + "Activity Right now")

            Spacer(modifier = Modifier.fillMaxWidth(0.7f))

            Column {
                Icon(imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "Profile Photo",
                    modifier = Modifier
                        .clickable { navController.navigate(ReaderScreens.StatsScreen.name) }
                        .size(45.dp),
                    tint = MaterialTheme.colors.secondaryVariant)

                Spacer(modifier = Modifier.fillMaxWidth(0.7f))

                Text(text = currentUserName,
                modifier = Modifier.padding(2.dp),
                style = MaterialTheme.typography.overline,
                color = Color.Red,
                maxLines = 1,
                overflow = TextOverflow.Clip)

                Divider()
            }
        }
       ReadRightNowArea(listOfBooks = listOfBooks, navController = navController)

        TitleSection(label = "Reading List")

        BookListArea(listOfBooks = listOfBooks, navController = navController)

    }
}

@Composable
fun BookListArea(listOfBooks: List<MBook>, navController: NavController) {

    val addedBooks = listOfBooks.filter { mBook ->
        mBook.startedReading == null && mBook.finishedReading == null
    }
    HorizontalScrollableComponent(addedBooks){
        navController.navigate(ReaderScreens.UpdateScreen.name+ "/$it")
        Log.d("TAG", "BookListArea: $it")
        //on card clicked navigate to details
    }
}

@Composable
fun HorizontalScrollableComponent(listOfBooks: List<MBook>,
                                  viewModel: HomeViewModel = hiltViewModel(),
                                  onCardPressed : (String) -> Unit) {
    val scrollState = rememberScrollState()
    Row(modifier = Modifier
        .fillMaxWidth()
        .heightIn(280.dp)
        .horizontalScroll(scrollState)) {

        if (viewModel.homeModelData.value.loading == true){
            LinearProgressIndicator()
        }else{
            if (listOfBooks.isNullOrEmpty()){
                Surface(modifier = Modifier.padding(23.dp)) {
                    Text(text = "No books found. Add a book",
                        style = TextStyle(color = Color.Red.copy(0.4f),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp))
                }
            }else{
                for (book in listOfBooks){
                    ListCard(book){
                        onCardPressed(book.googleBookId.toString())
                    }
                }

            }
        }

    }
}

@Composable
fun ReadRightNowArea(listOfBooks : List<MBook>,
                     navController: NavController){

    val readingNowBooks = listOfBooks.filter { mBook ->
        mBook.startedReading != null && mBook.finishedReading == null
    }
    HorizontalScrollableComponent(listOfBooks = readingNowBooks){
        navController.navigate(ReaderScreens.UpdateScreen.name + "/$it")
    }

}




