package com.example.a24012011180_vraj_soni_mad_assignment_project

import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import java.io.File

class DigitalIDActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_digital_idactivity)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val userId = intent.getIntExtra("userId", -1)
        val dbHelper = DatabaseHelper(this)
        val user = dbHelper.getUserById(userId)
        val digitalId = dbHelper.getDigitalID(userId)

        if(digitalId == null){
            Toast.makeText(this,"Digital Id Not Assigned Yet", Toast.LENGTH_SHORT).show()
            finish()
        }

        val Name = findViewById<TextView>(R.id.studentName)
        Name.text = user?.fullName

        val enNO = findViewById<TextView>(R.id.enrollmentNo)
        enNO.text = digitalId?.enrollmentNo

        val clgName = findViewById<TextView>(R.id.collegeLabel)
        clgName.text = digitalId?.college

        val degree = findViewById<TextView>(R.id.degreeLabel)
        degree.text = digitalId?.degree

        val bGroup = findViewById<TextView>(R.id.bGroupLabel)
        bGroup.text = digitalId?.bloodGroup

        val mobile = findViewById<TextView>(R.id.mobileLabel)
        mobile.text = user?.phone

        val validity = findViewById<TextView>(R.id.validityLabel)
        validity.text = digitalId?.validity

        val photoHolder = findViewById<ImageView>(R.id.studentImage)
        photoHolder.setImageURI(Uri.fromFile(File(digitalId?.photo ?: "")))

        
    }
}