package com.example.graphapp

import kotlin.system.measureTimeMillis

fun main() {
    val logger = GraphNavigationLogger()
    val metrics = GraphRemoteMetrics(metricsBackend = "console")

    fun executeQuery(query: String): List<String> {

        val startTime = System.currentTimeMillis()

        try {

            val result = executeGraphQuery(query)
            val resultCount = result.size
            val endTime = System.currentTimeMillis()
            val executionTime = (endTime - startTime) / 1000.0


            logger.logQuery(query, resultCount, executionTime)


            metrics.sendQueryMetrics(query, executionTime, resultCount)

            return result
        } catch (e: Exception) {
            logger.logError("Error executing query", e)
            throw e
        }
    }

    fun handleApiRequest(endpoint: String): String {
        val latency = measureTimeMillis {

            Thread.sleep((1000..3000).random().toLong())

        } / 1000.0

        metrics.sendApiLatency(endpoint, latency)

        return "Endpoint $endpoint processed in ${"%.4f".format(latency)}s"
    }

    val queryResult = executeQuery("MATCH (n) RETURN n LIMIT 10")
    println("Query Result: $queryResult")

    val apiResponse = handleApiRequest("/data")
    println(apiResponse)
}

fun executeGraphQuery(query: String): List<String> {

    val results = mutableListOf<String>()
    for (i in 1..5) {
        results.add("Result $i from query: $query")
    }
    return results
}