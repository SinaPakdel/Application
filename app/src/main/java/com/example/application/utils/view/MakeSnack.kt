package com.example.application.utils.view

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun makeSnack(message: String, view: View) =
    Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()