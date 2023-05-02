package com.esracangungor.todolistapp.widget

import android.appwidget.AppWidgetManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.esracangungor.todolistapp.utils.PreferencesHelper
import com.esracangungor.todolistapp.databinding.ActivityConfigurationBinding

class TodoConfigurationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConfigurationBinding
    var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfigurationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handleUi()
        handleWidgetId()
        handleBundleResult()
        initListViewAdapter()
    }

    private fun handleUi() {
        binding.apply {
            btnWidgetCancel.setOnClickListener {
                finish()
            }
        }
    }

    private fun handleWidgetId() {
        val configIntent = intent
        val extras = configIntent.extras
        if (extras != null) {
            appWidgetId = extras.getInt(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID
            )
        }
    }

    private fun handleBundleResult() {
        setResult(RESULT_CANCELED)
        val resultValue = Intent().putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        setResult(RESULT_CANCELED, resultValue)

        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
        }
    }

    private fun initListViewAdapter() {
        val adapter = WidgetCategorySelectionAdapter(
            categories = listOf("Daily", "School", "Office", "Other"),
            onClick = {
                selectAndSaveCategory(it)
            }
        )
        binding.rvTodoWidgetCategoryList.adapter = adapter
        binding.rvTodoWidgetCategoryList.layoutManager = LinearLayoutManager(this)
    }

    private fun selectAndSaveCategory(selectedTodoCategory: String) {
        PreferencesHelper.putString(TODO_WIDGET_SELECTED_CATEGORY, selectedTodoCategory, applicationContext)
        val intent = Intent().putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        setResult(RESULT_OK, intent)
        finish()
    }

    companion object {
        const val TODO_WIDGET_SELECTED_CATEGORY: String = "todoWidgetSelectedCategory"
    }

}