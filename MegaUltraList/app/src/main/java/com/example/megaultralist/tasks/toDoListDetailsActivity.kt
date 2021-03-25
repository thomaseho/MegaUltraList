package com.example.megaultralist.tasks

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.megaultralist.EXTRA_TODOLIST_INFO
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

            setResult(RESULT_CANCELED, Intent(EXTRA_TODOLIST_INFO).apply {
                // Something to be sent back to main
            })

            finish()

        }

        binding.toDoListName.text = todolist.listName
        binding.toDoListTasks.layoutManager = LinearLayoutManager(this)
        binding.toDoListTasks.adapter = TaskCollectionAdapter(todolist.tasks)

        binding.newTaskButton.setOnClickListener{

            createNewTaskButton()

        }

        calculateProgress(todolist.tasks)

    }


    private fun createNewTaskButton() {

        var intent = Intent(this, CreateNewTask::class.java)

        startActivity(intent)

    }

    private fun calculateProgress(tasks: List<Task>) {

        val size: Float = tasks.size.toFloat()
        var completedTasks: Float = 0.0F

        tasks.forEach{
            if (it.completed){

                completedTasks++

            }
        }

        val progress: Float = (completedTasks / size * 100)

        binding.toDoListProgressBar.progress = progress.toInt()
    }
}