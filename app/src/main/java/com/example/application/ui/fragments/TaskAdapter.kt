package com.example.application.ui.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.application.data.Task
import com.example.application.databinding.ItemTaskBinding

class TaskAdapter(private val itemClickListener: OnItemClickListener) : ListAdapter<Task, TaskAdapter.ViewHolder>(
    DiffCallback()
) {

    inner class ViewHolder(private val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.apply {
                root.setOnClickListener {
                    if (adapterPosition != RecyclerView.NO_POSITION) itemClickListener.onItemClick(getItem(adapterPosition))
                }
                checkBoxCompleted.setOnClickListener {
                    if (adapterPosition != RecyclerView.NO_POSITION) itemClickListener.onCheckbox(getItem(adapterPosition), checkBoxCompleted.isChecked)
                }
            }
        }

        fun bind(task: Task) {
            binding.apply {
                checkBoxCompleted.isChecked = task.completed
                textViewName.apply {
                    text = task.name
                    paint.isStrikeThruText = task.completed
                }
                labelPriority.isVisible = task.important
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))

    class DiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean = oldItem == newItem
    }

    interface OnItemClickListener {
        fun onItemClick(task: Task)
        fun onCheckbox(task: Task, isChecked: Boolean)
    }
}