package org.insbaixcamp.imccalculadora

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.util.rangeTo
import org.insbaixcamp.imccalculadora.ui.theme.ImcCalculadoraTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ImcCalculadoraTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    BMIScreen()
                }
            }
        }
    }
}

@Composable
fun BMIScreen(){
    // Variables
    var nom: String by remember { mutableStateOf(value = "") }
    var pes: Int by remember { mutableStateOf(value = 80) }
    var alçada: Int by remember { mutableStateOf(value = 180) }
    var imc: Double by remember { mutableStateOf(value = 0.0) }

    Column() {
        Text(text = "Calculadora IMC")
        // Per posar el nom
        TextField(
            value = nom,
            onValueChange = {nom = it},
            label = { Text(text = "Nom")}
        )

        Row(){
            Text(text = "Pes: $pes Kg")
            // Botó +
            Button(
                onClick = {pes++}
            ) {
                Text(text = "+")
            }

            // Boto -
            Button(
                onClick = {pes--}
            ) {
                Text(text = "-")
            }
        }

        Text(text = "Alçada: $alçada cm")
        Slider(
            value = alçada.toFloat(),
            onValueChange = {alçada = it.toInt()},
            valueRange = 100f..250f
        )

        Button(onClick = {
            var alçadaMetres = alçada.toDouble() / 100
            imc = pes / (alçadaMetres * alçadaMetres)
        }) {
            Text(text = "Calcular IMC")
        }
        imc?.let{
            Text(text = "IMC: $imc")
    }
    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ImcCalculadoraTheme {
        BMIScreen()
    }
}