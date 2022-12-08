package com.example.areader.components

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.areader.model.MBook
import com.example.areader.navigation.ReaderScreens
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ReaderLogo(modifier: Modifier = Modifier) {
    Text(
        text = "A.Reader",
        modifier = modifier.padding(bottom = 16.dp),
        style = MaterialTheme.typography.h3,
        color = Color.Red.copy(alpha = 0.5f)
    )
}

@Composable
fun EmailInput(
    modifier: Modifier = Modifier,
    emailState : MutableState<String>,
    labelId : String = "Email",
    enabled : Boolean = true,
    imeAction: ImeAction = ImeAction.Next,
    onAction: KeyboardActions = KeyboardActions.Default

){
    InputField(modifier = modifier,
        valueState = emailState,
        labelId = labelId,
        enabled = enabled,
        imeAction = imeAction,
        onAction = onAction,
        keyboardType = KeyboardType.Email)
}

@Composable
fun InputField(
    modifier: Modifier = Modifier,
    valueState : MutableState<String>,
    labelId : String,
    enabled : Boolean,
    isSingleLine : Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    onAction : KeyboardActions = KeyboardActions.Default
) {

    OutlinedTextField(value = valueState.value ,
        onValueChange = { valueState.value = it},
        modifier = modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        label = { Text(text = labelId)},
        enabled = enabled,
        singleLine = isSingleLine,
        keyboardActions = onAction,
        textStyle = TextStyle(fontSize = 18.sp,
            color = MaterialTheme.colors.onBackground),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType,
            imeAction = imeAction)
    )

}

@Composable
fun PasswordInput(
    modifier: Modifier,
    passwordState: MutableState<String>,
    labelId: String = "Password",
    enabled: Boolean,
    isSingleLine : Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Password,
    passwordVisibility: MutableState<Boolean>,
    imeAction: ImeAction = ImeAction.Done,
    onAction: KeyboardActions = KeyboardActions.Default
) {
    val visualTransformation = if (passwordVisibility.value) VisualTransformation.None else
        PasswordVisualTransformation()
    OutlinedTextField(value = passwordState.value,
        onValueChange = { passwordState.value = it},
        modifier = modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        enabled = enabled,
        label = { Text(text = labelId)},
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
        keyboardActions = onAction,
        singleLine = isSingleLine,
        textStyle = TextStyle(fontSize = 18.sp, color = MaterialTheme.colors.onBackground),
        visualTransformation = visualTransformation,
        trailingIcon = {PasswordVisibility(passwordVisibility = passwordVisibility)}
    )
}

@Composable
fun PasswordVisibility(passwordVisibility: MutableState<Boolean>) {
    val visible = passwordVisibility.value
    IconButton(onClick = {passwordVisibility.value = !visible}) {
        Icons.Default.Close

    }
}


@Composable
fun ReaderTobBar(
    title : String,
    icon : ImageVector? = null,
    showProfile : Boolean = true,
    navController: NavController,
    onBackArrowClicked : () -> Unit = {}
){
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (showProfile){
                    Icon(imageVector = Icons.Default.Person,
                        contentDescription = "Profile Icon",
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .scale(0.6f))
                }

                if (icon != null){
                    Icon(imageVector = icon,
                        tint = Color.Red.copy(0.7f),
                        modifier = Modifier.clickable { onBackArrowClicked.invoke() },
                        contentDescription = "Arrow Back" )
                }
                
                Spacer(modifier = Modifier.width(40.dp))
                Text(text = title,
                    color = Color.Red.copy(0.7f),
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp)
                )

            }
        },
        actions = {
            IconButton(onClick = {
                FirebaseAuth.getInstance().signOut().run {
                    navController.navigate(ReaderScreens.LoginScreen.name)
                }
            }) {
                if(showProfile) Row(){
                    Icon(imageVector = Icons.Filled.Logout, contentDescription = "Logout Icon")

                }else Box(){}
            }
        },
        backgroundColor = Color.Transparent,
        elevation = 0.dp
    )




}

