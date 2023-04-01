package com.example.application.ui.fragments

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.application.data.SortOrder
import com.example.application.data.Task
import com.example.application.R
import com.example.application.databinding.FragmentTasksBinding
import com.example.application.ui.TaskViewModel
import com.example.application.util.onQueryTextChanged
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TasksFragment : Fragment(R.layout.fragment_tasks), TaskAdapter.OnItemClickListener {
    private val viewModel: TaskViewModel by viewModels()
    private lateinit var binding: FragmentTasksBinding
    private lateinit var searchView: SearchView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTasksBinding.bind(view)
        val menuHost: MenuHost = requireActivity()
        val taskAdapter = TaskAdapter(this)

        binding.apply {
            rvTasks.apply {
                adapter = taskAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }

            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean = false

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val task = taskAdapter.currentList[viewHolder.adapterPosition]
                    viewModel.onTaskSwiped(task)
                }
            }).attachToRecyclerView(rvTasks)

            fabAddTask.setOnClickListener { viewModel.onAddNewTaskClicked() }

            setFragmentResultListener("ADD_EDIT_REQUEST") { _, bundle ->
                val result = bundle.getInt("ADD_EDIT_RESULT")
                viewModel.onAddEditeResult(result)
            }
        }

        viewModel.tasks.observe(viewLifecycleOwner) { taskAdapter.submitList(it) }

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_task_fragment, menu)
                val searchItem = menu.findItem(R.id.action_search)
                searchView = searchItem.actionView as SearchView
                val pendingQuery = viewModel.searchQuery.value

                if (pendingQuery != null && pendingQuery.isNotEmpty()) {
                    searchItem.expandActionView()
                    searchView.setQuery(pendingQuery, false)
                }
                searchView.onQueryTextChanged {
                    viewModel.searchQuery.value = it
                }

                viewLifecycleOwner.lifecycleScope.launch {
                    menu.findItem(R.id.action_hide_completed).isChecked = viewModel.prefManagerFlow.first().hideCompleted
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_sort_by_name -> {
                        viewModel.onSortOrderSelected(SortOrder.BY_NAME)
                        true
                    }
                    R.id.action_sort_by_date -> {
                        viewModel.onSortOrderSelected(SortOrder.BY_DATE)
                        true
                    }
                    R.id.action_hide_completed -> {
                        menuItem.isChecked = !menuItem.isChecked
                        viewModel.onHideCompleted(menuItem.isChecked)
                        true
                    }
                    R.id.action_delete_completed_task -> {
                        viewModel.onDeleteAllClicked()
                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)



        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.tasksEvent.collect { event ->
                when (event) {
                    is TaskViewModel.TasksEvent.ShowUndoDeletedTaskMessage -> {
                        Snackbar.make(requireView(), "Task deleted", Snackbar.LENGTH_LONG).setAction("UNDO") { viewModel.onUndoDeletedClicked(event.task) }.show()
                    }
                    is TaskViewModel.TasksEvent.NavigateToAddTaskScreen -> {
                        findNavController().navigate(TasksFragmentDirections.actionTasksFragmentToAddFragment("", null))
                    }
                    is TaskViewModel.TasksEvent.NavigateToEditTaskScreen -> {
                        findNavController().navigate(TasksFragmentDirections.actionTasksFragmentToAddFragment(event.task.name, event.task))
                    }
                    is TaskViewModel.TasksEvent.ShowTaskSavedConfirmationMessage -> {
                        Snackbar.make(requireView(), event.message, Snackbar.LENGTH_LONG).show()
                    }
                    TaskViewModel.TasksEvent.NavigateToDeleteAllCompletedScreen ->
                        findNavController().navigate(R.id.action_global_deleteAllCompletedDialog)

                }
            }
        }
    }

    override fun onItemClick(task: Task) {
        viewModel.onTaskSelected(task)
    }

    override fun onCheckbox(task: Task, isChecked: Boolean) {
        viewModel.onTeasCheckedChanged(task, isChecked)
    }

    override fun onDestroy() {
        super.onDestroy()
        searchView.setOnQueryTextListener(null)
    }
}