package com.example.application.ui

import androidx.lifecycle.*
import com.example.application.data.PrefManager
import com.example.application.data.SortOrder
import com.example.application.data.Task
import com.example.application.data.TaskDao
import com.example.application.util.ADD_TASK_RESULT_OK
import com.example.application.util.EDIT_TASK_ERSULT_OK

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val taskDao: TaskDao,
    private val prefManager: PrefManager,
    private val state: SavedStateHandle
) : ViewModel() {

    val searchQuery = state.getLiveData("search", "")

//
//    val sortOrder = MutableStateFlow(SortOrder.BY_NAME)
//    val hideCompleted = MutableStateFlow(false)

    val prefManagerFlow = prefManager.preFlow

    private val _tasksEvent = Channel<TasksEvent>()
    val tasksEvent = _tasksEvent.receiveAsFlow()

    private val tasksFlow =
        combine(searchQuery.asFlow(), prefManagerFlow) { query, filterPref -> Pair(query, filterPref) }
            .flatMapLatest { taskDao.getTasks(it.first, it.second.sortOrder, it.second.hideCompleted) }

    fun onSortOrderSelected(sortOrder: SortOrder) = viewModelScope.launch { prefManager.updateSortOrder(sortOrder) }
    fun onHideCompleted(hideCompleted: Boolean) = viewModelScope.launch { prefManager.updateHideCompleted(hideCompleted) }

    val tasks: LiveData<List<Task>> = tasksFlow.asLiveData()

    fun onTeasCheckedChanged(task: Task, checked: Boolean) = viewModelScope.launch { taskDao.update(task.copy(completed = checked)) }
    fun onTaskSelected(task: Task) = viewModelScope.launch { _tasksEvent.send(
        TasksEvent.NavigateToEditTaskScreen(
            task
        )
    ) }
    fun onTaskSwiped(task: Task) = viewModelScope.launch {
        taskDao.delete(task)
        _tasksEvent.send(TasksEvent.ShowUndoDeletedTaskMessage(task))
    }

    fun onUndoDeletedClicked(task: Task) = viewModelScope.launch { taskDao.insert(task) }
    fun onAddNewTaskClicked() = viewModelScope.launch { _tasksEvent.send(TasksEvent.NavigateToAddTaskScreen) }
    fun onAddEditeResult(result: Int) {
        when (result) {
            ADD_TASK_RESULT_OK -> showTaskSavedConfirmationMessage("Task Added")
            EDIT_TASK_ERSULT_OK -> showTaskSavedConfirmationMessage("Task Update")
        }
    }

    private fun showTaskSavedConfirmationMessage(message: String) = viewModelScope.launch {
        _tasksEvent.send(TasksEvent.ShowTaskSavedConfirmationMessage(message))
    }

    fun onDeleteAllClicked() = viewModelScope.launch {
        _tasksEvent.send(TasksEvent.NavigateToDeleteAllCompletedScreen)
    }


    sealed class TasksEvent {
        data class ShowUndoDeletedTaskMessage(val task: Task) : TasksEvent()

        object NavigateToAddTaskScreen : TasksEvent()
        data class NavigateToEditTaskScreen(val task: Task) : TasksEvent()
        data class ShowTaskSavedConfirmationMessage(val message: String) : TasksEvent()
        object NavigateToDeleteAllCompletedScreen : TasksEvent()
    }
}

