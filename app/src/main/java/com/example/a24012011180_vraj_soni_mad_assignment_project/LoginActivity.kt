package com.example.a24012011180_vraj_soni_mad_assignment_project

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )

            insets
        }

        val email = findViewById<EditText>(R.id.EmailInputField)
        val password = findViewById<EditText>(R.id.PasswordInputField)
        val loginBtn = findViewById<Button>(R.id.login_activity_loginBtn)

        loginBtn.setOnClickListener {

            val emailText = email.text.toString()
            val passwordText = password.text.toString()

            val dbHelper = DatabaseHelper(this)
            val user = dbHelper.getUser(emailText, passwordText)

            if (user != null) {

                if (user.role == "STUDENT") {

                    Intent(this, StudentDashboardActivity::class.java).also {
                        it.putExtra("userId", user.Id)
                        startActivity(it)
                    }

                } else if (user.role == "ADMIN") {

                    Intent(this, AdminDashboardActivity::class.java).also {
                        it.putExtra("userId", user.Id)
                        startActivity(it)
                    }

                }

            } else {

                Toast.makeText(
                    this,
                    "Invalid Email or Password",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        findViewById<Button>(R.id.login_activity_signUpBtn).setOnClickListener {
            Intent(this, SignupActivity::class.java).also {
                startActivity(it)
            }
        }
    }
}