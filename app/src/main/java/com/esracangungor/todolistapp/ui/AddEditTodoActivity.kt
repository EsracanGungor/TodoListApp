package com.esracangungor.todolistapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.esracangungor.todolistapp.R
import com.esracangungor.todolistapp.data.TodoItem
import com.esracangungor.todolistapp.databinding.ActivityAddEditTodoBinding
import com.esracangungor.todolistapp.ui.TodoListActivity.Companion.TODO_DESC
import com.esracangungor.todolistapp.ui.TodoListActivity.Companion.TODO_ID
import com.esracangungor.todolistapp.ui.TodoListActivity.Companion.TODO_TITLE
import com.esracangungor.todolistapp.ui.TodoListActivity.Companion.TODO_TYPE
import com.esracangungor.todolistapp.ui.TodoListActivity.Companion.TODO_TYPE_EDIT
import java.text.SimpleDateFormat
import java.util.*

class AddEditTodoActivity : AppCompatActivity() {
    private lateinit var viewModal: TodoViewModal
    private var todoId = -1
    private lateinit var binding: ActivityAddEditTodoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditTodoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModal = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[TodoViewModal::class.java]

        val todoType = intent.getStringExtra(TODO_TYPE)
        setUiSaveOrEdit(todoType)
        binding.btnAddEditSave.setOnClickListener {
            saveOrEdit(todoType)
        }
    }

    private fun setUiSaveOrEdit(todoType: String?) {
        if (todoType == TODO_TYPE_EDIT) {
            val todoTitle = intent.getStringExtra(TODO_TITLE)
            val todoDescription = intent.getStringExtra(TODO_DESC)
            todoId = intent.getIntExtra(TODO_ID, -1)
            binding.apply {
                btnAddEditSave.text = getString(R.string.update_todo)
                etAddEditTodoName.setText(todoTitle)
                etAddEditTodoDesc.setText(todoDescription)
            }
        } else {
            binding.apply {
                btnAddEditSave.text = getString(R.string.save_todo)
            }
        }
    }

    private fun saveOrEdit(todoType: String?) {
        val todoTitle = binding.etAddEditTodoName.text.toString()
        val todoDescription = binding.etAddEditTodoDesc.text.toString()
        val selectedCategory = when (binding.rgAddEditCategories.checkedRadioButtonId) {
            R.id.rb_daily -> getString(R.string.todo_category_daily)
            R.id.rb_school -> getString(R.string.todo_category_school)
            R.id.rb_office -> getString(R.string.todo_category_office)
            R.id.rb_other -> getString(R.string.todo_category_other)
            else -> getString(R.string.todo_category_daily)
        }

        if (todoType == TODO_TYPE_EDIT) {
            if (todoTitle.isNotEmpty() && todoDescription.isNotEmpty()) {
                val sdf = SimpleDateFormat("dd MMM, yyyy - HH:mm", Locale.getDefault())
                val currentDateAndTime: String = sdf.format(Date())
                val updatedTodo =
                    TodoItem(todoTitle, todoDescription, currentDateAndTime, selectedCategory)
                updatedTodo.id = todoId
                viewModal.updateTodo(updatedTodo)
                Toast.makeText(this, "Todo Updated..", Toast.LENGTH_LONG).show()
            }
        } else {
            if (todoTitle.isNotEmpty() && todoDescription.isNotEmpty()) {
                val sdf = SimpleDateFormat("dd MMM, yyyy - HH:mm", Locale.getDefault())
                val currentDateAndTime: String = sdf.format(Date())
                viewModal.addTodo(
                    TodoItem(
                        todoTitle,
                        todoDescription,
                        currentDateAndTime,
                        selectedCategory
                    )
                )
                Toast.makeText(this, "$todoTitle Added", Toast.LENGTH_LONG).show()
            }
        }
        startActivity(Intent(applicationContext, TodoListActivity::class.java))
        this.finish()
    }
}