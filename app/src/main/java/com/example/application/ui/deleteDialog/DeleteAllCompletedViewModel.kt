package com.example.application.ui.deleteDialog

import androidx.lifecycle.ViewModel
import com.example.application.data.TaskDao
import com.example.application.di.APPLICATION_SCOPE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeleteAllCompletedViewModel @Inject constructor(
    private val taskDao: TaskDao,
    @APPLICATION_SCOPE private val applicationScope: CoroutineScope
) : ViewModel() {

    fun onConfirmClick() = applicationScope.launch {
        taskDao.deleteAllTask()
    }
}