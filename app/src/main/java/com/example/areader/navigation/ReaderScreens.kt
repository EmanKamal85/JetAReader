package com.example.areader.navigation

enum class ReaderScreens {
    SplashScreen,
    LoginScreen,
    CreateAccountScreen,
    HomeScreen,
    SearchScreen,
    DetailsScreen,
    UpdateScreen,
    StatsScreen;

    companion object {
        fun fromRoute(route :String?) : ReaderScreens
        = when(route?.substringBefore("/")){
            SplashScreen.name -> SplashScreen
            LoginScreen.name -> LoginScreen
            CreateAccountScreen.name -> CreateAccountScreen
            HomeScreen.name -> HomeScreen
            SearchScreen.name -> SearchScreen
            DetailsScreen.name -> DetailsScreen
            UpdateScreen.name -> UpdateScreen
            StatsScreen.name -> StatsScreen
            null -> HomeScreen
            else -> throw IllegalArgumentException("Route $route is not recognized")


        }
    }
}