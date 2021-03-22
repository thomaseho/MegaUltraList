package com.example.megaultralist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.megaultralist.databinding.ActivityMainBinding
import com.example.megaultralist.tasks.Task
import com.example.megaultralist.tasks.toDoList
import com.example.megaultralist.tasks.toDoListCollectionAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var listCollection:MutableList<toDoList> = mutableListOf(
            toDoList(listName = "Handleliste", tasks = mutableListOf(Task("Brød"),
            Task("Egg"), Task("Melk"))),

            toDoList(listName = "Julegaver", tasks = mutableListOf(Task("PS5"),
            Task("3dPrinter"), Task("Sokker"))),

            toDoList(listName = "Filmer å se", tasks = mutableListOf(Task("LOTR"),
            Task("Harry Potter"), Task("ikke GoT"))),

            toDoList(listName = "Bucket list", tasks = mutableListOf(Task("Ikke stryke i apputvikling"),
            Task("Lære å fly"), Task("Spise en vegetarburger som ikke smaker drit"))))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toDoListListing.layoutManager = LinearLayoutManager(this)
        binding.toDoListListing.adapter = toDoListCollectionAdapter(listCollection)
    }
}
