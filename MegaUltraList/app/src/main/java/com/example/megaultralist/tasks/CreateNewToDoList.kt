package com.example.megaultralist.tasks

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.megaultralist.MainActivity
import com.example.megaultralist.databinding.ActivityCreateNewToDoListBinding
import com.example.megaultralist.databinding.ActivityMainBinding
import com.example.megaultralist.tasks.data.Task
import com.example.megaultralist.tasks.data.toDoList

class CreateNewToDoList : AppCompatActivity() {

    private lateinit var binding: ActivityCreateNewToDoListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateNewToDoListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.createToDoListButton.setOnClickListener {

            createNewList()

        }
    }

    private fun createNewList(){

        var listName = binding.newListName.text.toString()

        // Check to see if the user has entered a name for the list.
        if(listName.isNotEmpty()){

            val mutableList = mutableListOf<Task>()
            val todolist = toDoList(listName, mutableList)

            ToDoListDepositoryManager.instance.addToDoList(todolist)
            ToDoListDepositoryManager.instance.updateToDoList(todolist)

            Toast.makeText(this, "New list created", Toast.LENGTH_SHORT).show()

            finish()

        }else{
            Toast.makeText(this, "Please enter a name for the list :)", Toast.LENGTH_SHORT).show()
        }

    }
}