@Composable
fun TitleSection(label : String,
                 modifier: Modifier = Modifier){
    Surface(modifier = Modifier.padding(start = 5.dp, top = 1.dp)) {
        Column {
            Text(text = label,
                fontSize = 19.sp,
                fontStyle = FontStyle.Normal,
                textAlign = TextAlign.Left)
        }
    }
}
@Composable
fun FABContent(onTap: () -> Unit) {
    FloatingActionButton(onClick = {onTap()},
        shape = RoundedCornerShape(50.dp),
        backgroundColor = Color(0xff92cbdf)
    ) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "Add Book",
            tint = Color.White
        )
    }

}

@Preview
@Composable
fun ListCard(book : MBook = MBook(id = "1", title = "Kite Runner",
    authors = "Adam", notes = "Afghanistan Book"),
             onPressDetails : (String) -> Unit = {}){
    val context = LocalContext.current
    val resources = context.resources
    val displayMetrics = resources.displayMetrics
    val screenWidth = displayMetrics.widthPixels/ displayMetrics.density
    val spacing = 10.dp

    Card(shape = RoundedCornerShape(29.dp),
        backgroundColor = Color.White,
        elevation = 6.dp,
        modifier = Modifier
            .padding(6.dp)
            .height(242.dp)
            .width(202.dp)
            .clickable { onPressDetails.invoke(book.title.toString()) }) {

        Column(modifier = Modifier.width(screenWidth.dp - (spacing*2)),
            horizontalAlignment = Alignment.Start) {
            Row(horizontalArrangement = Arrangement.Center) {
                Image(painter = rememberImagePainter(
                    //data = "http://books.google.com/books/content?id=JGHODwAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api"
                    data = if (book.photoUrl.isNullOrEmpty()){"https://ebooks-it.org/e-books/mix/_images/med_CommonsWare.The" +
                            ".Busy.Coders.Guide.To.Advanced.Android.Development.Jul.2009.ISBN.0981678017.pdf.jpg"}
                            else{book.photoUrl.toString()}
                )
                    , contentDescription = "Book Image" ,
                    modifier = Modifier
                        .height(140.dp)
                        .width(100.dp)
                        .padding(4.dp))

                Spacer(modifier = Modifier.width(50.dp))

                Column(modifier = Modifier.padding(top = 25.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(imageVector = Icons.Rounded.FavoriteBorder,
                        contentDescription = "Fav Icon",
                        modifier = Modifier.padding(bottom = 1.dp))

                    BookRating(score = book.rating!!)

                }
            }

            Column {
                Text(text = book.title.toString(), modifier = Modifier.padding(4.dp),
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis)//...

                Text(book.authors.toString(), modifier = Modifier.padding(4.dp),
                    style = MaterialTheme.typography.caption)
            }

            val isStartedReading = remember{
                mutableStateOf(false)
            }

            Spacer(modifier = Modifier.height(15.dp))
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Bottom) {
                isStartedReading.value = book.startedReading != null
                        //&& book.finishedReading == null
                RoundedButton(label = if (isStartedReading.value )"Reading" else "Added", radius = 70)

            }



        }

    }
}

@Composable
fun BookRating(score: Double = 4.5) {
    Surface(modifier = Modifier
        .height(70.dp)
        .padding(4.dp),
        shape = RoundedCornerShape(56.dp),
        elevation = 6.dp,
        color = Color.White
    ) {

        Column(modifier = Modifier.padding(4.dp)) {
            Icon(imageVector = Icons.Filled.StarBorder, contentDescription ="Rating Icon" )
            Text(text = score.toString(), style = MaterialTheme.typography.subtitle1)

        }

    }

}


@Composable
fun RoundedButton(
    label : String = "Reading",
    radius : Int = 29,
    onPress : () -> Unit = {}){

    Surface(modifier = Modifier.clip(RoundedCornerShape(topStartPercent = radius,
        bottomEndPercent = radius))
        ,
        color = Color(0xff92cbdf)){

        Column(modifier = Modifier
            .width(90.dp)
            .heightIn(40.dp)
            .clickable { onPress.invoke() },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center){

            Text(text = label, style = TextStyle(color = Color.White, fontSize = 15.sp))
        }


    }
}

fun showToast(context : Context, msg : String){
    Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
}