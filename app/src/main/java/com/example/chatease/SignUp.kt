package com.example.chatease

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUp : AppCompatActivity() {
    private lateinit var btnLogin : TextView
    private lateinit var edtName : EditText
    private lateinit var edtEmail : EditText
    private lateinit var edtPassword : EditText
    private lateinit var btnSignup : Button
    private lateinit var mDbRef : DatabaseReference
    private lateinit var mAuth :FirebaseAuth
    private lateinit var progressBar :ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)
        supportActionBar?.hide()

        btnLogin = findViewById(R.id.btn_login)
        edtName = findViewById(R.id.et_fullName)
        edtEmail = findViewById(R.id.et_Email)
        edtPassword = findViewById(R.id.et_password)
        btnSignup = findViewById(R.id.btn_signup)
        progressBar = findViewById(R.id.otp_page_progress_bar)

        mAuth = FirebaseAuth.getInstance()

        progressBar.visibility=View.GONE
        btnSignup.setOnClickListener {
            val name = edtName.text.toString()
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()


            if(name.trim().equals("")){
                edtName.setError("name required")
                return@setOnClickListener
            }else{
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    edtEmail.setError("not valid")
                    return@setOnClickListener
                }else{
                    val passLength = 6
                    if (password.length < passLength){
                        edtPassword.setError("minimum 6 Char required")
                        return@setOnClickListener
                    }else{
                        signup(name,email,password)
                    }
                }
            }


        }

        btnLogin.setOnClickListener {
            val intent = Intent(this,Login::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun signup(name : String , email: String, password: String) {
        //logic for creating new users

        setInProgress(true)
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    addUserToDataBase(name,email,mAuth.currentUser?.uid!!)

                    val intent = Intent(this@SignUp,MainActivity::class.java)
                    Toast.makeText(this@SignUp,"Done", Toast.LENGTH_SHORT).show()
                    startActivity(intent)
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this@SignUp,"Not Valid", Toast.LENGTH_SHORT).show()
                    setInProgress(false)

                }
            }

    }
    private fun addUserToDataBase(name: String, email: String, uid: String) {

        mDbRef = FirebaseDatabase.getInstance().getReference()
        mDbRef.child("Users").child(uid).setValue(User(name,email,uid))
    }

    fun setInProgress(inProgress : Boolean){
        if (inProgress){
            progressBar.visibility = View.VISIBLE
            btnSignup.visibility = View.GONE
        }
        else{
            progressBar.visibility = View.GONE
            btnSignup.visibility = View.VISIBLE
        }
    }
}