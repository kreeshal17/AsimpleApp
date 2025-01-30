package com.example.datingapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.datingapp.Screen.SignIn
import com.example.datingapp.Screen.constant
import com.example.datingapp.Screen.constant.home
import com.example.datingapp.Screen.home
import com.example.datingapp.Screen.login

@Composable
fun MainNav(modifier:Modifier=Modifier,authViewModel: AuthViewModel)
{
    val  navController= rememberNavController()
    NavHost(navController = navController, startDestination =constant.login, builder = {

        composable(constant.login)
        {
            login(modifier,authViewModel,navController)
        }


        composable(constant.home)
        {
            home(modifier,authViewModel,navController)
        }
        composable(constant.signin)
        {
            SignIn(modifier,authViewModel,navController)
        }





    } )











}