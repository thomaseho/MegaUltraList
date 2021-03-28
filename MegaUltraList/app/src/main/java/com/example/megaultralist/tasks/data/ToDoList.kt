package com.example.megaultralist.tasks.data

import android.os.Parcelable
import com.example.megaultralist.tasks.data.Task
import kotlinx.android.parcel.Parcelize

@Parcelize
data class toDoList(val listName:String, var tasks:MutableList<Task>):Parcelable
// A toDoList is a collection of Tasks, that are to be completed.