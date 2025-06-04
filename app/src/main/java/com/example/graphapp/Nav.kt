package com.example.graphapp

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

@Composable
fun SetupNavigation(navController: NavHostController, graph: NavigationGraph) {
    NavHost(
        navController = navController,
        startDestination = "start"
    ) {
        graph.routes.forEach { route ->
            composable(route.id) {
                Text(text = "Route ${route.id}")
            }
        }
    }
}

@Composable
fun NavigationGraphScreen() {
    val context = LocalContext.current
    var graph by remember { mutableStateOf<NavigationGraph?>(null) }
    var errors by remember { mutableStateOf<List<String>>(emptyList()) }

    Column {
        Button(onClick = {
            val loadedGraph = NavigationGraph(emptyList())
            if (loadedGraph != null) {
                graph = loadedGraph
                errors = analyzeNavigationGraph(loadedGraph)
            } else {
                errors = listOf("Failed to load graph")
            }
        }) {
            Text("Load and Analyze Graph")
        }

        errors.forEach { error ->
            Text(text = "Error: $error")
        }

        if (graph != null) {
            SetupNavigation(graph = graph!!)
        }
    }
}