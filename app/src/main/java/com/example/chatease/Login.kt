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

class Login : AppCompatActivity() {

    private lateinit var btnJoin : TextView
    private lateinit var edtEmail : EditText
    private lateinit var edtPassword : EditText
    private lateinit var btnLogin : Button
    private lateinit var progressBar :ProgressBar

    private lateinit var mAuth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        supportActionBar?.hide()


        btnJoin = findViewById(R.id.btn_join)
        edtEmail = findViewById(R.id.et_Email)
        edtPassword = findViewById(R.id.et_password_login)
        btnLogin = findViewById(R.id.btn_login_login)
        progressBar = findViewById(R.id.otp_page_progress_bar)

        mAuth = FirebaseAuth.getInstance()
        progressBar.visibility = View.GONE
        btnLogin.setOnClickListener {
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                edtEmail.setError("Not valid")
                return@setOnClickListener
            }else{
                if (password.trim().equals("")){
                    edtPassword.setError("required")
                }else{
                    login(email,password)
                }

            }

        }
        btnJoin.setOnClickListener {
            val intent = Intent(this,SignUp::class.java)
            startActivity(intent)
            finish()
        }

    }
    private fun login(email: String, password: String) {
        //logic for logging in..

        setInProgress(true)
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val intent = Intent(this@Login,MainActivity::class.java)
                    startActivity(intent)
                    finish()

                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this@Login,"User Does not Exist", Toast.LENGTH_SHORT).show()
                    setInProgress(false)
                }
            }
    }
    fun setInProgress(inProgress : Boolean){
        if (inProgress){
            progressBar.visibility = View.VISIBLE
            btnLogin.visibility = View.GONE
        }
        else{
            progressBar.visibility = View.GONE
            btnLogin.visibility = View.VISIBLE
        }
    }
}