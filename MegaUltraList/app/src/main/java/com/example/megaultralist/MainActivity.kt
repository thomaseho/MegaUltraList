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
        setContentView(binding.root)

        binding.toDoListListing.layoutManager = LinearLayoutManager(this)
        binding.toDoListListing.adapter = toDoListCollectionAdapter(listCollection)
    }
}
