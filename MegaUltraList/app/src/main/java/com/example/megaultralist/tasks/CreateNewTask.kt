package com.example.megaultralist.tasks

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.megaultralist.ToDoListHolder
import com.example.megaultralist.databinding.ActivityCreateNewTaskBinding
import com.example.megaultralist.tasks.data.Task
import kotlinx.android.synthetic.main.list_layout.*

class CreateNewTask : AppCompatActivity() {

    private lateinit var binding: ActivityCreateNewTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreateNewTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.createTaskButton.setOnClickListener {

            createNewTask()

        }

    }

    private fun createNewTask() {

        val taskName = binding.newTaskName.text.toString()

        // Check to see if the user has entered a name for the task
        if (taskName.isNotEmpty()){

            val task = Task(taskName, false)

            ToDoListDepositoryManager.instance.addTaskToList(ToDoListHolder.PickedToDoList, task)

            Toast.makeText(this, "New task created", Toast.LENGTH_SHORT).show()

            finish()

        }else{

            Toast.makeText(this, "Please enter a task :)", Toast.LENGTH_SHORT).show()

        }

    }
}