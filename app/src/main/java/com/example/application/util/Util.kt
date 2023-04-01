package com.example.application.util

import android.app.Activity

val <T> T.exhustive: T
    get() = this

const val ADD_TASK_RESULT_OK = Activity.RESULT_FIRST_USER
const val EDIT_TASK_ERSULT_OK = Activity.RESULT_FIRST_USER + 1