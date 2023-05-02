package com.esracangungor.todolistapp.data


class TodoRepository(private val todoDao: TodoDao) {

    fun allTodos() = todoDao.getAllTodoItems()

    fun allTodosByCategory(category: String?) = todoDao.getAllTodoItemsByCategory(category)

    suspend fun insert(todo: TodoItem) {
        todoDao.insert(todo)
    }

    suspend fun delete(todo: TodoItem) {
        todoDao.delete(todo)
    }

    suspend fun update(todo: TodoItem) {
        todoDao.update(todo)
    }
}