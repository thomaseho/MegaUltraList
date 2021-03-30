package com.example.megaultralist.tasks

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.net.Uri
import android.os.Build
import android.telephony.TelephonyManager
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.net.toUri
import com.example.megaultralist.ToDoListHolder
import com.example.megaultralist.tasks.data.Task
import com.example.megaultralist.tasks.data.toDoList
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.io.File
import java.io.FileOutputStream
import com.beust.klaxon.Klaxon
import java.io.StringReader

class ToDoListDepositoryManager {

    lateinit var listCollection:MutableList<toDoList>
    private lateinit var unique_id: String

    var onToDoLists:((List<toDoList>) -> Unit)? = null
    var onTasks:((List<Task>) -> Unit)? = null
    var onTodoListUpdate:((toDoList:toDoList) -> Unit)? = null
    var onChanges:((List<toDoList>) -> Unit)? = null

    fun load(){

        val noProgress: Int = 0

        listCollection = mutableListOf(

         toDoList(listName = "Shoppinglist", tasks = mutableListOf(
            Task("Bread", false),
            Task("Eggs", false),
            Task("Milk", false)
        ), noProgress),

        toDoList(listName = "Chores", tasks = mutableListOf(
            Task("Make dinner", false),
            Task("Take out the trash", false),
            Task("Clean the toilet", false)
        ), noProgress),

        toDoList(listName = "Watchlist", tasks = mutableListOf(
            Task("LOTR", false),
            Task("Harry Potter", false),
            Task("The world burn", false)
        ), noProgress)
        )

        updateAllLists()
    }

    fun addToDoList(toDoList: toDoList){

        listCollection.add(toDoList)
        onToDoLists?.invoke(listCollection)
        updateChanges()

    }
    fun updateAllLists(){
        onToDoLists?.invoke(listCollection)
    }

    fun updateToDoList(toDoList: toDoList){

        onTodoListUpdate?.invoke(toDoList)

    }

    fun removeToDoList(toDoList: toDoList){

        listCollection.remove(toDoList)
        updateAllLists()
        updateChanges()

    }

    fun addTaskToList(toDoList: toDoList?, task: Task){

        if (toDoList != null){
            toDoList.tasks.add(task)
            updateAllLists()
            updateToDoListTasks(toDoList.tasks)
            updateChanges()
        }

    }

    fun updateTaskCompletion(task: Task, status: Boolean){

        task.completed = status
        ToDoListHolder.PickedToDoList?.let { updateToDoListTasks(it.tasks) }
        updateAllLists()
        updateChanges()

    }

    fun removeTaskFromList(toDoList: toDoList?, task: Task){

        if (toDoList != null){
            toDoList.tasks.remove(task)
            updateToDoListTasks(toDoList.tasks)
            updateAllLists()
            updateChanges()
        }

    }

    fun updateToDoListTasks(tasks: List<Task>) {
        onTasks?.invoke(tasks)
    }

    fun calculateProgressBar() {

        val size: Float? = ToDoListHolder.PickedToDoList?.tasks?.size?.toFloat()
        var completedTasks: Float = 0.0F
        var progress: Int = 0

        if(ToDoListHolder.PickedToDoList != null){

            val tasks = ToDoListHolder.PickedToDoList!!.tasks

            tasks.forEach{

                if(it.completed){

                    completedTasks++
                }
            }
            progress = (completedTasks / size!! * 100).toInt()

            ToDoListHolder.PickedToDoList!!.progress = progress
        }

    }

    fun updateChanges(){
        onChanges?.invoke(listCollection)
    }

    companion object {

        val instance = ToDoListDepositoryManager()

    }
}
