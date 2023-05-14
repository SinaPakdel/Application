package com.example.application.utils.view

import androidx.appcompat.widget.SearchView

inline fun SearchView.onQueryTextChanged(crossinline listener: (String) -> Unit) {
    this.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            listener.invoke(query.orEmpty())
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            return true
        }
    })
}