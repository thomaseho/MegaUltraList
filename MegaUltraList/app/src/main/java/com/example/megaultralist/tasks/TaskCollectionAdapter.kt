package com.example.megaultralist.tasks


import android.os.Build
import android.system.Os.remove
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat.recreate
import androidx.core.app.ActivityCompat.startActivities
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.megaultralist.ToDoListHolder
import com.example.megaultralist.databinding.ActivityToDoListDetailsBinding
import com.example.megaultralist.databinding.TaskLayoutBinding
import com.example.megaultralist.tasks.data.Task

class TaskCollectionAdapter (private var tasks:List<Task>) : RecyclerView.Adapter<TaskCollectionAdapter.ViewHolder>() {

    class ViewHolder(val binding:TaskLayoutBinding): RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.R)
        fun bind(Task: Task) {

            val thisList = ToDoListHolder.PickedToDoList

            binding.taskText.text = Task.taskName
            binding.taskCheckBox.isChecked = Task.completed

            binding.deleteTaskButton.setOnClickListener {

                val newTasks = thisList?.tasks

                thisList?.tasks?.forEach {

                    if (Task.completed) {
                        Task.completed = false
                        binding.taskCheckBox.isChecked = false
                    }
                }

                newTasks?.remove(Task)

                ToDoListDepositoryManager.instance.removeTaskFromList(ToDoListHolder.PickedToDoList, Task)

                if (newTasks != null) {
                    thisList.tasks = newTasks
                }

                if (thisList != null) {
                    ToDoListDepositoryManager.instance.updateToDoList(thisList)
                }

            }

            binding.taskCheckBox.setOnClickListener {

                val status = !Task.completed
                binding.taskCheckBox.isChecked = status

                ToDoListDepositoryManager.instance.updateTaskCompletion(Task, status)

            }
        }

    }

    override fun getItemCount(): Int = tasks.size

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val task = tasks[position]
        holder.bind(task)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(TaskLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    public fun updateTasks(newTask: List<Task>){

        tasks = newTask
        notifyDataSetChanged()
    }

}
