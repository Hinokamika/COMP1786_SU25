package com.example.comp1786_su25

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.comp1786_su25.functionPages.AddClassScreen
import com.example.comp1786_su25.pages.HomePage
import com.example.comp1786_su25.pages.IntroPage
import com.example.comp1786_su25.pages.LoginPage
import com.example.comp1786_su25.pages.SigninPage

@Composable
fun MyAppNavigation(modifier: Modifier = Modifier, authViewModel: AuthViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "intro") {
        composable("intro") {
            IntroPage(modifier, navController, authViewModel)
        }
        composable("login") {
            LoginPage(modifier, navController, authViewModel)
        }
        composable("signup") {
            SigninPage(modifier, navController, authViewModel)
        }
        composable("home") {
            HomePage(modifier, navController, authViewModel)
        }
        composable ("addclass") {
             AddClassScreen(modifier ,navController) // Uncomment when AddClassScreen is implemented
        }
    }
}