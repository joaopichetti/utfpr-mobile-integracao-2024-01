package br.edu.utfpr.apppedidos.ui.chat.newchat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.edu.utfpr.apppedidos.R
import br.edu.utfpr.apppedidos.data.chat.Chat
import br.edu.utfpr.apppedidos.data.usuario.Usuario
import br.edu.utfpr.apppedidos.ui.theme.AppPedidosTheme
import br.edu.utfpr.apppedidos.ui.utils.composables.DefaultErrorLoading
import br.edu.utfpr.apppedidos.ui.utils.composables.DefaultLoading

@Composable
fun NewChatScreen(
    modifier: Modifier = Modifier,
    viewModel: NewChatViewModel = viewModel(factory = NewChatViewModel.Factory),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onBackPressed: () -> Unit,
    onChatStarted: (Chat) -> Unit
) {
    viewModel.uiState.startedChat?.let(onChatStarted)

    val context = LocalContext.current
    LaunchedEffect(snackbarHostState, viewModel.uiState.hasErrorStartingChat) {
        if (viewModel.uiState.hasErrorStartingChat) {
            snackbarHostState.showSnackbar(context.getString(R.string.erro_iniciar_conversa))
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopBar(onBackPressed = onBackPressed)
        }
    ) { innerPadding ->
        if (viewModel.uiState.isLoading || viewModel.uiState.isStartingChat) {
            val messageCode = if (viewModel.uiState.isLoading) {
                R.string.carregando_usuarios
            } else {
                R.string.iniciando_conversa
            }
            DefaultLoading(
                modifier = Modifier.padding(innerPadding),
                text = stringResource(messageCode)
            )
        } else if (viewModel.uiState.hasErrorLoading) {
            DefaultErrorLoading(
                modifier = Modifier.padding(innerPadding),
                text = stringResource(R.string.erro_carregar_usuarios),
                onTryAgainPressed = viewModel::load
            )
        } else {
            Usuarios(
                modifier = Modifier.padding(innerPadding),
                usuarios = viewModel.uiState.usuarios,
                onUsuarioPressed = viewModel::onUsuarioSelected
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit
) {
    TopAppBar(
        modifier = modifier.fillMaxWidth(),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary
        ),
        title = { Text(text = stringResource(R.string.selecione_um_usurio)) },
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.voltar)
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun TopBarPreview() {
    AppPedidosTheme {
        TopBar(
            onBackPressed = {}
        )
    }
}

@Composable
private fun Usuarios(
    modifier: Modifier = Modifier,
    usuarios: List<Usuario>,
    onUsuarioPressed: (Usuario) -> Unit
) {
    if (usuarios.isEmpty()) {
        EmptyList(modifier = modifier)
    } else {
        FilledList(
            modifier = modifier,
            usuarios = usuarios,
            onUsuarioPressed = onUsuarioPressed
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
            text = "${stringResource(R.string.nenhum_usuario_encontrado)}...",
            style = MaterialTheme.typography.titleLarge,
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
    usuarios: List<Usuario>,
    onUsuarioPressed: (Usuario) -> Unit
) {
    LazyColumn(modifier = modifier) {
        itemsIndexed(usuarios) { index, usuario ->
            ListItem(
                modifier = Modifier.clickable { onUsuarioPressed(usuario) },
                headlineContent = {
                    Text(usuario.nome)
                }
            )
            if (index < usuarios.lastIndex) {
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FilledListPreview() {
    AppPedidosTheme {
        FilledList(
            usuarios = listOf(
                Usuario(
                    id = 1,
                    nome = "João Guilherme"
                ),
                Usuario(
                    id = 2,
                    nome = "José Carlos"
                ),
                Usuario(
                    id = 3,
                    nome = "Ana Laura"
                )
            ),
            onUsuarioPressed = {}
        )
    }
}
















