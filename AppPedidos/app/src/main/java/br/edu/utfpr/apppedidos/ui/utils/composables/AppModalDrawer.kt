package br.edu.utfpr.apppedidos.ui.utils.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.SupervisedUserCircle
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.edu.utfpr.apppedidos.R
import br.edu.utfpr.apppedidos.ui.theme.AppPedidosTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun AppModalDrawer(
    modifier: Modifier = Modifier,
    drawerState: DrawerState,
    currentRoute: String,
    onClientesPressed: () -> Unit,
    onChatsPressed: () -> Unit,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    content: @Composable () -> Unit
) {
    ModalNavigationDrawer(
        modifier = modifier,
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                currentRoute = currentRoute,
                onClientesPressed = onClientesPressed,
                onChatsPressed = onChatsPressed,
                closeDrawer = { coroutineScope.launch { drawerState.close()} }
            )
        }
    ) {
        content()
    }
}

@Composable
private fun AppDrawer(
    modifier: Modifier = Modifier,
    currentRoute: String,
    onClientesPressed: () -> Unit,
    onChatsPressed: () -> Unit,
    closeDrawer: () -> Unit
) {
    ModalDrawerSheet {
        Column(modifier = modifier.fillMaxSize()) {
            DrawerButton(
                imageVector = Icons.Filled.SupervisedUserCircle,
                label = stringResource(R.string.clientes),
                isSelected = currentRoute == "listClientes",
                action = {
                    onClientesPressed()
                    closeDrawer()
                }
            )
            DrawerButton(
                imageVector = Icons.AutoMirrored.Filled.Chat,
                label = stringResource(R.string.mensagens),
                isSelected = currentRoute == "listChat",
                action = {
                    onChatsPressed()
                    closeDrawer()
                }
            )
        }
    }
}

@Composable
private fun DrawerButton(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    label: String,
    isSelected: Boolean,
    action: () -> Unit,
) {
    val tintColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.secondary
    }
    TextButton(
        onClick = action,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                modifier = Modifier.size(45.dp),
                imageVector = imageVector,
                contentDescription = label,
                tint = tintColor,
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.titleLarge,
                color = tintColor
            )
        }
    }
}

@Preview(showBackground = true, heightDp = 400)
@Composable
fun AppDrawerPreview() {
    AppPedidosTheme {
        AppDrawer(
            currentRoute = "listClientes",
            onChatsPressed = {},
            onClientesPressed = {},
            closeDrawer = {}
        )
    }
}











