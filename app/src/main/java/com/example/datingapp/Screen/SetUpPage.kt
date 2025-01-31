package com.example.datingapp.Screen

import android.app.DatePickerDialog
import android.content.Context
import android.net.Uri
import android.widget.DatePicker
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.datingapp.AuthViewModel
import com.example.datingapp.R
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.Calendar

data class Radio(
    val label:String,
    val isChecked:Boolean
)


fun openDateDialog(onDateSelected: (String,Int) -> Unit,context: Context) {

    val calendar =Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day= calendar.get(Calendar.DAY_OF_MONTH)


    val datePickerDialog= DatePickerDialog(context,
        {    _,  selectedyear:Int,selectedmonth:Int,selectedday:Int  ->

            val formattedDate = "$selectedyear-${selectedmonth+1}-$selectedday"
            val birthDate= LocalDate.of(selectedyear,selectedmonth+1,selectedday)
            val Calulatedage =ChronoUnit.YEARS.between(birthDate,LocalDate.now()).toInt()


            onDateSelected(formattedDate,Calulatedage)
        },
        year
        ,month,
        day


    )
    datePickerDialog.show()







}

@Composable
fun Setup(modifier: Modifier = Modifier, authViewModel: AuthViewModel, navController: NavController) {

    val items = remember {
        mutableStateListOf(
            Radio("Male", false),
            Radio("Female", false),
            Radio("Others", false)
        )
    }

    var FirstName by remember {
        mutableStateOf("")
    }
    var LastName by remember {
        mutableStateOf("")
    }
    var dob by remember {
        mutableStateOf("")
    }
    var age by remember {
        mutableStateOf(0)
    }

    val context = LocalContext.current
    var selectedImage by remember {
        mutableStateOf<Uri?>(null)
    }
    var selectIndex by remember {
        mutableIntStateOf(0)
    }
    val imageLauncherPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { selectedImage = it }
    )
    Box(modifier =modifier.fillMaxSize())
    {
   Image(painter = painterResource(id = R.drawable.img), contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize().alpha(0.3f))
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(vertical = 20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
               Text(text = "Enter the Necessary Imformation")
             Spacer(modifier = Modifier.height(10.dp))
            AsyncImage(
                model = selectedImage, contentDescription = null,

                modifier = Modifier
                    .size(100.dp) // Set size for the circular image
                    .padding(4.dp)
                    .clip(CircleShape), // Clip the image to circle
                contentScale = ContentScale.Crop
            )// Crop to fit the circle)
            Button(onClick = {
                imageLauncherPicker.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            })
            {
                Text(text = "Upload Image")
            }



            OutlinedTextField(value = FirstName, onValueChange = { FirstName = it }, label = {
                Text(text = "enter your First Name")
            })
            Spacer(modifier = Modifier.height(12.dp),)
            OutlinedTextField(value = LastName, onValueChange = { LastName = it }, label = {
                Text(text = "enter your Last Name")
            })
            Spacer(modifier = Modifier.height(12.dp),)

            Text(text = "Select Your Gender")
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {


                items.forEachIndexed { index, item ->
                    Row(verticalAlignment = Alignment.CenterVertically)
                    {
                        RadioButton(
                            selected = index == selectIndex,
                            onClick = { selectIndex = index })


                    }
                    Text(text = item.label)


                }


            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Absolute.SpaceAround
            ) {
                OutlinedTextField(value = dob, onValueChange = { }, label = {
                    Text(text = "Enter your Dob")


                },
                    readOnly = true

                )
                Button(onClick = {
                    openDateDialog(
                        { selectedDay, calculatedage ->
                            dob = selectedDay
                            age = calculatedage
                        }, context
                    )
                }
                )
                {
                    Text(text = "select Date")
                }
            }


            Spacer(modifier = Modifier.padding(20.dp))
            Button(
                onClick = { navController.navigate(constant.home) },
                enabled = age > 18 // This enables the button only if age > 18
            ) {
                Text(text = "Click to confirm that You agree with our necessary terms and Conditions")
            }

        }
    }


}