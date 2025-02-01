package com.example.datingapp.Screen

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.datingapp.AuthEvent
import com.example.datingapp.AuthViewModel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember

@Composable
fun SignIn(modifier: Modifier=Modifier, authViewModel: AuthViewModel, navController: NavController)
{







    var authstate=authViewModel.authState.observeAsState()


    var show = remember { true
    }

    var email by remember {
        mutableStateOf("")
    }
    var eye by remember {
        mutableStateOf(false)
    }
    var password by remember {
        mutableStateOf("")
    }



    val context= LocalContext.current
    // Just place the CircularProgressIndicator in the composable hierarchy
    when (val authState = authstate.value) {
        is AuthEvent.Authenticated -> {
            LaunchedEffect(authState) {
                navController.navigate(constant.SetUp)
            }
        }
        is AuthEvent.Error -> {
            LaunchedEffect(authState) {
                Toast.makeText(context, authState.message, Toast.LENGTH_SHORT).show()
            }
        }
        is AuthEvent.Loading -> {
            CircularProgressIndicator(modifier = Modifier.fillMaxWidth())  // You can add modifier to center if needed
        }
        else -> null
    }




    Column(modifier= modifier
        .fillMaxSize()
        .padding(vertical = 20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {

        AnimatedVisibility(visible = show,
            enter = fadeIn(animationSpec = tween(durationMillis = 1000)),
            exit = fadeOut(animationSpec = tween(durationMillis = 5000))



        ) {
            Text(text="Sign In Details", style = TextStyle(color = Color.Blue, fontStyle= FontStyle.Normal, fontSize = 20.sp))

        }

        Spacer(modifier = Modifier.height(25.dp))




        OutlinedTextField(value = email, onValueChange = {email=it},
            label = {
                Text(text="enter your email address")
            })

        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(value = password, onValueChange = {password=it},
            label = {
                Text(text="enter your passsword")
            },

            visualTransformation = if(eye)  VisualTransformation.None else PasswordVisualTransformation()

            ,
            trailingIcon = {
                var image= if (eye) Icons.Filled.Face else Icons.Outlined.Face

                IconButton(onClick = {eye=!eye}) {
                    Icon(imageVector = image, contentDescription = null)

                }
            }
        )

        Spacer(modifier = Modifier.height(30.dp))
        Button(onClick = {authViewModel.signUp(email,password)}) {
            Text(text="Sign -In")

        }
        Spacer(modifier = Modifier.height(10.dp))


        TextButton(onClick = {
            navController.navigate(constant.login)
        }) {
            Text(text = "Already have an account? Log-In")

        }





    }

}


