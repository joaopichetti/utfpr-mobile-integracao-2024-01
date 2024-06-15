package br.edu.utfpr.apppedidos.ui.cliente.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.edu.utfpr.apppedidos.R
import br.edu.utfpr.apppedidos.data.cliente.Cliente
import br.edu.utfpr.apppedidos.ui.theme.AppPedidosTheme
import br.edu.utfpr.apppedidos.ui.utils.composables.DefaultErrorLoading
import br.edu.utfpr.apppedidos.ui.utils.composables.DefaultLoading

@Composable
fun ClientesListScreen(
    modifier: Modifier = Modifier,
    viewModel: ClientesListViewModel = viewModel(),
    onClientePressed: (Cliente) -> Unit,
    onAddPressed: () -> Unit,
    openDrawer: () -> Unit
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            ClientesTopBar(
                onRefresh = viewModel::load,
                showRefreshAction = viewModel.uiState.success,
                openDrawer = openDrawer
            )
        },
        floatingActionButton = {
            if (viewModel.uiState.success) {
                FloatingActionButton(onClick = onAddPressed) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(R.string.adicionar)
                    )
                }
            }
        }
    ) { paddingValues ->
        if (viewModel.uiState.loading) {
            DefaultLoading(
                modifier = Modifier.padding(paddingValues),
                text = "${stringResource(R.string.carregando_clientes)}..."
            )
        } else if (viewModel.uiState.hasError) {
            DefaultErrorLoading(
                modifier = Modifier.padding(paddingValues),
                text = stringResource(R.string.erro_ao_carregar_clientes),
                onTryAgainPressed = viewModel::load
            )
        } else {
            ClientesList(
                modifier = Modifier.padding(paddingValues),
                clientes = viewModel.uiState.clientes,
                onClientePressed = onClientePressed
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ClientesTopBar(
    modifier: Modifier = Modifier,
    onRefresh: () -> Unit,
    showRefreshAction: Boolean,
    openDrawer: () -> Unit
) {
    TopAppBar(
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary
        ),
        title = { Text(stringResource(R.string.clientes)) },
        navigationIcon = {
            IconButton(onClick = openDrawer) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = stringResource(R.string.abrir_menu)
                )
            }
        },
        actions = {
            if (showRefreshAction) {
                IconButton(onClick = onRefresh) {
                    Icon(
                        imageVector = Icons.Filled.Refresh,
                        contentDescription = stringResource(R.string.atualizar)
                    )
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun ClientesTopBarPreview() {
    AppPedidosTheme {
        ClientesTopBar(
            onRefresh = {},
            showRefreshAction = true,
            openDrawer = {}
        )
    }
}

@Composable
private fun ClientesList(
    modifier: Modifier = Modifier,
    clientes: List<Cliente> = listOf(),
    onClientePressed: (Cliente) -> Unit
) {
    if (clientes.isEmpty()) {
        EmptyList(modifier = modifier)
    } else {
        FilledList(
            modifier = modifier,
            clientes = clientes,
            onClientePressed = onClientePressed
        )
    }
}

@Composable
private fun EmptyList(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Nenhum cliente encontrado",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = "Adicione algum pressionando o \"+\"",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Preview(showBackground = true, heightDp = 400)
@Composable
private fun EmptyListPreview() {
    AppPedidosTheme {
        EmptyList()
    }
}

@Composable
private fun FilledList(
    modifier: Modifier = Modifier,
    clientes: List<Cliente>,
    onClientePressed: (Cliente) -> Unit,
) {
    LazyColumn(
        modifier = modifier.padding(vertical = 4.dp)
    ) {
        itemsIndexed(clientes) { index, cliente ->
            ListItem(
                modifier = Modifier
                    .padding(8.dp)
                    .clickable { onClientePressed(cliente) },
                headlineContent = {
                    Text(
                        text = "${cliente.id} - ${cliente.nome}",
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                supportingContent = {
                    Text(
                        text = cliente.endereco.descricao,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                trailingContent = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Selecionar",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            )
            if (index < clientes.lastIndex) {
                HorizontalDivider()
            }
        }
    }
}

@Preview(showBackground = true, heightDp = 400)
@Composable
private fun FilledListPreview() {
    AppPedidosTheme {
        FilledList(
            clientes = clientesFake,
            onClientePressed = {}
        )
    }
}
