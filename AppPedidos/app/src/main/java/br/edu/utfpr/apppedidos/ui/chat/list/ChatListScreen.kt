package br.edu.utfpr.apppedidos.ui.chat.list

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AddComment
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
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
fun ChatListScreen(
    modifier: Modifier = Modifier,
    onNewChatPressed: () -> Unit,
    viewModel: ChatListViewModel = viewModel(factory = ChatListViewModel.Factory),
    openDrawer: () -> Unit,
    onChatPressed: (Chat) -> Unit
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopBar(
                usuario = viewModel.uiState.usuario,
                showActions = viewModel.uiState.isSuccess,
                onRefreshPressed = viewModel::loadChats,
                onLogoutPressed = viewModel::logout,
                openDrawer = openDrawer
            )
        },
        floatingActionButton = {
            if (viewModel.uiState.isSuccess) {
                FloatingActionButton(onClick = onNewChatPressed) {
                    Icon(
                        imageVector = Icons.Filled.AddComment,
                        contentDescription = stringResource(R.string.nova_mensagem)
                    )
                }
            }
        }
    ) { innerPadding ->
        if (viewModel.uiState.isLoadingUser || viewModel.uiState.isLoadingChats) {
            DefaultLoading(
                modifier = Modifier.padding(innerPadding),
                text = "${
                    if (viewModel.uiState.isLoadingUser) {
                        stringResource(R.string.verificando_usuario)
                    } else {
                        stringResource(R.string.carregando_chats)
                    }
                }..."
            )
        } else if (viewModel.uiState.hasErrorLoadingChats) {
            DefaultErrorLoading(
                modifier = Modifier.padding(innerPadding),
                text = stringResource(R.string.erro_carregar_conversas),
                onTryAgainPressed = viewModel::loadChats
            )
        } else if (viewModel.uiState.usuario == null) {
            JoinChatContent(
                modifier = Modifier.padding(innerPadding),
                email = viewModel.uiState.email,
                emailErrorCode = viewModel.uiState.emailErrorCode,
                onEmailChanged = viewModel::onEmailChanged,
                isAccessing = viewModel.uiState.isAccessing,
                onAccessPressed = viewModel::access
            )
        } else {
            ChatList(
                modifier = Modifier.padding(innerPadding),
                usuario = viewModel.uiState.usuario!!,
                chats = viewModel.uiState.chats,
                onChatPressed = onChatPressed
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    modifier: Modifier = Modifier,
    usuario: Usuario? = null,
    showActions: Boolean,
    onRefreshPressed: () -> Unit,
    onLogoutPressed: () -> Unit,
    openDrawer: () -> Unit
) {
    TopAppBar(
        modifier = modifier.fillMaxWidth(),
        colors = usuario?.let {
            TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary
            )
        } ?: TopAppBarDefaults.topAppBarColors(),
        title = {
            usuario?.let {
                Column(
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(stringResource(R.string.mensagens))
                    Text(
                        text = it.nome,
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }
        },
        navigationIcon = {
            IconButton(onClick = openDrawer) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = stringResource(R.string.abrir_menu)
                )
            }
        },
        actions = {
            if (showActions) {
                IconButton(onClick = onRefreshPressed) {
                    Icon(
                        imageVector = Icons.Filled.Refresh,
                        contentDescription = stringResource(R.string.atualizar)
                    )
                }
                IconButton(onClick = onLogoutPressed) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Logout,
                        contentDescription = stringResource(R.string.sair)
                    )
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun TopBarPreview() {
    AppPedidosTheme {
        TopBar(
            usuario = Usuario(
                id = 1,
                nome = "João"
            ),
            showActions = true,
            onLogoutPressed = {},
            onRefreshPressed = {},
            openDrawer = {}
        )
    }
}

@Composable
private fun JoinChatContent(
    modifier: Modifier = Modifier,
    email: String,
    @StringRes
    emailErrorCode: Int?,
    onEmailChanged: (String) -> Unit,
    onAccessPressed: () -> Unit,
    isAccessing: Boolean
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(all = 16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.dica_acessar_chat),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )
        OutlinedTextField(
            label = { Text(stringResource(R.string.email)) },
            value = email,
            onValueChange = onEmailChanged,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            isError = emailErrorCode != null,
            enabled = !isAccessing,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            )
        )
        emailErrorCode?.let {
            Text(
                text = stringResource(it),
                modifier = Modifier.padding(top = 4.dp),
                color = MaterialTheme.colorScheme.error
            )
        }
        ElevatedButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            enabled = !isAccessing,
            onClick = onAccessPressed
        ) {
            if (isAccessing) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(32.dp)
                        .padding(all = 8.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text(stringResource(R.string.acessar))
            }
        }
    }
}

@Preview(showBackground = true, heightDp = 400)
@Composable
private fun JoinChatContentPreview() {
    AppPedidosTheme {
        JoinChatContent(
            email = "",
            emailErrorCode = null,
            onEmailChanged = {},
            onAccessPressed = {},
            isAccessing = false
        )
    }
}

@Composable
private fun ChatList(
    modifier: Modifier = Modifier,
    usuario: Usuario,
    chats: List<Chat>,
    onChatPressed: (Chat) -> Unit
) {
    if (chats.isEmpty()) {
        EmptyList(modifier = modifier)
    } else {
        FilledList(
            modifier = modifier,
            usuario = usuario,
            chats = chats,
            onChatPressed = onChatPressed
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
            text = stringResource(R.string.nenhuma_conversa_localizada),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = stringResource(R.string.dica_iniciar_conversa),
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
    usuario: Usuario,
    chats: List<Chat>,
    onChatPressed: (Chat) -> Unit
) {
    LazyColumn(modifier = modifier) {
        itemsIndexed(chats) { index, chat ->
            ListItem(
                modifier = Modifier.clickable { onChatPressed(chat) },
                headlineContent = {
                    Text(chat.getNome(usuario))
                },
                trailingContent = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = stringResource(R.string.selecionar)
                    )
                }
            )
            if (index < chats.lastIndex) {
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
            usuario = Usuario(id = 1, nome = "João"),
            chats = listOf(
                Chat(
                    id = 1,
                    usuarios = listOf(
                        Usuario(id = 1, nome = "João"),
                        Usuario(id = 2, nome = "José")
                    )
                ),
                Chat(
                    id = 2,
                    usuarios = listOf(
                        Usuario(id = 1, nome = "João"),
                        Usuario(id = 3, nome = "Ana")
                    )
                )
            ),
            onChatPressed = {}
        )
    }
}









