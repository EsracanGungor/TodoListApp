package com.esracangungor.todolistapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.esracangungor.todolistapp.data.TodoItem
import com.esracangungor.todolistapp.databinding.ActivityTodoListBinding

class TodoListActivity : AppCompatActivity(), TodoClickEditInterface, TodoClickDeleteInterface {
    lateinit var viewModel: TodoViewModal
    private lateinit var binding: ActivityTodoListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTodoListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val rvTodoAdapter = TodoAdapter(this, this)
        binding.rvTodo.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = rvTodoAdapter
        }

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[TodoViewModal::class.java]

        viewModel.allTodos.observe(this) { list ->
            list?.let {
                rvTodoAdapter.updateList(it)
            }
        }

        binding.fabAdd.setOnClickListener {
            val intent = Intent(this@TodoListActivity, AddEditTodoActivity::class.java)
            startActivity(intent)
            this.finish()
        }
    }

    override fun onEditIconClick(todoItem: TodoItem) {
        val intent = Intent(this@TodoListActivity, AddEditTodoActivity::class.java)
            .apply {
                putExtra(TODO_TYPE, TODO_TYPE_EDIT)
                putExtra(TODO_CATEGORY, todoItem.todoCategory)
                putExtra(TODO_TITLE, todoItem.todoTitle)
                putExtra(TODO_DESC, todoItem.todoDescription)
                putExtra(TODO_ID, todoItem.id)
            }
        startActivity(intent)
        this.finish()
    }

    override fun onDeleteIconClick(todoItem: TodoItem) {
        viewModel.deleteTodo(todoItem)
        Toast.makeText(this, "${todoItem.todoTitle} Deleted", Toast.LENGTH_LONG).show()
    }

    companion object {
        const val TODO_TYPE: String = "todoType"
        const val TODO_TYPE_EDIT: String = "edit"
        const val TODO_TYPE_DELETE: String = "delete"
        const val TODO_CATEGORY: String = "todoCategory"
        const val TODO_TITLE: String = "todoTitle"
        const val TODO_DESC: String = "todoDesc"
        const val TODO_ID: String = "todoId"
    }
}