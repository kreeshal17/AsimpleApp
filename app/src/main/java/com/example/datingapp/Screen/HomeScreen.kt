package com.example.datingapp.Screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.datingapp.AuthViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.TextFieldValue
import com.example.datingapp.AuthEvent
import com.example.datingapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun home(modifier: Modifier = Modifier, authViewModel: AuthViewModel, navController: NavController) {

    val authstate= authViewModel.authState.observeAsState()
    val context= LocalContext.current
    LaunchedEffect(authstate.value) {
        when(authstate.value)
        {
           is  AuthEvent.Unauthenticated -> navController.navigate(constant.login)

            is AuthEvent.Error -> Toast.makeText(context,(authstate.value as AuthEvent.Error).message,Toast.LENGTH_SHORT).show()
            else->null
        }

    }



    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("LoveConnect") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF6200EE),  // Background color for the TopAppBar
                    titleContentColor = Color.White  // Content (title) color
                )
            )
        },
        bottomBar = {
            BottomAppBar (
                actions = {

                       TextButton(onClick = {authViewModel.signOut() }) {
                           Text(text= "Log-out")

                       }


                }




            )



        },
        content = { padding ->
            HomePageContent(Modifier.padding(padding))  // Call the correct content composable
        }
    )
}

@Composable
fun HomePageContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "Welcome to LoveConnect!", style = MaterialTheme.typography.headlineMedium)

        // Search bar
        var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
        SearchBar(searchQuery) { newText ->
            searchQuery = newText
        }

        // Display images or cards for profiles
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            ProfileCard(name = "John Doe", age = 28)
            ProfileCard(name = "Jane Smith", age = 24)
        }
    }
}

@Composable
fun SearchBar(query: TextFieldValue, onQueryChange: (TextFieldValue) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        BasicTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
                .background(Color.Gray.copy(alpha = 0.1f))
                .padding(12.dp),
            decorationBox = { innerTextField ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text("Search Profiles", color = Color.Gray, modifier = Modifier.weight(1f))
                    innerTextField()
                }
            }
        )
        Button(onClick = { /* Handle search action */ }) {
            Text("Search")
        }
    }
}

@Composable
fun ProfileCard(name: String, age: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),

    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground), // Placeholder image
                contentDescription = "Profile picture",
                modifier = Modifier.size(60.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = name, style = MaterialTheme.typography.headlineMedium)
                Text(text = "Age: $age", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}


