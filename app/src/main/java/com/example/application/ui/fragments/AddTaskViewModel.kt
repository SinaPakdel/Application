package com.example.application.ui.fragments

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.application.data.Task
import com.example.application.data.TaskDao
import com.example.application.util.ADD_TASK_RESULT_OK
import com.example.application.util.EDIT_TASK_ERSULT_OK
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddTaskViewModel @Inject constructor(
    private val taskDao: TaskDao,
    private val state: SavedStateHandle
) : ViewModel() {

    private val _addEditTaskEventChannel = Channel<AddEditTaskEvent>()
    val addEditTaskEventChannel = _addEditTaskEventChannel.receiveAsFlow()
    fun onSaveClicked() {
        if (taskName.isBlank()) {
            showInvalidInputMessage("Name cannot be empty");return
        }
        if (task != null) {
            val updatedTask = task.copy(name = taskName, important = taskImportant)
            updatedTask(updatedTask)
        } else {
            createTask(Task(taskName, taskImportant))
        }
    }

    private fun showInvalidInputMessage(message: String) = viewModelScope.launch {
        _addEditTaskEventChannel.send(AddEditTaskEvent.ShowInvalidInputMessage(message))
    }

    private fun createTask(task: Task) = viewModelScope.launch {
        taskDao.insert(task)
        _addEditTaskEventChannel.send(AddEditTaskEvent.NaviagateBackWithResult(ADD_TASK_RESULT_OK))
    }

    private fun updatedTask(task: Task) = viewModelScope.launch {
        taskDao.update(task)
        _addEditTaskEventChannel.send(AddEditTaskEvent.NaviagateBackWithResult(EDIT_TASK_ERSULT_OK))
    }

    val task = state.get<Task>("task")

    var taskName = state.get<String>("taskName") ?: task?.name ?: ""
        set(value) {
            field = value
            state["taskName"] = value
        }

    var taskImportant = state.get<Boolean>("taskImportant") ?: task?.important ?: false
        set(value) {
            field = value
            state["taskImportant"] = value
        }


    sealed class AddEditTaskEvent {
        data class ShowInvalidInputMessage(val mesage: String) : AddEditTaskEvent()
        data class NaviagateBackWithResult(val result: Int) : AddEditTaskEvent()
    }

}