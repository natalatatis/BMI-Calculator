package com.example.bmi_calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.text.input
import androidx.compose.ui.graphics.Color

//Color variables for each category
val redColor = Color(0xFFFF1744) // red
val orangeColor = Color(0xFFFF9100) // Orange
val yellowColor = Color(0xFFFFEB3B) // Yellow
val greenColor = Color(0xFF4CAF50) // Green



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
                val bmi = bmiResult?.toFloatOrNull()

                if(bmi != null){
                    //set the BMI to categories and add colors depending on them
                    val(categoryText, categoryColor) = when{
                        bmi < 16 -> "Severely Underweight" to redColor
                        bmi in 16.0..16.9 -> "Underweight" to orangeColor
                        bmi in  17.0..18.4 -> "Slightly Underweight" to yellowColor
                        bmi in 18.5..24.9 -> "Healthy Weight" to greenColor
                        bmi in 25.0..29.9 -> "Overweight" to yellowColor
                        bmi in 30.0..34.9 -> "Obese (Class I)" to orangeColor
                        else -> "Obese (Class II+)" to redColor
                    }
                    Text("Your BMI is $bmi")
                    Text(categoryText, color = categoryColor)

                    //Box with the color depending the category
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .padding(top = 8.dp)
                            .background(categoryColor)
                    )

                }

            }

        }
    )
}