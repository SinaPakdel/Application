package com.example.application.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.application.di.APPLICATION_SCOPE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider


@Database(entities = [Task::class], version = 1)
abstract class TaskDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao

    /**
     * Just create first time when database created
     */
    class Callback @Inject constructor(
        /**
         * private val database: Provider<TaskDatabase> its just like lazily, create when needed
         */
        private val database: Provider<TaskDatabase>,
        /**
         * in AppModule we handle it with dagger
         */
        @APPLICATION_SCOPE private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            val dao = database.get().taskDao()
            applicationScope.launch {
                dao.insert(Task("CODE"))
                dao.insert(Task("EAT"))
                dao.insert(Task("SLEEP", important = true))
                dao.insert(Task("RUN", important = true))
                dao.insert(Task("GIVE UP", important = true, completed = true))
                dao.insert(Task("DECODE"))
            }
        }
    }
}