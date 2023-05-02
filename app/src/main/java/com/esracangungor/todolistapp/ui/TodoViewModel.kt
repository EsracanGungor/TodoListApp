package com.esracangungor.todolistapp.ui

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.esracangungor.todolistapp.data.TodoDatabase
import com.esracangungor.todolistapp.data.TodoItem
import com.esracangungor.todolistapp.data.TodoRepository

class TodoViewModal(application: Application) : AndroidViewModel(application) {

    val allTodos: LiveData<List<TodoItem>>
    private val repository: TodoRepository

    init {
        val dao = TodoDatabase.getDatabase(application).getTodoDao()
        repository = TodoRepository(dao)
        allTodos = repository.allTodos()
    }

    fun deleteTodo(todo: TodoItem) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(todo)
    }

    fun updateTodo(todo: TodoItem) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(todo)
    }

    fun addTodo(todo: TodoItem) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(todo)
    }
}