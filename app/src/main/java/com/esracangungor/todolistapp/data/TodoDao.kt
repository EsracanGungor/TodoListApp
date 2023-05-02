package com.esracangungor.todolistapp.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TodoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: TodoItem)

    @Delete
    suspend fun delete(item: TodoItem)

    @Query("SELECT * FROM todoItems order by id ASC")
    fun getAllTodoItems(): LiveData<List<TodoItem>>

    @Query("SELECT * FROM todoItems WHERE  category = :category order by id ASC")
    fun getAllTodoItemsByCategory(category: String?): LiveData<List<TodoItem>>

    @Update
    suspend fun update(item: TodoItem)
}