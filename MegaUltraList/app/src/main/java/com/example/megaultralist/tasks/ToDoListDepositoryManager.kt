package com.example.megaultralist.tasks

import com.example.megaultralist.tasks.data.Task
import com.example.megaultralist.tasks.data.toDoList

class ToDoListDepositoryManager {

    private lateinit var listCollection:MutableList<toDoList>

    var onToDoLists:((List<toDoList>) -> Unit)? = null
    var onTodoListUpdate:((toDoList:toDoList) -> Unit)? = null

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

        onToDoLists?.invoke(listCollection)
    }

    fun addToDoList(toDoList: toDoList){

        listCollection.add(toDoList)
        onToDoLists?.invoke(listCollection)

    }

    fun updateToDoList(toDoList: toDoList){

        onTodoListUpdate?.invoke(toDoList)

    }

    companion object {

        val instance = ToDoListDepositoryManager()

    }

}