package com.esracangungor.todolistapp.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import android.os.Build
import android.widget.RemoteViews
import androidx.lifecycle.LiveData
import com.esracangungor.todolistapp.utils.PreferencesHelper
import com.esracangungor.todolistapp.R
import com.esracangungor.todolistapp.data.TodoDatabase
import com.esracangungor.todolistapp.data.TodoItem
import com.esracangungor.todolistapp.data.TodoRepository
import com.esracangungor.todolistapp.ui.AddEditTodoActivity
import com.esracangungor.todolistapp.ui.TodoListActivity.Companion.TODO_DESC
import com.esracangungor.todolistapp.ui.TodoListActivity.Companion.TODO_ID
import com.esracangungor.todolistapp.ui.TodoListActivity.Companion.TODO_TITLE
import com.esracangungor.todolistapp.ui.TodoListActivity.Companion.TODO_TYPE
import com.esracangungor.todolistapp.ui.TodoListActivity.Companion.TODO_TYPE_DELETE
import com.esracangungor.todolistapp.ui.TodoListActivity.Companion.TODO_TYPE_EDIT
import com.esracangungor.todolistapp.widget.TodoConfigurationActivity.Companion.TODO_WIDGET_SELECTED_CATEGORY
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TodoListAppWidget : AppWidgetProvider() {
    private val actionUpdate = "UPDATE_ITEM"
    private lateinit var todoRepository: TodoRepository
    private lateinit var allTodos: LiveData<List<TodoItem>>
    private var todoItemList = ArrayList<TodoItem>()
    var category: String? = null

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        category = PreferencesHelper.getString(TODO_WIDGET_SELECTED_CATEGORY, context)

        val dao = TodoDatabase.getDatabase(context).getTodoDao()
        todoRepository = TodoRepository(dao)
        allTodos = todoRepository.allTodosByCategory(category)

        allTodos.observeForever { list ->
            updateList(context, list)
        }

        for (appWidgetId in appWidgetIds!!) {

            val addIntent = Intent(context, javaClass)
            addIntent.action = actionUpdate

            val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                0 or PendingIntent.FLAG_MUTABLE
            } else {
                0
            }

            val addPendingIntent =
                PendingIntent.getBroadcast(context, 0, addIntent, flag)

            val serviceIntent = Intent(context, TodoListAppWidgetService::class.java)
            serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            serviceIntent.data = Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME))

            val intent = Intent(context, AddEditTodoActivity::class.java)
            val pendingIntent =
                PendingIntent.getActivity(context, 0, intent, flag)

            val remoteViews = RemoteViews(context.packageName, R.layout.todo_list_app_widget_layout)
            remoteViews.setOnClickPendingIntent(R.id.iv_add_todo, pendingIntent)
            remoteViews.setRemoteAdapter(R.id.lv_widget_todo, serviceIntent)
            remoteViews.setEmptyView(R.id.lv_widget_todo, R.id.ll_widget_todo_list_error)
            remoteViews.setPendingIntentTemplate(R.id.lv_widget_todo, addPendingIntent)
            appWidgetManager!!.updateAppWidget(appWidgetId, remoteViews)
        }
    }

    override fun onEnabled(context: Context?) {
        super.onEnabled(context)
        notifyAppWidgetViewDataChanged(context)
    }

    override fun onReceive(context: Context, intent: Intent?) {
        super.onReceive(context, intent)
        if (intent == null) {
            return
        } else {
            when (intent.action) {
                actionUpdate -> {
                    when (intent.getStringExtra(WIDGET_UPDATE_TYPE)) {
                        TODO_TYPE_EDIT -> {
                            editTodoItemFromWidget(intent, context)
                            notifyAppWidgetViewDataChanged(context)
                        }
                        TODO_TYPE_DELETE -> {
                            deleteTodoItemFromWidget(intent, context)
                            notifyAppWidgetViewDataChanged(context)
                        }
                    }
                }
            }
        }
    }

    private fun deleteTodoItemFromWidget(intent: Intent, context: Context) {
        val widgetTodoItem =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra(
                    WIDGET_TODO_ITEM,
                    TodoItem::class.java
                )
            } else {
                intent.getParcelableExtra(WIDGET_TODO_ITEM)
            }
        val dao = TodoDatabase.getDatabase(context).getTodoDao()
        todoRepository = TodoRepository(dao)
        CoroutineScope(Dispatchers.Main).launch {
            if (widgetTodoItem != null) {
                todoRepository.delete(widgetTodoItem)
            }
        }
    }

    private fun editTodoItemFromWidget(intent: Intent, context: Context) {
        val widgetTodoItem =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra(
                    WIDGET_TODO_ITEM,
                    TodoItem::class.java
                )
            } else {
                intent.getParcelableExtra(WIDGET_TODO_ITEM)
            }
        if (widgetTodoItem != null) {
            val widgetEditIntent = Intent(context, AddEditTodoActivity::class.java).apply {
                putExtra(TODO_TYPE, TODO_TYPE_EDIT)
                putExtra(TODO_TITLE, widgetTodoItem.todoTitle)
                putExtra(TODO_DESC, widgetTodoItem.todoDescription)
                putExtra(TODO_ID, widgetTodoItem.id)
                addFlags(FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(widgetEditIntent)
        }

    }

    private fun updateList(context: Context?, newList: List<TodoItem>) {
        todoItemList.clear()
        todoItemList.addAll(newList)
        notifyAppWidgetViewDataChanged(context)
    }

    private fun notifyAppWidgetViewDataChanged(context: Context?) {
        val widgetManager = AppWidgetManager.getInstance(context?.applicationContext)
        widgetManager.notifyAppWidgetViewDataChanged(
            widgetManager.getAppWidgetIds(
                context?.applicationContext?.packageName?.let {
                    ComponentName(
                        it,
                        TodoListAppWidget::class.java.name
                    )
                }
            ),
            R.id.lv_widget_todo
        )
    }

    companion object {
        const val WIDGET_UPDATE_TYPE = "UpdateType"
        const val WIDGET_TODO_ITEM = "TodoItem"
    }
}