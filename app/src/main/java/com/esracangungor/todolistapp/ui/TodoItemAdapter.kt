package com.esracangungor.todolistapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.esracangungor.todolistapp.data.TodoItem
import com.esracangungor.todolistapp.databinding.TodoItemBinding

class TodoAdapter(
    private val TodoClickDeleteInterface: TodoClickDeleteInterface,
    private val TodoClickEditInterface: TodoClickEditInterface
) : RecyclerView.Adapter<TodoAdapter.ViewHolder>() {

    private val allTodos = ArrayList<TodoItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = TodoItemBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(
            binding
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(allTodos[position])
    }

    override fun getItemCount(): Int {
        return allTodos.size
    }

    inner class ViewHolder(private val binding: TodoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            todo: TodoItem
        ) {
            binding.apply {
                val dateText = "Last Updated : ${todo.timeStamp}"
                tvTodoItemCategory.text = todo.todoCategory
                tvTodoItemName.text = todo.todoTitle
                tvTodoItemDesc.text = todo.todoDescription
                tvTodoItemDate.text = dateText
                ivTodoItemDelete.setOnClickListener {
                    TodoClickDeleteInterface.onDeleteIconClick(todo)
                }
                ivTodoItemEdit.setOnClickListener {
                    TodoClickEditInterface.onEditIconClick(todo)
                }
            }
        }
    }

    fun updateList(newList: List<TodoItem>) {
        allTodos.clear()
        allTodos.addAll(newList)
        notifyDataSetChanged()
    }
}

interface TodoClickDeleteInterface {
    fun onDeleteIconClick(todoItem: TodoItem)
}

interface TodoClickEditInterface {
    fun onEditIconClick(todoItem: TodoItem)
}