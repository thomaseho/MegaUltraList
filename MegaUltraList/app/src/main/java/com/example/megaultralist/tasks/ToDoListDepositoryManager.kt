package com.example.megaultralist.tasks

import android.net.Uri
import androidx.core.net.toUri
import com.example.megaultralist.MainActivity
import com.example.megaultralist.ToDoListHolder
import com.example.megaultralist.databinding.ActivityMainBinding
import com.example.megaultralist.tasks.data.Task
import com.example.megaultralist.tasks.data.toDoList
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.File
import java.io.FileOutputStream

class ToDoListDepositoryManager {

    private lateinit var listCollection:MutableList<toDoList>

    var onToDoLists:((List<toDoList>) -> Unit)? = null
    var onTasks:((List<Task>) -> Unit)? = null
    var onTodoListUpdate:((toDoList:toDoList) -> Unit)? = null
    var onChanges:((file: Uri) -> Unit)? = null

    val storage = Firebase.storage
    val storageRef = storage.reference

    fun load(){
        listCollection = mutableListOf(

         toDoList(listName = "Handleliste", tasks = mutableListOf(
            Task("Brød", true),
            Task("Egg", false),
            Task("Melk", true)
        )),

        toDoList(listName = "Julegaver", tasks = mutableListOf(
            Task("PS5", false),
            Task("3dPrinter", true),
            Task("Sokker", false)
        )),

        toDoList(listName = "Filmer å se", tasks = mutableListOf(
            Task("LOTR", true),
            Task("Harry Potter", true),
            Task("ikke GoT", true)
        )),

        toDoList(listName = "Bucket list", tasks = mutableListOf(
            Task("Ikke stryke i apputvikling", false),
            Task("Lære å fly", false),
            Task("Spise en vegetarburger som ikke smaker drit", false)
        )),

        toDoList(listName = "Handleliste", tasks = mutableListOf(
            Task("Brød", false),
            Task("Egg", false),
            Task("Melk", false)
        )),

        toDoList(listName = "Julegaver", tasks = mutableListOf(
            Task("PS5", false),
            Task("3dPrinter", false),
            Task("Sokker", false)
        )),

        toDoList(listName = "Filmer å se", tasks = mutableListOf(
            Task("LOTR", false),
            Task("Harry Potter", false),
            Task("ikke GoT", false)
        )),
        toDoList(listName = "Bucket list", tasks = mutableListOf(
            Task("Ikke stryke i apputvikling", false),
            Task("Lære å fly", false),
            Task("Spise en vegetarburger som ikke smaker drit", false)
        )),
        toDoList(listName = "Handleliste", tasks = mutableListOf(
            Task("Brød", false),
            Task("Egg", false),
            Task("Melk", false)
        )),

        toDoList(listName = "Julegaver", tasks = mutableListOf(
            Task("PS5", false),
            Task("3dPrinter", false),
            Task("Sokker", false)
        )),

        toDoList(listName = "Filmer å se", tasks = mutableListOf(
            Task("LOTR", false),
            Task("Harry Potter", false),
            Task("ikke GoT", false)
        )),

        toDoList(listName = "Bucket list", tasks = mutableListOf(
            Task("Ikke stryke i apputvikling", false),
            Task("Lære å fly", false),
            Task("Spise en vegetarburger som ikke smaker drit", false)
        ))
        )

        updateAllLists()
    }

    fun addToDoList(toDoList: toDoList){

        listCollection.add(toDoList)
        onToDoLists?.invoke(listCollection)

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

    }

    fun addTaskToList(toDoList: toDoList?, task: Task){

        if (toDoList != null){
            toDoList.tasks.add(task)
            updateAllLists()
            updateToDoListTasks(toDoList.tasks)
        }

    }

    fun updateTaskCompletion(task: Task, status: Boolean){

        task.completed = status
        updateAllLists()
        ToDoListHolder.PickedToDoList?.let { updateToDoListTasks(it.tasks) }

    }

    fun removeTaskFromList(toDoList: toDoList?, task: Task){

        if (toDoList != null){
            toDoList.tasks.remove(task)
            updateAllLists()
            updateToDoListTasks(toDoList.tasks)
            saveData()
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

    fun saveData(){

        updateAllLists()
        val fileName = "userlists/userLists.json"
        val stream = File(fileName)

        var content: String = "{\n"
        listCollection.forEach { toDoList ->
            content = content + "    \"todolist\":   {\n" + "\"listname\": " + "\"${toDoList.listName}\",\n" + "\"tasks\":  [\n"
            toDoList.tasks.forEach{
                content = content + "    {\"taskName\": \"${it.taskName}\"," + " \"completed\": ${it.completed}},\n"
            }
            content = content + "    ]\n" + "    },\n"
        }
        content = content + "}"

        //file.bufferedWriter().use { writer ->
        // writer.write(content)
        //}

        //this.onChanges?.invoke(file.toUri())

    }

    companion object {

        val instance = ToDoListDepositoryManager()

    }

}
