package com.example.firebasekotlincrud
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Videogame(val name: String? = null, val date: String? = null, val attend: ArrayList<String>? = null,val price: String? = null,  val description: String? = null, val url: String? = null, @Exclude val key: String? = null) {
}