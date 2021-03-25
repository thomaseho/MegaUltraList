package com.example.megaultralist.tasks

import android.system.Os.remove
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import com.example.megaultralist.databinding.TaskLayoutBinding
import com.example.megaultralist.tasks.data.Task

class TaskCollectionAdapter (private val tasks:MutableList<Task>) : RecyclerView.Adapter<TaskCollectionAdapter.ViewHolder>() {

    class ViewHolder(val binding:TaskLayoutBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(Task: Task) {

            binding.taskText.text = Task.taskName
            binding.taskCheckBox.isChecked = Task.completed

            binding.deleteTaskButton.setOnClickListener {

                deleteTaskButton()

            }

            binding.taskCheckBox.setOnCheckedChangeListener { compoundButton: CompoundButton, b: Boolean ->

                val status = binding.taskCheckBox.isChecked

                ToDoListDepositoryManager.instance.updateTaskCompletion(Task, status)

            }
        }

        private fun deleteTaskButton() {

            // Work in progress

        }
    }

    override fun getItemCount(): Int = tasks.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val task = tasks[position]
        holder.bind(task)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(TaskLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

}
