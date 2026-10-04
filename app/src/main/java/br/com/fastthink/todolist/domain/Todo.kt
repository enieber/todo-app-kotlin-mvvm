package br.com.fastthink.todolist.domain

data class Todo(
    val id: Long,
    val title: String,
    val description: String?,
    val isDone: Boolean,
)

// fake objects
val todo1 = Todo(
    id = 1,
    title = "Task 1",
    description = "description 1",
    isDone = false,
)

val todo2 = Todo(
    id = 2,
    title = "Task 2",
    description = "description 2",
    isDone = true,
)

val todo3 = Todo(
    id = 3,
    title = "Task 3",
    description = "description 3",
    isDone = true,
)
