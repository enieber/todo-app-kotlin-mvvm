package br.com.fastthink.todolist.data

import br.com.fastthink.todolist.domain.Todo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TodoRepositoryImpl(
    private val dao: TodoDao,
) : TodoRepository {
    override suspend fun insert(title: String, description: String?, id: Long?) {

        val entity = id?.let {
            dao.getBy(it)?.copy(
                title = title,
                description = description,
            )
        } ?: TodoEntity(
            title = title,
            description = description,
            isDone = false,
        )

        dao.insert(entity)
    }

    override suspend fun updateDone(id: Long, isDone: Boolean) {
        val existingEntity = dao.getBy(id) ?: return;
        val updatedEntity = existingEntity.copy(isDone = isDone)
        dao.insert(updatedEntity)
    }

    override suspend fun delete(id: Long) {
        val existingEntity = dao.getBy(id) ?: return;
        dao.delete(existingEntity)
    }

    override fun getAll(): Flow<List<Todo>> {
        return dao.getAll().map { entities ->
            entities.map {
                entity -> mapTodoEntityToTodo(entity)
            }
        }
    }

    override suspend fun gettBy(id: Long): Todo? {
        return dao.getBy(id)?.let {
            entity -> mapTodoEntityToTodo(entity)
        }
    }

    private fun mapTodoEntityToTodo(entity: TodoEntity): Todo {
        return Todo(
            id = entity.id,
            title = entity.title,
            description = entity.description,
            isDone = entity.isDone
        )
    }
}