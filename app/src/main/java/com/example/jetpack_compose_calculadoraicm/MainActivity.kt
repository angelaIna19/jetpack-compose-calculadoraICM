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
            // Aplicamos el tema de la aplicación
            JetpackcomposecalculadoraICMTheme {

                // 1. NAVEGACIÓN: rememberNavController es el "timonel" que controla a dónde vamos
                val navController = rememberNavController()

                // 2. NAVHOST: Es el contenedor donde se intercambiarán las pantallas
                // startDestination define cuál es la pantalla que se ve al abrir la app
                NavHost(navController = navController, startDestination = "captura") {

                    // Definimos la ruta "captura" y qué Composable mostrará
                    composable("captura") {
                        PantallaCaptura(navController)
                    }

                    // Nota: La ruta del resultado se agregará en el commit de navegación (Commit 3)
                }
            }
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

@Composable
fun PantallaCaptura(navController: androidx.navigation.NavController? = null) {
    // 3. ESTADOS: Usamos 'remember' y 'mutableStateOf'
    var nombre by remember { mutableStateOf("") }
    var peso by remember { mutableStateOf("") }
    var altura by remember { mutableStateOf("") }

    // Estados para validaciones
    var errorNombre by remember { mutableStateOf(false) }
    var errorPeso by remember { mutableStateOf(false) }
    var errorAltura by remember { mutableStateOf(false) }

    // 4. LAYOUT (Estructura)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Datos del Usuario", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        // Campo para el Nombre con validación de solo letras y longitud mínima
        OutlinedTextField(
            value = nombre,
            onValueChange = {
                // Filtramos para que solo acepte letras y espacios
                if (it.all { char -> char.isLetter() || char.isWhitespace() }) {
                    nombre = it
                    // Error si está vacío o si es demasiado corto (menos de 3 letras)
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

        // Campo para el Peso con validación de rango realista
        OutlinedTextField(
            value = peso,
            onValueChange = {
                peso = it
                val valor = it.toDoubleOrNull()
                // Error si no es número o si está fuera del rango 10-500 kg
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

        // Campo para la Altura con validación de rango realista
        OutlinedTextField(
            value = altura,
            onValueChange = {
                altura = it
                val valor = it.toDoubleOrNull()
                // Error si no es número o si está fuera del rango 0.5 - 2.5 m
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

        // Botón con validación de rangos
        Button(
            onClick = {
                val p = peso.toDoubleOrNull()
                val a = altura.toDoubleOrNull()

                errorNombre = nombre.isBlank()
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
