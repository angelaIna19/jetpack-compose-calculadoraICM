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
    // 3. ESTADOS: Usamos 'remember' y 'mutableStateOf' para que Compose
    // "recuerde" lo que el usuario escribe y redibuje la pantalla (Recomposición)
    var nombre by remember { mutableStateOf("") }
    var peso by remember { mutableStateOf("") }
    var altura by remember { mutableStateOf("") }

    // 4. LAYOUT (Estructura): Column organiza los elementos uno debajo de otro
    Column(
        modifier = Modifier
            .fillMaxSize() // Ocupa todo el alto y ancho disponible
            .padding(16.dp), // Agrega margen interno (Requisito 1)
        horizontalAlignment = Alignment.CenterHorizontally, // Centra horizontalmente
        verticalArrangement = Arrangement.Center // Centra verticalmente en la pantalla
    ) {
        // Título de la pantalla
        Text(text = "Datos del Usuario", style = MaterialTheme.typography.headlineMedium)

        // 5. SPACER: Crea un espacio vacío entre elementos (Requisito 1)
        Spacer(modifier = Modifier.height(16.dp))

        // Campo para el Nombre
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it }, // Actualiza el estado al escribir
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth() // Se expande a todo el ancho
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo para el Peso
        OutlinedTextField(
            value = peso,
            onValueChange = { peso = it },
            label = { Text("Peso (kg)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo para la Altura
        OutlinedTextField(
            value = altura,
            onValueChange = { altura = it },
            label = { Text("Altura (m)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Calcular")
        }
    }
}
