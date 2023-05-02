package com.esracangungor.todolistapp.widget

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.lifecycle.LiveData
import com.esracangungor.todolistapp.utils.PreferencesHelper
import com.esracangungor.todolistapp.R
import com.esracangungor.todolistapp.data.TodoDatabase
import com.esracangungor.todolistapp.data.TodoItem
import com.esracangungor.todolistapp.data.TodoRepository
import com.esracangungor.todolistapp.ui.TodoListActivity.Companion.TODO_TYPE_DELETE
import com.esracangungor.todolistapp.ui.TodoListActivity.Companion.TODO_TYPE_EDIT
import com.esracangungor.todolistapp.widget.TodoConfigurationActivity.Companion.TODO_WIDGET_SELECTED_CATEGORY
import com.esracangungor.todolistapp.widget.TodoListAppWidget.Companion.WIDGET_TODO_ITEM
import com.esracangungor.todolistapp.widget.TodoListAppWidget.Companion.WIDGET_UPDATE_TYPE


class TodoListAppWidgetService : RemoteViewsService() {
    private lateinit var todoRepository: TodoRepository
    private lateinit var allTodos: LiveData<List<TodoItem>>
    private var todoItemList = ArrayList<TodoItem>()
    private var category: String? = null

    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return WidgetItemFactory(applicationContext)
    }

    inner class WidgetItemFactory(private val context: Context) :
        RemoteViewsFactory {

        override fun onCreate() {
            category = PreferencesHelper.getString(TODO_WIDGET_SELECTED_CATEGORY, context)

            val dao = TodoDatabase.getDatabase(application).getTodoDao()
            todoRepository = TodoRepository(dao)
            allTodos = todoRepository.allTodosByCategory(category)
            allTodos.observeForever { list ->
                updateList(list)
            }
        }

        override fun onDataSetChanged() {
            if (todoItemList.isNotEmpty()) {
                getViewAt(0)
            }
        }

        override fun onDestroy() {
            //Close datasource connection
        }

        override fun getCount(): Int {
            return todoItemList.size
        }

        override fun getViewAt(position: Int): RemoteViews {
            val todoItem: TodoItem = todoItemList[position]

            val fillIntentEdit = Intent().apply {
                putExtra(WIDGET_UPDATE_TYPE, TODO_TYPE_EDIT)
                putExtra(WIDGET_TODO_ITEM, todoItem)
            }

            val fillIntentDel = Intent().apply {
                putExtra(WIDGET_UPDATE_TYPE, TODO_TYPE_DELETE)
                putExtra(WIDGET_TODO_ITEM, todoItem)
            }
            val dateText = "Last Updated : ${todoItem.timeStamp}"

            val remoteViews = RemoteViews(context.packageName, R.layout.todo_widget_item)
            remoteViews.setTextViewText(R.id.tv_todo_item_category, todoItem.todoCategory)
            remoteViews.setTextViewText(R.id.tv_todo_item_name, todoItem.todoTitle)
            remoteViews.setTextViewText(R.id.tv_todo_item_desc, todoItem.todoDescription)
            remoteViews.setTextViewText(R.id.tv_todo_item_date, dateText)
            remoteViews.setOnClickFillInIntent(R.id.iv_todo_item_edit, fillIntentEdit)
            remoteViews.setOnClickFillInIntent(R.id.iv_todo_item_delete, fillIntentDel)
            return remoteViews
        }

        override fun getLoadingView(): RemoteViews? {
            return null
        }

        override fun getViewTypeCount(): Int {
            return 1
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun hasStableIds(): Boolean {
            return true
        }

        private fun updateList(newList: List<TodoItem>) {
            todoItemList.clear()
            todoItemList.addAll(newList)
            onDataSetChanged()
        }
    }
}