package com.example.firebasekotlincrud

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_add.*

class AddActivity : AppCompatActivity() {

    private val database = Firebase.database

    override fun onCreate(savedInstanceState: Bundle?) {
         val sharedPreferences = getSharedPreferences(
            "packageName",
            Context.MODE_PRIVATE
        )
        println(sharedPreferences.getString("username", "false"))
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        val myRef = database.getReference("videogame")

        val name=nameEditText.text
        val date=dateEditText.text
        val price=priceEditText.text
        val description=descriptionEditText.text
        val url=urlEditText.text
        var attend= ArrayList<String>()
            attend.add("Indice")
            sharedPreferences.getString("username", "false")?.let {

                attend.add(it) }


        saveButton.setOnClickListener { v ->
            val evento = Videogame(name.toString(), date.toString(), attend,price.toString(), description.toString(), url.toString())
            myRef.child(myRef.push().key.toString()).setValue(evento)
            finish()
        }
    }
}