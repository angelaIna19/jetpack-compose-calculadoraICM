package com.example.jetpack_compose_calculadoraicm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.jetpack_compose_calculadoraicm.ui.theme.JetpackcomposecalculadoraICMTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JetpackcomposecalculadoraICMTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "captura") {
                    composable("captura") {
                        PantallaCaptura(navController)
                    }
                    composable("resultado/{nombre}/{imc}") { backStackEntry ->
                        val userNombre = backStackEntry.arguments?.getString("nombre") ?: ""
                        val userImc = backStackEntry.arguments?.getString("imc") ?: "0.0"
                        PantallaResultado(userNombre, userImc, navController)
                    }
                }
            }
        }
    }
}

@Composable
fun PantallaCaptura(navController: androidx.navigation.NavController? = null) {
    var nombre by remember { mutableStateOf("") }
    var peso by remember { mutableStateOf("") }
    var altura by remember { mutableStateOf("") }

    var errorNombre by remember { mutableStateOf(false) }
    var errorPeso by remember { mutableStateOf(false) }
    var errorAltura by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Datos del Usuario", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = nombre,
            onValueChange = {
                if (it.all { char -> char.isLetter() || char.isWhitespace() }) {
                    nombre = it
                    errorNombre = it.isBlank() || it.trim().length < 3
                }
            },
            label = { Text("Nombre") },
            isError = errorNombre,
            supportingText = { 
                if (errorNombre) {
                    val mensaje = when {
                        nombre.isBlank() -> "El nombre no puede estar vacío"
                        nombre.trim().length < 3 -> "El nombre debe tener al menos 3 letras"
                        else -> "Solo se permiten letras"
                    }
                    Text(mensaje)
                } 
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = peso,
            onValueChange = {
                peso = it
                val valor = it.toDoubleOrNull()
                errorPeso = valor == null || valor < 10.0 || valor > 500.0
            },
            label = { Text("Peso (kg)") },
            isError = errorPeso,
            supportingText = { if (errorPeso) Text("Ingresa un peso realista (10 - 500 kg)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = altura,
            onValueChange = {
                altura = it
                val valor = it.toDoubleOrNull()
                errorAltura = valor == null || valor < 0.5 || valor > 2.5
            },
            label = { Text("Altura (m)") },
            isError = errorAltura,
            supportingText = { if (errorAltura) Text("Ingresa una altura realista (0.5 - 2.5 m)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val p = peso.toDoubleOrNull()
                val a = altura.toDoubleOrNull()
                errorNombre = nombre.isBlank() || nombre.trim().length < 3
                errorPeso = p == null || p < 10.0 || p > 500.0
                errorAltura = a == null || a < 0.5 || a > 2.5

                if (!errorNombre && !errorPeso && !errorAltura && p != null && a != null) {
                    val imc = p / (a * a)
                    navController?.navigate("resultado/$nombre/${String.format("%.2f", imc)}")
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Calcular")
        }
    }
}
@Composable
fun PantallaResultado(nombre: String, imc: String, navController: androidx.navigation.NavController) {
    // 1. Convertimos el texto a número de forma segura con el operador Elvis (?:)
    val imcValor = imc.replace(",", ".").toDoubleOrNull() ?: 0.0

    // 2. Lógica inteligente con 'when' siguiendo los requisitos de colores de la tarea
    val (categoria, color) = when {
        imcValor < 18.5 -> "Bajo peso" to androidx.compose.ui.graphics.Color.Red
        imcValor < 25.0 -> "Peso normal" to androidx.compose.ui.graphics.Color.Green
        imcValor < 30.0 -> "Sobrepeso" to androidx.compose.ui.graphics.Color(0xFFFFA500) // Naranja
        else -> "Obesidad" to androidx.compose.ui.graphics.Color.Red
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f))
        ) {
            Column(
                modifier = Modifier.padding(24.dp).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "¡Hola $nombre!", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Tu IMC es de:", style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = imc,
                    style = MaterialTheme.typography.displayLarge,
                    color = color
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = categoria,
                    style = MaterialTheme.typography.headlineMedium,
                    color = color
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth(0.7f)
        ) {
            Text("Volver a calcular")
        }
    }
}

@Preview(showBackground = true, name = "Vista Previa de Captura")
@Composable
fun PreviewPantallaCaptura() {
    JetpackcomposecalculadoraICMTheme {
        PantallaCaptura()
    }
}
