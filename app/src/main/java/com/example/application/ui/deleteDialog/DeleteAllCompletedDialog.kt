package com.example.application.ui.deleteDialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeleteAllCompletedDialog : DialogFragment() {
    private val viewModel: DeleteAllCompletedViewModel by viewModels()


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = AlertDialog.Builder(requireContext()).setTitle("Confirm Delete").setMessage("Delete All?").setNegativeButton("Cancle", null)
        .setPositiveButton("Yes") { _, _ ->
            viewModel.onConfirmClick()
        }.create()

}