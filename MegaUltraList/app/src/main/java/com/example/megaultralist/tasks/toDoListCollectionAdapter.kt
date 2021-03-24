package com.example.megaultralist.tasks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.megaultralist.databinding.ListLayoutBinding
import com.example.megaultralist.tasks.data.toDoList

class toDoListCollectionAdapter(private val todolists:MutableList<toDoList>, private val onToDoListClicked:(toDoList) -> Unit) : RecyclerView.Adapter<toDoListCollectionAdapter.ViewHolder>() {

    class ViewHolder(val binding:ListLayoutBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(toDoList: toDoList, onToDoListClicked: (toDoList) -> Unit) {
            binding.toDoListName.text = toDoList.listName

            binding.card.setOnClickListener {
                onToDoListClicked(toDoList)
            }
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