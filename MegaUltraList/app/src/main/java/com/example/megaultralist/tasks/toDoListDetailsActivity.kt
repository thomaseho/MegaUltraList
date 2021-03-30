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

        // Collect toDoList from the holder
        val receivedToDoList = ToDoListHolder.PickedToDoList

        // null check on the list, this will never be null, but in case something happens
        if (receivedToDoList != null) {

            todolist = receivedToDoList
            Log.i("ToDoList Details view", receivedToDoList.toString())

        } else {

            finish()

        }

        // Set bindings for the different elements of the layout file
        binding.toDoListName.text = todolist.listName
        binding.toDoListTasks.layoutManager = LinearLayoutManager(this)

        // Hook the Recyclerview up to the TaskCollectionAdapter. Send the tasks of the picked
        // list to the adapter.
        binding.toDoListTasks.adapter = TaskCollectionAdapter(todolist.tasks)

        binding.newTaskButton.setOnClickListener{

            createNewTaskButton()

        }

        // Set the progress of a list with a function in the DepositoryManager.
        binding.toDoListProgressBar.progress = ToDoListDepositoryManager.instance.calculateProgressBar()

        // When a task is changed, notify the TaskCollectionAdapter to update its dataset and
        // recalculate the progressbar.
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