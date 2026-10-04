package br.com.fastthink.todolist.ui.feature.list

sealed interface ListEvent {
    data class Delete(val id: Long) : ListEvent
    data class CheckItem(val id: Long, val isDone: Boolean) : ListEvent

    data class AddEdit(val id: Long?) : ListEvent
}