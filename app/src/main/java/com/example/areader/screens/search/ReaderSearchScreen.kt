package com.example.areader.screens.search


import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.areader.components.InputField
import com.example.areader.components.ReaderTobBar
import com.example.areader.model.Item
import com.example.areader.navigation.ReaderScreens


@Suppress("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ReaderSearchScreen(navController: NavController,
                       bookSearchViewModel: BookSearchViewModel = hiltViewModel()) {
    Scaffold(topBar = {
        ReaderTobBar(title = "Search Books",
            icon =  Icons.Default.ArrowBack,
            navController = navController,
            showProfile = false){
            //navController.navigate(ReaderScreens.HomeScreen.name)
            navController.navigate(ReaderScreens.HomeScreen.name)
        }
    }){
        Surface() {
            Column() {
                SearchForm(modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                bookSearchViewModel){query ->
                   // Log.d("Search Input", "ReaderSearchScreen: $it")
                    bookSearchViewModel.searchBooks(query)

                }

               BookList(navController, bookSearchViewModel)


            }

        }

    }
}

@Composable
fun BookRow(book: Item, navController: NavController) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .clickable {
            navController.navigate(ReaderScreens.DetailsScreen.name + "/${book.id}")
        }
        .padding(3.dp)
        .height(100.dp),
        shape = RectangleShape,
        elevation = 5.dp) {
        Row(Modifier.padding(5.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Top) {
            val imageUrl : String = book.volumeInfo.imageLinks.smallThumbnail.ifEmpty {
                "https://ebooks-it.org/e-books/mix/_images/med_CommonsWare." +
                        "The.Busy.Coders.Guide.To.Advanced.Android.Development.Jul.2009.ISBN.0981678017.pdf.jpg"
            }
            Image(modifier = Modifier
                .padding(end = 4.dp)
                .width(80.dp)
                .fillMaxHeight()
                , painter = rememberImagePainter(data = imageUrl), contentDescription = "Book URL")
            Column() {
                Text(text = book.volumeInfo.title
                    , overflow = TextOverflow.Ellipsis)
                Text(text = "Author: ${book.volumeInfo.authors}", overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.caption,
                    fontStyle = FontStyle.Italic)

                Text(text = "Date: ${book.volumeInfo.publishedDate}", overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.caption,
                    fontStyle = FontStyle.Italic)

                Text(text = "Category: ${book.volumeInfo.categories}", overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.caption,
                    fontStyle = FontStyle.Italic)

//                Text(text = book.notes.toString(), overflow = TextOverflow.Ellipsis)
//                Text(text = book.id.toString(), overflow = TextOverflow.Ellipsis)
            }


        }

    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun BookList(navController: NavController, bookSearchViewModel: BookSearchViewModel) {

    val listOfBooks = bookSearchViewModel.listOfBooksWithResource

    if (bookSearchViewModel.isLoading){
        //CircularProgressIndicator()
        Row(
            modifier = Modifier.padding(end = 2.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LinearProgressIndicator()
            Text(text = "Loading...")

        }
        Log.d("BOO", "BookList: Loading...")
    }else{
        Log.d("BOO", "BookList: ${bookSearchViewModel.listOfBooksWithResource}")

        LazyColumn(modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            items(items = listOfBooks){book ->
//            ListCard(book = book){
//                navController.navigate(ReaderScreens.DetailsScreen.name)
//            }

                BookRow(book , navController )
            }

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



}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchForm(
    modifier: Modifier = Modifier,
    bookSearchViewModel: BookSearchViewModel,
    loading : Boolean = false,
    hint : String = "Search",
    onSearch : (String) -> Unit
) {
    val searchQueryState = rememberSaveable{
        mutableStateOf("")
    }
    val valid = remember(searchQueryState.value){
        searchQueryState.value.trim().isNotEmpty()
    }

    val keyboardController = LocalSoftwareKeyboardController.current

    InputField(modifier = modifier,
        valueState = searchQueryState, labelId = "Search" , enabled = true,
    onAction = KeyboardActions{
        if (!valid)return@KeyboardActions
        onSearch(searchQueryState.value.trim())
        searchQueryState.value = ""
        keyboardController?.hide()
    }
    )

}
