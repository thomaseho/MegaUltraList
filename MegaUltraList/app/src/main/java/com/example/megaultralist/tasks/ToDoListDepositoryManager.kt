package com.example.megaultralist.tasks

import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import com.example.megaultralist.MainActivity
import com.example.megaultralist.ToDoListHolder
import com.example.megaultralist.databinding.ActivityMainBinding
import com.example.megaultralist.tasks.data.Task
import com.example.megaultralist.tasks.data.toDoList
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileOutputStream
import java.io.PrintWriter
import java.time.LocalDateTime
import kotlin.random.Random

class ToDoListDepositoryManager {

    private lateinit var listCollection:MutableList<toDoList>

    var onToDoLists:((List<toDoList>) -> Unit)? = null
    var onTasks:((List<Task>) -> Unit)? = null
    var onTodoListUpdate:((toDoList:toDoList) -> Unit)? = null
    var onChanges:((List<toDoList>) -> Unit)? = null
    val gson = Gson()

    val TAG:String = "MegaUltraList:ToDoListDepositoryManager"

    fun loadFirebase(){

        var userListRef = Firebase.storage.reference.child("userlists/userLists.json")
        val ONE_MEGABYTE: Long = 1024 * 1024
        userListRef.getBytes(ONE_MEGABYTE).addOnSuccessListener {
            load()
            updateAllLists()
        }.addOnFailureListener{
            load()
        }

    }

    fun load(){
        listCollection = mutableListOf(

         toDoList(listName = "Shoppinglist", tasks = mutableListOf(
            Task("Bread", false),
            Task("Eggs", false),
            Task("Milk", false)
        )),

        toDoList(listName = "Chores", tasks = mutableListOf(
            Task("Make dinner", false),
            Task("Take out the trash", false),
            Task("Clean the toilet", false)
        )),

        toDoList(listName = "Watchlist", tasks = mutableListOf(
            Task("LOTR", false),
            Task("Harry Potter", false),
            Task("The world burn", false)
        ))
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

    fun calculateProgressBar(): Int {

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


        }
        return progress
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun saveData(filePath: File?){
        updateAllLists()
        val path = filePath
        val fileName = "userLists.json"
        val file = File(path, fileName)

        if(file.exists() && file.isFile){
            file.delete()
        }
        file.createNewFile()

        if (path != null){

            var content: String = "{\n"
            listCollection.forEach { toDoList ->

                content = content + "    \"todolist\":   {\n" + "\"listname\": " + "\"${toDoList.listName}\",\n" + "\"tasks\":  [\n"

                toDoList.tasks.forEach {

                    content = content + "    {\"taskName\": \"${it.taskName}\"," + " \"completed\": ${it.completed}},\n"

                }
                content = "$content    ]\n    },\n"
            }

            content = "$content}"

            FileOutputStream(file, true).bufferedWriter().use { writer ->
                writer.write(content)
            }
            upload(file.toUri())
        }

    }

    fun upload(file: Uri){

        Log.d(TAG, "Upload file $file")

        val ref = FirebaseStorage.getInstance().reference.child("userlists/${file.lastPathSegment}")
        val uploadTask = ref.putFile(file)

        uploadTask.addOnSuccessListener {
            Log.d(TAG, "Saved changes ${it.toString()}")
        }.addOnFailureListener{
            Log.e(TAG, "Error saving changes to Firebase", it)
        }

    }

    fun updateChanges(){
        onChanges?.invoke(listCollection)
    }

    companion object {

        val instance = ToDoListDepositoryManager()

    }
}
