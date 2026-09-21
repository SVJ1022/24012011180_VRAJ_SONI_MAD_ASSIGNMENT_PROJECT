package com.example.a24012011180_vraj_soni_mad_assignment_project

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.File

class DigitalIDActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_digital_idactivity)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->

            val systemBars =
                insets.getInsets(WindowInsetsCompat.Type.systemBars())

            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )

            insets
        }

        // Get Student ID passed from StudentDashboardActivity
        val userId = intent.getIntExtra("userId", -1)

        if (userId == -1) {
            Toast.makeText(
                this,
                "Invalid Student ID",
                Toast.LENGTH_SHORT
            ).show()

            finish()
            return
        }

        val dbHelper = DatabaseHelper(this)
        val user = dbHelper.getUserById(userId)
        val digitalId = dbHelper.getDigitalID(userId)

        if (digitalId == null) {

            Toast.makeText(
                this,
                "Digital Id Not Assigned Yet",
                Toast.LENGTH_SHORT
            ).show()

            Intent(this, StudentDashboardActivity::class.java).also {
                it.putExtra("userId", userId)
                startActivity(it)
            }

            finish()
            return
        }

        val name = findViewById<TextView>(R.id.studentName)
        name.text = user?.fullName

        val enrollmentNo = findViewById<TextView>(R.id.enrollmentNo)
        enrollmentNo.text = digitalId.enrollmentNo


        val college = findViewById<TextView>(R.id.collegeValue)
        college.text = digitalId.college

        val degree = findViewById<TextView>(R.id.degreeValue)
        degree.text = digitalId.degree

        val bGroup = findViewById<TextView>(R.id.bGroupValue)
        bGroup.text = digitalId.bloodGroup

        val mobile = findViewById<TextView>(R.id.mobileValue)
        mobile.text = user?.phone


        val validity = findViewById<TextView>(R.id.validityValue)
        validity.text = digitalId.validity


        val photoHolder = findViewById<ImageView>(R.id.studentImage)
        val photoPath = digitalId.photo

        Log.i(
            "DIGITAL ID ACTIVITY",
            "Digital ID: $digitalId"
        )

        Log.i(
            "DIGITAL ID ACTIVITY",
            "Photo Path: $photoPath"
        )

        if (photoPath.isNotEmpty()) {
            val photoFile = File(photoPath)

            if (photoFile.exists()) {
                photoHolder.setImageURI(
                    Uri.fromFile(photoFile)
                )
            } else {
                Log.w(
                    "DIGITAL ID ACTIVITY",
                    "Photo file does not exist: $photoPath"
                )

            }
        }

        val backBtn = findViewById<Button>(R.id.backBtn)

        backBtn.setOnClickListener {
            finish()
        }
    }
}