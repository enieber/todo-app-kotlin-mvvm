package br.com.fastthink.todolist.ui.feature.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fastthink.todolist.data.TodoRepository
import br.com.fastthink.todolist.navigation.AddEditRoute
import br.com.fastthink.todolist.ui.UiEvent
import br.com.fastthink.todolist.ui.feature.addedit.AddEditEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ListViewModel(
    private val repository: TodoRepository,
) : ViewModel() {

    val todos = repository.getAll()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: ListEvent) {
        when(event) {
            is ListEvent.Delete -> {
                delete(event.id)
            }
            is ListEvent.CheckItem -> {
                checkItem(event.id, event.isDone)
            }
            is ListEvent.AddEdit -> {
                viewModelScope.launch {
                    _uiEvent.send(UiEvent.Navigate(AddEditRoute(event.id)))
                }
            }
        }
    }

    private fun delete(id: Long) {
        viewModelScope.launch {
            repository.delete(id)
        }
    }

    private fun checkItem(id: Long, isDone: Boolean) {
        viewModelScope.launch {
            repository.updateDone(id, isDone)
        }
    }

}