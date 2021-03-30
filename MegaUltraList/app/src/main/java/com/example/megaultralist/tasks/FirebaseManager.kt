package com.example.megaultralist.tasks

import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import com.beust.klaxon.Klaxon
import com.example.megaultralist.tasks.data.toDoList
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.io.File
import java.io.FileOutputStream

class FirebaseManager {

    private lateinit var unique_id: String

    val TAG:String = "MegaUltraList:FirebaseManager"

    fun loadFirebase() {

        val userListRef = Firebase.storage.reference.child("userlists/${unique_id}-Lists.json")
        val ONE_MEGABYTE: Long = 1024 * 1024

        userListRef.getBytes(ONE_MEGABYTE).addOnSuccessListener {

            val userLists = String(it)

            val result = Klaxon().parseArray<toDoList>(userLists)

            ToDoListDepositoryManager.instance.listCollection = mutableListOf()

            if (result != null){

                result.forEach{

                    ToDoListDepositoryManager.instance.listCollection.add(it)
                    ToDoListDepositoryManager.instance.updateAllLists()

                }
            } else {

                ToDoListDepositoryManager.instance.load()

            }

        }.addOnFailureListener{

            ToDoListDepositoryManager.instance.load()

        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun saveData(filePath: File?){
        ToDoListDepositoryManager.instance.updateAllLists()
        val path = filePath
        val fileName = "${unique_id}-Lists.json"
        val file = File(path, fileName)

        if(file.exists() && file.isFile){
            file.delete()
        }
        file.createNewFile()

        if (path != null){

            var content: String = "[\n"
            ToDoListDepositoryManager.instance.listCollection.forEach { toDoList ->

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

    fun setUniqueID(deviceID: String){
        unique_id = deviceID
    }

    companion object {

        val instance = FirebaseManager()

    }
}