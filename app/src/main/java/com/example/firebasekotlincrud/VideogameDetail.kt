package com.example.firebasekotlincrud

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.Html
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_videogame_detail.*

class VideogameDetail : AppCompatActivity() {
    private lateinit var attend:ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_videogame_detail)

        val key = intent.getStringExtra("key")
        val database = Firebase.database
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS") val myRef = database.getReference("videogame").child(key.toString())

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val videogame:Videogame? = dataSnapshot.getValue(Videogame::class.java)
                if (videogame != null) {
                    nameTextView.text = videogame.name.toString()
                    descriptionTextView.text = videogame.description.toString()
                    images(videogame.url.toString())
                    attend= videogame.attend!!
                    buttonAttend()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("TAG", "Failed to read value.", error.toException())
            }
        })

        attendButton.setOnClickListener{
            //val attend: List<String> = listOf("Bruno","Franco")

            val sharedPreferences = getSharedPreferences(
                "packageName",
                Context.MODE_PRIVATE
            )
            var flag=false
            var c=0
            attend.forEach {

                if(it==sharedPreferences.getString("username", "false")){
                    attend.removeAt(c)
                    flag=true
                }
                c++
            }

            if(!flag){
                sharedPreferences.getString("username", "false")?.let { it1 -> attend.add(it1) }

            }
            if(attend==null){

                myRef.child("attend").setValue(listOf("creador"))

            }else{
            myRef.child("attend").setValue(attend)}

            if(attendButton.text=="Asistiré"){
                attendButton.text="No asistiré"
                attendButton.setBackgroundResource(R.drawable.ic_btn_no_atten)
                showAlert("Irás a este evento")
            }else{
                attendButton.text="Asistiré"
                showAlert("Ya no irás a este evento :(")
                attendButton.setBackgroundResource(R.drawable.ic_btn_atten)
                }

        }

    }

    private fun buttonAttend(){
        val sharedPreferences = getSharedPreferences(
            "packageName",
            Context.MODE_PRIVATE
        )

        var flag=false

        attend.forEach {

            if(it==sharedPreferences.getString("username", "false")){

                flag=true
            }

        }

    if(flag){
        attendButton.text="No asistiré"
        attendButton.setBackgroundResource(R.drawable.ic_btn_no_atten)

    }else{
        attendButton.text="Asistiré"
        attendButton.setBackgroundResource(R.drawable.ic_btn_atten)
    }
}

    private fun showAlert(mensaje:String){
        val builder= AlertDialog.Builder(this)
        builder.setTitle("Dialogo")
        builder.setMessage(mensaje)
        builder. setPositiveButton(Html.fromHtml("<font color='#FF0000'>Aceptar</font>"),null)
        val dialog: AlertDialog =builder.create()
        dialog.show()
    }
    private  fun images(url: String){
        Glide.with(applicationContext)
            .load(url)
            .into(posterImgeView)

        Glide.with(applicationContext)
            .load(url)
            .into(backgroundImageView)
    }




}