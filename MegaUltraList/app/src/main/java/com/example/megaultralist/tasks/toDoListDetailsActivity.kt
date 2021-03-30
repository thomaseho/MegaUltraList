package com.example.megaultralist.tasks

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.megaultralist.ToDoListHolder
import com.example.megaultralist.databinding.ActivityToDoListDetailsBinding
import com.example.megaultralist.tasks.data.Task
import com.example.megaultralist.tasks.data.toDoList

class toDoListDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityToDoListDetailsBinding
    private lateinit var todolist: toDoList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityToDoListDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val receivedToDoList = ToDoListHolder.PickedToDoList

        if (receivedToDoList != null) {

            todolist = receivedToDoList
            Log.i("ToDoList Details view", receivedToDoList.toString())

        } else {

            finish()

        }

        binding.toDoListName.text = todolist.listName
        binding.toDoListTasks.layoutManager = LinearLayoutManager(this)

        binding.toDoListTasks.adapter = TaskCollectionAdapter(todolist.tasks)

        binding.newTaskButton.setOnClickListener{

            createNewTaskButton()

        }

        binding.toDoListProgressBar.progress = ToDoListDepositoryManager.instance.calculateProgressBar()

        ToDoListDepositoryManager.instance.onTasks = {
            (binding.toDoListTasks.adapter as TaskCollectionAdapter).updateTasks(it)
            binding.toDoListProgressBar.progress = ToDoListDepositoryManager.instance.calculateProgressBar()
        }
    }


    private fun createNewTaskButton() {

        val intent = Intent(this, CreateNewTask::class.java)

        startActivity(intent)

    }

}