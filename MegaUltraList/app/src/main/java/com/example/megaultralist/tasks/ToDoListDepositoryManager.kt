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

    private lateinit var listCollection:MutableList<toDoList>
    private lateinit var unique_id: String

    var onToDoLists:((List<toDoList>) -> Unit)? = null
    var onTasks:((List<Task>) -> Unit)? = null
    var onTodoListUpdate:((toDoList:toDoList) -> Unit)? = null
    var onChanges:((List<toDoList>) -> Unit)? = null
    @SuppressLint("HardwareIds")

    val TAG:String = "MegaUltraList:ToDoListDepositoryManager"

    fun loadFirebase() {

        // Download user file from Firebase
        val userListRef = Firebase.storage.reference.child("userlists/${unique_id}-Lists.json")
        val ONE_MEGABYTE: Long = 1024 * 1024

        userListRef.getBytes(ONE_MEGABYTE).addOnSuccessListener {

            val userLists = String(it)

            // Convert the Json file into an object we can use in Kotlin
            val result = Klaxon().parseArray<toDoList>(userLists)

            // Initialize the listCollection
            listCollection = mutableListOf()

            if (result != null){

                result.forEach{

                    // Add previous lists to the collection
                    listCollection.add(it)
                    updateAllLists()

                }
            } else {
                // If it is a first time user or something went wrong, load premade lists.
                load()

            }

        }.addOnFailureListener{

            // If file didnt download initialize premade lists.
            load()

        }

    }

    fun load(){

        // A set of premade lists in case the user is a first time user
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
        val fileName = "${unique_id}-Lists.json"
        val file = File(path, fileName)

        // Check if the user already has a file, if he does delete it and create a new one
        // I didnt find any other way to clear out the data already in the file on firebase than
        // this one.
        if(file.exists() && file.isFile){
            file.delete()
        }
        file.createNewFile()

        if (path != null){

            // Convert our listCollection to Json... Manually...
            var content: String = "[\n"
            listCollection.forEach { toDoList ->

                content = content + "{\n" + "\"listName\": " + "\"${toDoList.listName}\",\n" + "\"tasks\":[\n"

                toDoList.tasks.forEach {

                    content = content + "{\"taskName\": \"${it.taskName}\"," + "\"completed\": ${it.completed}},\n"

                }
                content = "$content]\n},\n"
            }

            content = "$content]"

            FileOutputStream(file, true).bufferedWriter().use { writer ->
                writer.write(content)
            }

            // Call the upload function
            upload(file.toUri())
        }

    }

    // Function to upload the user data to Firebase Storage.
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

    // Set a unique id for each user. Used in the filename for userdata, so that people only access
    // their own lists.
    fun setUniqueID(deviceID: String){
        unique_id = deviceID
    }

    // Make sure there is only one instance of the DepositoryManager.
    companion object {

        val instance = ToDoListDepositoryManager()

    }
}
