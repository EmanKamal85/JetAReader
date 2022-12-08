package com.example.areader.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.areader.screens.ReaderSplashScreen
import com.example.areader.screens.details.ReaderDetailsScreen
import com.example.areader.screens.home.HomeViewModel
import com.example.areader.screens.home.ReaderHomeScreen
import com.example.areader.screens.login.ReaderLoginScreen
import com.example.areader.screens.search.BookSearchViewModel
import com.example.areader.screens.search.ReaderSearchScreen
import com.example.areader.screens.stats.ReaderStatsScreen
import com.example.areader.screens.update.ReaderUpdateScreen

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun ReaderNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController,
        startDestination = ReaderScreens.SplashScreen.name ){

        composable(ReaderScreens.SplashScreen.name){
            ReaderSplashScreen(navController = navController)
        }

        composable(ReaderScreens.HomeScreen.name){
            val homeViewModel = hiltViewModel<HomeViewModel>()
            ReaderHomeScreen(navController = navController, viewModel = homeViewModel)
        }

        val detailsName = ReaderScreens.DetailsScreen.name
        composable("$detailsName/{bookId}", arguments = listOf(navArgument("bookId"){
            type = NavType.StringType
        })){backStackEntry ->
            backStackEntry.arguments?.getString("bookId").let {

                ReaderDetailsScreen(navController = navController, bookId = it.toString())
            }
        }

        val updateName = ReaderScreens.UpdateScreen.name
        composable("$updateName/{bookItemId}", arguments = listOf(navArgument("bookItemId"){
            type = NavType.StringType
        })){backStackEntry ->
            backStackEntry.arguments?.getString("bookItemId").let {

                ReaderUpdateScreen(navController = navController, bookItemId = it.toString())
            }
        }

        composable(ReaderScreens.LoginScreen.name){
            ReaderLoginScreen(navController = navController)
        }

        composable(ReaderScreens.SearchScreen.name){
            val bookSearchViewModel = hiltViewModel<BookSearchViewModel>()
            ReaderSearchScreen(navController, bookSearchViewModel)
        }

        composable(ReaderScreens.StatsScreen.name){
            val viewModel = hiltViewModel<HomeViewModel>()
            ReaderStatsScreen(navController = navController, viewModel = viewModel)
        }


    }
}