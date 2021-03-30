package com.example.megaultralist.tasks.data

import android.os.Parcelable
import com.example.megaultralist.tasks.data.Task
import kotlinx.android.parcel.Parcelize

@Parcelize
data class toDoList(val listName:String, var tasks:MutableList<Task>, var progress: Int):Parcelable