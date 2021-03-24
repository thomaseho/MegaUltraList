package com.example.megaultralist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.megaultralist.databinding.ActivityMainBinding
import com.example.megaultralist.tasks.data.Task
import com.example.megaultralist.tasks.data.toDoList
import com.example.megaultralist.tasks.toDoListCollectionAdapter
import com.example.megaultralist.tasks.toDoListDetailsActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.list_layout.view.*


const val EXTRA_TODOLIST_INFO: String = "com.example.megaultralist.tasks.info"
const val REQUEST_TODOLIST_DETAILS:Int = 564567

class ToDoListHolder {

    companion object {
        var PickedToDoList:toDoList? = null
    }
}

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    private val TAG:String = "Mega Ultra List:Mainactivity"

    private var listCollection:MutableList<toDoList> = mutableListOf(
            toDoList(listName = "Handleliste", tasks = mutableListOf(Task("Brød", true),
            Task("Egg", false), Task("Melk", true))),

            toDoList(listName = "Julegaver", tasks = mutableListOf(Task("PS5", false),
            Task("3dPrinter", true), Task("Sokker", false))),

            toDoList(listName = "Filmer å se", tasks = mutableListOf(Task("LOTR", true),
            Task("Harry Potter", true), Task("ikke GoT", true))),

            toDoList(listName = "Bucket list", tasks = mutableListOf(Task("Ikke stryke i apputvikling", false),
            Task("Lære å fly", false), Task("Spise en vegetarburger som ikke smaker drit", false))),

            toDoList(listName = "Handleliste", tasks = mutableListOf(Task("Brød", false),
                    Task("Egg", false), Task("Melk", false))),

            toDoList(listName = "Julegaver", tasks = mutableListOf(Task("PS5", false),
                    Task("3dPrinter", false), Task("Sokker", false))),

            toDoList(listName = "Filmer å se", tasks = mutableListOf(Task("LOTR", false),
                    Task("Harry Potter", false), Task("ikke GoT", false))),
            toDoList(listName = "Bucket list", tasks = mutableListOf(Task("Ikke stryke i apputvikling", false),
                    Task("Lære å fly", false), Task("Spise en vegetarburger som ikke smaker drit", false))),
            toDoList(listName = "Handleliste", tasks = mutableListOf(Task("Brød", false),
                    Task("Egg", false), Task("Melk", false))),

            toDoList(listName = "Julegaver", tasks = mutableListOf(Task("PS5", false),
                    Task("3dPrinter", false), Task("Sokker", false))),

            toDoList(listName = "Filmer å se", tasks = mutableListOf(Task("LOTR", false),
                    Task("Harry Potter", false), Task("ikke GoT", false))),

            toDoList(listName = "Bucket list", tasks = mutableListOf(Task("Ikke stryke i apputvikling", false),
                    Task("Lære å fly", false), Task("Spise en vegetarburger som ikke smaker drit", false))))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        auth = Firebase.auth
        signInAnonymously()

        setContentView(binding.root)

        binding.toDoListListing.layoutManager = LinearLayoutManager(this)
        binding.toDoListListing.adapter = toDoListCollectionAdapter(listCollection, this::onToDoListClicked)

    }

    private fun signInAnonymously(){

        auth.signInAnonymously().addOnSuccessListener {
            Log.d(TAG, "Login success ${it.user}")
        }.addOnFailureListener {
            Log.e(TAG, "Login failed", it)
        }

    }

    private fun addToDoList(listName: String, tasks: MutableList<Task>){
        // Addding later
    }

    private fun onToDoListClicked(toDoList: toDoList): Unit {

        ToDoListHolder.PickedToDoList = toDoList

        val intent = Intent(this, toDoListDetailsActivity::class.java)

        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_TODOLIST_DETAILS){

        }
    }
}
