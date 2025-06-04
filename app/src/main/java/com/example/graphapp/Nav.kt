package com.example.graphapp

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
    val navController = rememberNavController()

    Column {
        Button(onClick = {
            val loadedGraph = readNavigationGraphFromJson(context, R.raw.navigation_graph) // R.raw.navigation_graph - твой json файл
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
            SetupNavigation(navController = navController, graph = graph!!)
        }
    }
}