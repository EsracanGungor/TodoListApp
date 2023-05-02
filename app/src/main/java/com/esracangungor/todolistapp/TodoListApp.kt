package com.esracangungor.todolistapp

import android.app.Application
import com.esracangungor.todolistapp.di.appModule
import org.koin.android.ext.koin.androidContext

class TodoListApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin()
    }

    private fun startKoin() {
        org.koin.core.context.startKoin {
            androidContext(this@TodoListApp.applicationContext)
            modules(
                listOf(
                    appModule
                )
            )
        }
    }
}