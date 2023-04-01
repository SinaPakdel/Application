package com.example.application.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.application.R
import com.example.application.databinding.FragmentAddBinding
import com.example.application.util.exhustive
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddTaskFragment : Fragment(R.layout.fragment_add) {
    private val viewModel: AddTaskViewModel by viewModels()
    private lateinit var binding: FragmentAddBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddBinding.bind(view)

        binding.apply {
            editTextTaskName.setText(viewModel.taskName)
            checkBoxImportant.isChecked = viewModel.taskImportant
            checkBoxImportant.jumpDrawablesToCurrentState()
            textViewDateCreated.isVisible = viewModel.task != null
            textViewDateCreated.text = "Create: ${viewModel.task?.createdDateFormatted}"


            editTextTaskName.addTextChangedListener {
                viewModel.taskName = it.toString()
            }

            checkBoxImportant.setOnCheckedChangeListener { _, isChecked ->
                viewModel.taskImportant = isChecked
            }

            fabSaveTask.setOnClickListener {
                viewModel.onSaveClicked()
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.addEditTaskEventChannel.collect { event ->
                when (event) {
                    is AddTaskViewModel.AddEditTaskEvent.NaviagateBackWithResult -> {
                        binding.editTextTaskName.clearFocus()
                        setFragmentResult("ADD_EDIT_REQUEST", bundleOf("ADD_EDIT_RESULT" to event.result))
                        findNavController().popBackStack()
                    }
                    is AddTaskViewModel.AddEditTaskEvent.ShowInvalidInputMessage -> {
                        Snackbar.make(requireView(), event.mesage, Snackbar.LENGTH_LONG).show()
                    }
                }.exhustive
            }
        }
    }
}