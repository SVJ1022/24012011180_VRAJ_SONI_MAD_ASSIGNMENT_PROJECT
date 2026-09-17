package com.example.a24012011180_vraj_soni_mad_assignment_project

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<Button>(R.id.LoginBtn).setOnClickListener {
            Intent(this, LoginActivity::class.java).also{
                startActivity(it)
            }
        }

        findViewById<Button>(R.id.SignUpBtn).setOnClickListener {
            Intent(this, SignupActivity::class.java).also{
                startActivity(it)
            }
        }
    }


}