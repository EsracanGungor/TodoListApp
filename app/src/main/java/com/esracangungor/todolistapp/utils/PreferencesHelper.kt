package com.esracangungor.todolistapp.utils

import android.content.Context
import android.content.SharedPreferences


object PreferencesHelper {
    const val DEFAULT_STRING_VALUE = "default_value"

    /**
     * Returns Editor to modify values of SharedPreferences
     * @param context Application context
     * @return editor instance
     */
    private fun getEditor(context: Context): SharedPreferences.Editor {
        return getPreferences(context).edit()
    }

    /**
     * Returns SharedPreferences object
     * @param context Application context
     * @return shared preferences instance
     */
    private fun getPreferences(context: Context): SharedPreferences {
        val name = "TodoAppPreferences"
        return context.getSharedPreferences(
            name,
            Context.MODE_PRIVATE
        )
    }

    /**
     * Save a string on SharedPreferences
     * @param tag tag
     * @param value value
     * @param context Application context
     */
    fun putString(tag: String?, value: String?, context: Context) {
        val editor: SharedPreferences.Editor = getEditor(context)
        editor.putString(tag, value)
        editor.apply()
    }

    /**
     * Get a string value from SharedPreferences
     * @param tag tag
     * @param context Application context
     * @return String value
     */
    fun getString(tag: String?, context: Context): String? {
        val sharedPreferences = getPreferences(context)
        return sharedPreferences.getString(tag, DEFAULT_STRING_VALUE)
    }
}