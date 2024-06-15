package br.edu.utfpr.apppedidos.ui

import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.edu.utfpr.apppedidos.ui.chat.list.ChatListScreen
import br.edu.utfpr.apppedidos.ui.cliente.details.ClienteDetailsScreen
import br.edu.utfpr.apppedidos.ui.cliente.form.ClienteFormScreen
import br.edu.utfpr.apppedidos.ui.cliente.list.ClientesListScreen
import br.edu.utfpr.apppedidos.ui.utils.composables.AppModalDrawer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private object Screens {
    const val LIST_CLIENTES = "listClientes"
    const val CLIENTE_DETAILS = "clienteDetails"
    const val CLIENTE_FORM = "clienteForm"
    const val LIST_CHAT = "listChat"
}

object Arguments {
    const val ID = "id"
    const val CHAT_ID = "chatId"
}

private object Routes {
    const val LIST_CLIENTES = Screens.LIST_CLIENTES
    const val CLIENTE_DETAILS = "${Screens.CLIENTE_DETAILS}/{${Arguments.ID}}"
    const val CLIENTE_FORM = "${Screens.CLIENTE_FORM}?${Arguments.ID}={${Arguments.ID}}"
    const val LIST_CHAT = Screens.LIST_CHAT
}

@Composable
fun AppPedidos(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    startRoute: String = Routes.LIST_CLIENTES
) {
    val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentNavBackStackEntry?.destination?.route ?: startRoute

    NavHost(
        navController = navController,
        startDestination = startRoute,
        modifier = modifier
    ) {
        composable(route = Routes.LIST_CLIENTES) {
            AppModalDrawer(
                drawerState = drawerState,
                currentRoute = currentRoute,
                onClientesPressed = { navigateToListClientes(navController) },
                onChatsPressed = { navigateToListChats(navController) }
            ) {
                ClientesListScreen(
                    onClientePressed = { cliente ->
                        navController.navigate("${Screens.CLIENTE_DETAILS}/${cliente.id}")
                    },
                    onAddPressed = {
                        navController.navigate(Screens.CLIENTE_FORM)
                    },
                    openDrawer = {
                        coroutineScope.launch { drawerState.open() }
                    }
                )
            }
        }
        composable(
            route = Routes.CLIENTE_DETAILS,
            arguments = listOf(
                navArgument(name = Arguments.ID) { type = NavType.IntType }
            )
        ) { navBackStackEntry ->
            ClienteDetailsScreen(
                onBackPressed = {
                    navController.popBackStack()
                },
                onClienteDeleted = {
                    navigateToListClientes(navController)
                },
                onEditPressed = {
                    val clienteId = navBackStackEntry.arguments?.getInt(Arguments.ID) ?: 0
                    navController.navigate("${Screens.CLIENTE_FORM}?${Arguments.ID}=$clienteId")
                }
            )
        }
        composable(
            route = Routes.CLIENTE_FORM,
            arguments = listOf(
                navArgument(name = Arguments.ID) { type = NavType.StringType; nullable = true}
            )
        ) {
            ClienteFormScreen(
                onBackPressed = {
                    navController.popBackStack()
                },
                onClienteSaved = {
                    navigateToListClientes(navController)
                }
            )
        }
        composable(route = Routes.LIST_CHAT) {
            AppModalDrawer(
                drawerState = drawerState,
                currentRoute = currentRoute,
                onClientesPressed = { navigateToListClientes(navController) },
                onChatsPressed = { navigateToListChats(navController) }
            ) {
                ChatListScreen(
                    onNewChatPressed = {},
                    onChatPressed = { chat -> },
                    openDrawer = {
                        coroutineScope.launch { drawerState.open() }
                    }
                )
            }
        }
    }
}

private fun navigateToListClientes(navController: NavHostController) {
    navController.navigate(Routes.LIST_CLIENTES) {
        popUpTo(navController.graph.findStartDestination().id) {
            inclusive = true
        }
    }
}

private fun navigateToListChats(navController: NavHostController) {
    navController.navigate(Routes.LIST_CHAT) {
        popUpTo(navController.graph.findStartDestination().id) {
            inclusive = true
        }
    }
}