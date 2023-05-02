package com.esracangungor.todolistapp.di

import com.esracangungor.todolistapp.data.TodoRepository
import org.koin.dsl.module


val appModule = module {
    single {
        TodoRepository(
            todoDao = get()
        )
    }
}