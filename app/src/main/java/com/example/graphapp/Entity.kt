package com.example.graphapp


data class NavigationGraph(
    val routes: List<Route>
)

data class Route(
    val id: String,
    val destination: String,
    val arguments: List<Argument> = emptyList()
)

data class Argument(
    val name: String,
    val type: String
)