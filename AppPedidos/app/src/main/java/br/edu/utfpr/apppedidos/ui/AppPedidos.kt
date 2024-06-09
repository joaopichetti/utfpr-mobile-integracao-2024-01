package br.edu.utfpr.apppedidos.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.edu.utfpr.apppedidos.ui.cliente.details.ClienteDetailsScreen
import br.edu.utfpr.apppedidos.ui.cliente.form.ClienteFormScreen
import br.edu.utfpr.apppedidos.ui.cliente.list.ClientesListScreen

@Composable
fun AppPedidos(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = "listClientes",
        modifier = modifier
    ) {
        composable(route = "listClientes") {
            ClientesListScreen(
                onClientePressed = { cliente ->
                    navController.navigate("clienteDetails/${cliente.id}")
                },
                onAddPressed = {
                    navController.navigate("clienteForm")
                }
            )
        }
        composable(
            route = "clienteDetails/{id}",
            arguments = listOf(
                navArgument(name = "id") { type = NavType.IntType }
            )
        ) { navBackStackEntry ->
            ClienteDetailsScreen(
                onBackPressed = {
                    navController.popBackStack()
                },
                onClienteDeleted = {
                    navController.navigate("listClientes") {
                        popUpTo(navController.graph.findStartDestination().id) {
                            inclusive = true
                        }
                    }
                },
                onEditPressed = {
                    val clienteId = navBackStackEntry.arguments?.getInt("id") ?: 0
                    navController.navigate("clienteForm?id=$clienteId")
                }
            )
        }
        composable(
            route = "clienteForm?id={id}",
            arguments = listOf(
                navArgument(name = "id") { type = NavType.StringType; nullable = true}
            )
        ) {
            ClienteFormScreen(
                onBackPressed = {
                    navController.popBackStack()
                },
                onClienteSaved = {
                    navController.navigate("listClientes") {
                        popUpTo(navController.graph.findStartDestination().id) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}
