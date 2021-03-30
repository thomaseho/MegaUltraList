package com.example.megaultralist.tasks.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Task(val taskName:String, var completed:Boolean):Parcelable