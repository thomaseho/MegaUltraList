package com.example.megaultralist.tasks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.megaultralist.databinding.ListLayoutBinding

class toDoListCollectionAdapter(private val todolists:MutableList<toDoList>) : RecyclerView.Adapter<toDoListCollectionAdapter.ViewHolder>() {

    class ViewHolder(val binding:ListLayoutBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(toDoList: toDoList) {
            binding.toDoListName.text = toDoList.listName
        }
    }

    override fun getItemCount(): Int = todolists.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val toDoList = todolists[position]
        holder.bind(toDoList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }
}