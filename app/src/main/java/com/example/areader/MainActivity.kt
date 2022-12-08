package com.example.areader

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.areader.navigation.ReaderNavigation
import com.example.areader.ui.theme.AReaderTheme
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AReaderTheme {
//                val db = FirebaseFirestore.getInstance()
//                val user : MutableMap<String, Any> = HashMap()
//                user["firstName"] = "Joe"
//                user["lastName"] = "James"

                // A surface container using the 'background' color from the theme
                ReaderApp()

            }
        }
    }
}

@Composable
fun ReaderApp(){
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
//                    db.collection("users")
//                        .add(user)
//                        .addOnSuccessListener {
//                            Log.d("FirebaseTest", "onCreate: ${it.id}")
//                        }.addOnFailureListener {
//                            Log.d("FirebaseTest", "onCreate: ${it}")
//                        }

        Column(verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {

            ReaderNavigation()
        }
    }

}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AReaderTheme {

    }
}