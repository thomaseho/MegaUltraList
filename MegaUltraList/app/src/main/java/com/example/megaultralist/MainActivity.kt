package com.example.megaultralist

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.getSystemService
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.megaultralist.databinding.ActivityMainBinding
import com.example.megaultralist.databinding.ActivityToDoListDetailsBinding
import com.example.megaultralist.tasks.*
import com.example.megaultralist.tasks.data.Task
import com.example.megaultralist.tasks.data.toDoList
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.list_layout.view.*
import java.io.File

class ToDoListHolder {
    // Holder for picked lists, makes it easier to access said list in other areas of the codebase
    companion object {
        var PickedToDoList:toDoList? = null
    }
}

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    private val TAG:String = "Mega Ultra List:Mainactivity"

    @SuppressLint("HardwareIds")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        auth = Firebase.auth
        signInAnonymously()


        // Setup for a unique id, so that the file saved on firebase is unique to each user.
        val secureID = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        ToDoListDepositoryManager.instance.setUniqueID(secureID)

        setContentView(binding.root)

        binding.toDoListListing.layoutManager = LinearLayoutManager(this)
        binding.toDoListListing.adapter = toDoListCollectionAdapter(emptyList<toDoList>(), this::onToDoListClicked)

        binding.newListButton.setOnClickListener {

            createNewListButton()

        }

        ToDoListDepositoryManager.instance.onToDoLists = {
            (binding.toDoListListing.adapter as toDoListCollectionAdapter).updateToDoListCollection(it)
        }

        // When changes are made to either lists, tasks or completion of tasks the changes are uploaded to Firebase.
        ToDoListDepositoryManager.instance.onChanges = {
            saveLists()
        }

        // Download the userfile from Firebase, if it is a firsttime user a set of premade lists appear.
        ToDoListDepositoryManager.instance.loadFirebase()

    }

    // Sign in without user to Firebase
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

    // When a list is clicked, enter a new activity with a detailed view of that list.
    private fun onToDoListClicked(toDoList: toDoList): Unit {

        ToDoListHolder.PickedToDoList = toDoList

        val intent = Intent(this, toDoListDetailsActivity::class.java)

        startActivity(intent)
    }

    // Couldnt figure out another way to get the path to SD_Card, created it here and sent it
    // to the depository manager.
    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveLists(){
        val path = this.getExternalFilesDir(null)
        ToDoListDepositoryManager.instance.saveData(path)
    }
}
