package com.example.megaultralist.tasks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.megaultralist.databinding.ListLayoutBinding
import com.example.megaultralist.tasks.data.Task
import com.example.megaultralist.tasks.data.toDoList
import com.google.android.gms.tasks.Tasks
import kotlinx.android.synthetic.main.list_layout.view.*

class toDoListCollectionAdapter(private val todolists:MutableList<toDoList>, private val onToDoListClicked:(toDoList) -> Unit) : RecyclerView.Adapter<toDoListCollectionAdapter.ViewHolder>() {

    class ViewHolder(val binding:ListLayoutBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(toDoList: toDoList, onToDoListClicked: (toDoList) -> Unit) {
            binding.toDoListName.text = toDoList.listName

            binding.card.setOnClickListener {
                onToDoListClicked(toDoList)
            }

            binding.toDoListProgressBar.progress = calculateProgress(toDoList.tasks)
        }

        private fun calculateProgress(tasks: MutableList<Task>): Int {

            val size: Float = tasks.size.toFloat()
            var completedTasks: Float = 0.0F

            tasks.forEach {
                if (it.completed){
                    completedTasks++
                }
            }

            return (completedTasks / size * 100).toInt()
        }
    }

    override fun getItemCount(): Int = todolists.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val toDoList = todolists[position]
        holder.bind(toDoList, onToDoListClicked)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }


}