package com.example.megaultralist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.megaultralist.databinding.ActivityMainBinding
import com.example.megaultralist.tasks.CreateNewToDoList
import com.example.megaultralist.tasks.ToDoListDepositoryManager
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        auth = Firebase.auth
        signInAnonymously()

        setContentView(binding.root)

        binding.toDoListListing.layoutManager = LinearLayoutManager(this)
        binding.toDoListListing.adapter = toDoListCollectionAdapter(emptyList<toDoList>(), this::onToDoListClicked)
        binding.newListButton.setOnClickListener {

            createNewListButton()

        }

        ToDoListDepositoryManager.instance.onToDoLists = {
            (binding.toDoListListing.adapter as toDoListCollectionAdapter).updateToDoListCollection(it)
        }

        ToDoListDepositoryManager.instance.onChanges = {
            ToDoListDepositoryManager.instance.upload(it)
        }

        ToDoListDepositoryManager.instance.load()

    }

    private fun signInAnonymously(){

        auth.signInAnonymously().addOnSuccessListener {
            Log.d(TAG, "Login success ${it.user}")
        }.addOnFailureListener {
            Log.e(TAG, "Login failed", it)
        }

    }


    private fun createNewListButton(){

        val intent = Intent(this, CreateNewToDoList::class.java)

        startActivity(intent)

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
