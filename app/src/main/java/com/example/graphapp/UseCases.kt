package com.example.graphapp

import android.content.Context
import androidx.annotation.RawRes

fun readNavigationGraphFromJson(context: Context, @RawRes resourceId: Int): NavigationGraph? {
    return try {
        val inputStream = context.resources.openRawResource(resourceId)
        Json.decodeFromStream(NavigationGraph.serializer(), inputStream)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun analyzeNavigationGraph(graph: NavigationGraph): List<String> {
    val errors = mutableListOf<String>()

    val routeIds = graph.routes.map { it.id }.toSet()
    graph.routes.forEach { route ->
        if (route.destination !in routeIds) {
            errors.add("Route ${route.id}: Destination ${route.destination} not found")
        }
    }

    return errors
}

fun optimizeNavigationGraph(graph: NavigationGraph): NavigationGraph {
    val usedRoutes = mutableSetOf<String>()
    fun findUsedRoutes(routeId: String) {
        usedRoutes.add(routeId)
        val route = graph.routes.find { it.id == routeId } ?: return
        findUsedRoutes(route.destination)
    }

    findUsedRoutes("start")

    val optimizedRoutes = graph.routes.filter { it.id in usedRoutes }
    return graph.copy(routes = optimizedRoutes)
}