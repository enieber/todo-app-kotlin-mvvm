package br.com.fastthink.todolist.ui.feature.list

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.fastthink.todolist.R
import br.com.fastthink.todolist.data.TodoDatabaseProvider
import br.com.fastthink.todolist.data.TodoRepositoryImpl
import br.com.fastthink.todolist.domain.Todo
import br.com.fastthink.todolist.domain.todo1
import br.com.fastthink.todolist.domain.todo2
import br.com.fastthink.todolist.domain.todo3
import br.com.fastthink.todolist.navigation.AddEditRoute
import br.com.fastthink.todolist.ui.UiEvent
import br.com.fastthink.todolist.ui.components.TodoItem
import br.com.fastthink.todolist.ui.feature.addedit.AddEditViewModel
import br.com.fastthink.todolist.ui.theme.TodoListTheme

@Composable
fun ListScreen(
    navigateToAddEditScreen: (id: Long?) -> Unit,
) {
    val context = LocalContext.current.applicationContext
    val database = TodoDatabaseProvider.provider(context)
    val repository = TodoRepositoryImpl(
        dao = database.todoDao
    )
    val viewModel = viewModel<ListViewModel> {
        ListViewModel(repository = repository)
    }

    val todos by viewModel.todos.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { uiEvent ->
            when(uiEvent) {
                is UiEvent.Navigate<*> -> {
                    when (uiEvent.route) {
                        is AddEditRoute -> {
                            navigateToAddEditScreen(uiEvent.route.id)
                        }
                    }
                }
                is UiEvent.NavigateBack -> {}
                is UiEvent.ShowSnackbar -> {}
            }
        }
    }

    ListContent(
        todos = todos,
        onEvent = viewModel::onEvent,
    )
}

@Composable
fun ListContent(
    todos: List<Todo>,
    onEvent: (ListEvent) -> Unit,
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { onEvent(ListEvent.AddEdit(null)) }) {
                Icon(
                    painter = painterResource(R.drawable.ic_add),
                    contentDescription = "Add",
                )
            }
        }
    ) {
        paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .consumeWindowInsets(paddingValues),
                contentPadding = PaddingValues(16.dp)
            ) {
                itemsIndexed(todos) { index, todo ->
                    TodoItem(
                        todo = todo,
                        onDeleteClick = {
                            onEvent(ListEvent.Delete(todo.id))
                        },
                        onItemClick = {
                            onEvent(ListEvent.AddEdit(todo.id))
                        },
                        onDoneChange = { isDone ->
                            onEvent(ListEvent.CheckItem(id = todo.id, isDone = isDone))
                        },
                    )

                    if (index < todos.lastIndex) {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
    }
}

@Preview
@Composable
private fun ListContentPreview() {
    TodoListTheme {
        ListContent(
            todos = listOf(
                todo1,
                todo2,
                todo3
            ),
            onEvent = {}
        )
    }
}
