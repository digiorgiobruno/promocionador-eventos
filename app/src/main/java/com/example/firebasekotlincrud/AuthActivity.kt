package com.example.firebasekotlincrud
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.firebasekotlincrud.databinding.ActivityAuthBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_auth.*


class AuthActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding:ActivityAuthBinding
    private  var rbCheck =false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        //setup

        rbCheck = binding.RBsesion.isChecked
        rbclick()
        val sharedPreferences = getSharedPreferences(
            "packageName",
            Context.MODE_PRIVATE
        )
        //sharedPreferences.edit().putString("username","Valor predeterminado").apply()

        if (sharedPreferences.getString("username", "false")=="false") {
            //Toast.makeText(this, "no hay sesion seteada",Toast.LENGTH_SHORT).show()
            setup(auth)
        }else{
            //Toast.makeText(this, sharedPreferences.getString("username", "false"),Toast.LENGTH_SHORT).show()
            sharedPreferences.getString("username", "false")?.let { showHome(it) }
        }

    }

    private fun rbclick(){

        binding.RBsesion.setOnClickListener(View.OnClickListener {
        binding.RBsesion.isChecked = !rbCheck
                rbCheck = binding.RBsesion.isChecked
        })
    }
    private fun setup(auth:FirebaseAuth) {
        title = "Autentificación"


        binding.registerBotton.setOnClickListener {


            if (binding.emailEditText.text.isNotEmpty() && binding.passwordEditText.text.isNotEmpty()){
                val email = binding.emailEditText.text.toString()
                val password=binding.passwordEditText.text.toString()
                //Toast.makeText(this, "Presionaste Registrar $email $password",Toast.LENGTH_SHORT).show()


                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {

                            showHome(task.result?.user?.email?:"")
                        } else {
                            showAlert()
                        }
                    }

            }

        }
        binding.loginBotton.setOnClickListener {
            //Toast.makeText(this,"Presionaste Acceder",Toast.LENGTH_SHORT).show()
            if (binding.emailEditText.text.isNotEmpty() && binding.passwordEditText.text.isNotEmpty()){
                val email = binding.emailEditText.text.toString()
                val password=binding.passwordEditText.text.toString()
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener{
                    if(it.isSuccessful){

                        val user = auth.currentUser
                        if (rbCheck) {
                        val sharedPreferences = getSharedPreferences(
                            "packageName",
                            Context.MODE_PRIVATE
                        )
                        sharedPreferences.edit().putString("username",it.result?.user?.email ?: "").apply()

                        }
                        showHome(it.result?.user?.email?:"")
                    }else{
                        showAlert()
                    }
                }
            }

        }
    }
    private fun showAlert(){
        val builder= AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error autentcando al usuario")
        builder.setPositiveButton("Aceptar",null)
        val dialog:AlertDialog =builder.create()
        dialog.show()
    }
    private fun showHome(email: String){

        val homeIntent= Intent(this,MainActivity::class.java).apply {
            putExtra("email",email)

        }
        startActivity(homeIntent)
    } 
}