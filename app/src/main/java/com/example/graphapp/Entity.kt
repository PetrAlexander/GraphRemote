package com.example.graphapp

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class NavigationGraph(
    val routes: List<Route>
)

@Serializable
data class Route(
    val id: String,
    val destination: String,
    val arguments: List<Argument> = emptyList()
)

@Serializable
data class Argument(
    val name: String,
    val type: String
)