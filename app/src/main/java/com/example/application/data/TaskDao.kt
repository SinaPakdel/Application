package com.example.application.data

import androidx.room.*
import com.example.application.data.SortOrder
import com.example.application.data.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {


    fun getTasks(searchQuery: String, sortOrder: SortOrder, hideCompleted: Boolean): Flow<List<Task>> =
        when (sortOrder) {
            SortOrder.BY_NAME -> getTasksByName(searchQuery, hideCompleted)
            SortOrder.BY_DATE -> getTasksByCompleted(searchQuery, hideCompleted)
        }

    @Query("SELECT * FROM task_table WHERE (completed != :hideCompleted OR completed=0) AND name LIKE '%' || :searchQuery || '%' ORDER BY important DESC, name")
    fun getTasksByName(searchQuery: String, hideCompleted: Boolean): Flow<List<Task>>

    @Query("SELECT * FROM task_table WHERE (completed != :hideCompleted OR completed=0) AND name LIKE '%' || :searchQuery || '%' ORDER BY important DESC, created")
    fun getTasksByCompleted(searchQuery: String, hideCompleted: Boolean): Flow<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Task)

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)

    @Query("DELETE FROM task_table WHERE completed = 1")
    suspend fun deleteAllTask()
}