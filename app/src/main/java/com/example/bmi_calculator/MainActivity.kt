package com.example.bmi_calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.text.input.


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme{
                BMIScreen()
            }
        }
    }
}

@Composable
fun BMIScreen(){
    var weight by remember { mutableStateOf("")}
    var height by remember { mutableStateOf("")}
    var bmiResult by remember { mutableStateOf<String?>(null)}
    var errorMessage by remember { mutableStateOf<String?>(null)}

    fun calculateBMI(){
        val weightVal = weight.toFloatOrNull()
        val heightVal = height.toFloatOrNull()
        //check if any of the values arent numbers
        if(weightVal==null || heightVal==null){
            errorMessage = "Please enter numeric values"
            bmiResult = null
            return
        }

        if(weightVal<1 || heightVal<1){
            errorMessage = "Please enter reasonable values"
            bmiResult = null
            return
        }

        val heightMeters = heightVal / 100
        val bmi = weightVal / (heightMeters * heightMeters)
        bmiResult = String.format("%.2f", bmi)
        errorMessage = null
    }

    @OptIn(ExperimentalMaterial3Api::class)
    Scaffold(
        topBar = {
            TopAppBar(title = {Text("Body Mass Index Calculator")})
        },
        content={ padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(24.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                //text input field for the weight
                OutlinedTextField(value = weight,
                    onValueChange =  {weight=it},
                    label={Text("Weight (kg)")},
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )

                //text input field for the height
                OutlinedTextField(value = height,
                    onValueChange =  {height=it},
                    label={Text("Height (cm)")},
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
                Button(onClick = { calculateBMI()}){
                    Text("Calculate BMI")
                }

                //display error messages
                if(errorMessage!=null){
                    Text(errorMessage!!, color = MaterialTheme.colorScheme.error)
                }

                //display BMI result
                if(bmiResult!=null){
                    Text("Your BMI is $bmiResult")
                }
            }
        }
    )
}