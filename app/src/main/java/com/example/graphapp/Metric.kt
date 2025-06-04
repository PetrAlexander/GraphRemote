package com.example.graphapp

fun main() {
    // Load configuration (choose ONE of these metrics backends based on your config)
    val config = Properties() //Or fetch the config from application.properties
    val metricsBackend = config.getProperty("metrics.backend", "console") // Default to console

    val prometheusMetrics: PrometheusGraphRemoteMetrics? = if (metricsBackend == "prometheus") PrometheusGraphRemoteMetrics() else null
    val statsDMetrics: StatsDGraphRemoteMetrics? = if (metricsBackend == "statsd") StatsDGraphRemoteMetrics() else null
    //val logger = GraphNavigationLogger()

    fun executeQuery(query: String): List<String> {
        val startTime = System.currentTimeMillis()

        try {
            // Execute the query against the graph database
            val result = executeGraphQuery(query)
            val resultCount = result.size
            val endTime = System.currentTimeMillis()
            val executionTime = (endTime - startTime) / 1000.0 // Convert to seconds

            //logger.logQuery(query, resultCount, executionTime) //Logging example

            // Record Metrics based on the backend being leveraged.
            prometheusMetrics?.recordQuery(query)
            prometheusMetrics?.recordQueryExecutionTime(query, executionTime)

            statsDMetrics?.incrementQueryCount(query)
            statsDMetrics?.recordQueryExecutionTime(query, executionTime)

            return result
        } catch (e: Exception) {
            //logger.logError("Error executing query", e) // Logging error example
            throw e
        }
    }

    // ... (Rest of your application logic)

    //Make sure you close out your metrics reporter, if you enable statsd
    statsDMetrics?.close()

}