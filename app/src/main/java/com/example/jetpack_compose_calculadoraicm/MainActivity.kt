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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
                        PantallaCaptura()
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
fun PantallaCaptura() {
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

        // Campo para el Nombre con validación
        OutlinedTextField(
            value = nombre,
            onValueChange = {
                nombre = it
                errorNombre = it.isBlank()
            },
            label = { Text("Nombre") },
            isError = errorNombre,
            supportingText = { if (errorNombre) Text("El nombre no puede estar vacío") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo para el Peso con validación numérica
        OutlinedTextField(
            value = peso,
            onValueChange = {
                peso = it
                // Valida que sea un número válido
                errorPeso = it.toDoubleOrNull() == null || it.toDouble() <= 0
            },
            label = { Text("Peso (kg)") },
            isError = errorPeso,
            supportingText = { if (errorPeso) Text("Ingresa un peso válido (ej: 70.5)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo para la Altura con validación numérica
        OutlinedTextField(
            value = altura,
            onValueChange = {
                altura = it
                // Valida que sea un número válido
                errorAltura = it.toDoubleOrNull() == null || it.toDouble() <= 0
            },
            label = { Text("Altura (m)") },
            isError = errorAltura,
            supportingText = { if (errorAltura) Text("Ingresa una altura válida (ej: 1.75)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Botón con lógica de validación
        Button(
            onClick = {
                // Verificación final antes de avanzar
                errorNombre = nombre.isBlank()
                errorPeso = peso.toDoubleOrNull() == null
                errorAltura = altura.toDoubleOrNull() == null

                if (!errorNombre && !errorPeso && !errorAltura) {
                    // Aquí irá la navegación en el próximo commit
                    println("Datos válidos: $nombre, $peso, $altura")
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Calcular")
        }
    }
}
