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

    @SuppressLint("HardwareIds")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        auth = Firebase.auth
        signInAnonymously()

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


        ToDoListDepositoryManager.instance.onChanges = {
            saveLists()
        }

        ToDoListDepositoryManager.instance.loadFirebase()

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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveLists(){
        val path = this.getExternalFilesDir(null)
        ToDoListDepositoryManager.instance.saveData(path)
    }
}
