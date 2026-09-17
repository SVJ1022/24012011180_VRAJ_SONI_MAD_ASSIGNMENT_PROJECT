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

class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val dbHelper = DatabaseHelper(this)

        val newStudentName = findViewById<EditText>(R.id.fullNameInputField)
        val mail = findViewById<EditText>(R.id.EmailInputField)
        val phone = findViewById<EditText>(R.id.PhoneInputField)
        val batch = findViewById<EditText>(R.id.batchField)
        val pwd = findViewById<EditText>(R.id.PasswordInputField)
        val cpwd = findViewById<EditText>(R.id.ConfirmPasswordField)
        val personType = findViewById<EditText>(R.id.roleInputField)

        val submitBtn = findViewById<Button>(R.id.CreateAccountBtn)



        submitBtn.setOnClickListener {
            val fname = newStudentName.text.toString()
            val mailAd = mail.text.toString()
            val contact = phone.text.toString()
            val btc = batch.text.toString()
            val passwd = pwd.text.toString()
            val cwd = cpwd.text.toString()
            val post = personType.text.toString()

            if(fname.isEmpty() ||
                mailAd.isEmpty() ||
                contact.isEmpty() ||
                btc.isEmpty() ||
                passwd.isEmpty() ||
                cwd.isEmpty()||
                post.isEmpty()){

                Toast.makeText(
                    this,
                    "Please fill all Fields",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            if(dbHelper.checkUser(mailAd)){
                Toast.makeText(
                    this,
                    "You are ALready Registered",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            } else {
                try{
                    if(passwd == cwd){
                        val user = User(fullName = fname,email = mailAd,phone = contact,batch = btc, password = passwd, role = post)
                        dbHelper.insertUser(user)
                        Toast.makeText(
                            this,
                            "Account Created Successfully",
                            Toast.LENGTH_SHORT
                        ).show()

                        Intent(this, LoginActivity::class.java).also{
                            startActivity(it)
                        }

                    } else {
                        Toast.makeText(
                            this,
                            "Confirm Password Does Not Match",
                            Toast.LENGTH_SHORT
                        ).show()

                        return@setOnClickListener
                    }
                } catch (e: Exception){
                    e.printStackTrace()
                }
            }
        }
        findViewById<Button>(R.id.signup_activity_loginBtn).setOnClickListener {
            Intent(this, LoginActivity::class.java).also{
                startActivity(it)
            }
        }
    }
}