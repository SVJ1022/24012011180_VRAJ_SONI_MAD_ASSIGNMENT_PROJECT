package com.example.a24012011180_vraj_soni_mad_assignment_project

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.io.FileOutputStream

class AddDigitalIDActivity : AppCompatActivity() {

    private var photoPath: String = ""
    private lateinit var photo: ImageView
    private val imagePicker =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->

            if (uri != null) {

                // Show selected image
                photo.setImageURI(uri)

                // Save image into app storage
                photoPath = saveImageToInternalStorage(uri)

                Toast.makeText(
                    this,
                    "Photo selected successfully",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_add_digital_id)

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

        val userId = intent.getIntExtra("userId", -1)
        val dbHelper = DatabaseHelper(this)
        val user = dbHelper.getUserById(userId)

        findViewById<TextView>(R.id.StudentName).text = user?.fullName

        val enrollmentNo = findViewById<EditText>(R.id.enrollmentNo)
        val college = findViewById<EditText>(R.id.college)
        val degree = findViewById<EditText>(R.id.degree)
        val bGroup = findViewById<EditText>(R.id.bGroup)
        val validity = findViewById<EditText>(R.id.validity)

        photo = findViewById(R.id.uploadPhoto)

        val saveDigitalIDBtn = findViewById<Button>(R.id.saveDigitalID)

        if (dbHelper.checkDigitalID(userId)) {

            val digitalID = dbHelper.getDigitalID(userId)

            enrollmentNo.setText(digitalID?.enrollmentNo)
            college.setText(digitalID?.college)
            degree.setText(digitalID?.degree)
            bGroup.setText(digitalID?.bloodGroup)
            validity.setText(digitalID?.validity)

            photoPath = digitalID?.photo ?: ""

            if (photoPath.isNotEmpty()) {
                photo.setImageURI(Uri.fromFile(File(photoPath)))
            }
        }

        photo.setOnClickListener {

            imagePicker.launch("image/*")

        }
        saveDigitalIDBtn.setOnClickListener {

            val enrollment = enrollmentNo.text.toString()
            val collegeName = college.text.toString()
            val degreeName = degree.text.toString()
            val bloodGroup = bGroup.text.toString()
            val validityPeriod = validity.text.toString()


            // Check photo
            if (enrollment.isEmpty()||
                collegeName.isEmpty()||
                degreeName.isEmpty()||
                bloodGroup.isEmpty()||
                validityPeriod.isEmpty()||
                photoPath.isEmpty()) {

                Toast.makeText(
                    this,
                    "Please fill all Fields",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            if(dbHelper.checkDigitalID(userId)){
                dbHelper.updateDigitalId(
                    userId,
                    enrollment,
                    collegeName,
                    degreeName,
                    bloodGroup,
                    validityPeriod,
                    photoPath
                )

                Toast.makeText(
                    this,
                    "Digital ID Updated",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                dbHelper.insertDigitalId(
                    userId,
                    enrollment,
                    collegeName,
                    degreeName,
                    bloodGroup,
                    validityPeriod,
                    photoPath
                )

                Toast.makeText(
                    this,
                    "Digital ID Assigned",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        findViewById<Button>(R.id.backBtn).setOnClickListener {
            finish()
        }
    }

    private fun saveImageToInternalStorage(uri: Uri): String {

        val inputStream = contentResolver.openInputStream(uri)

        val fileName = "student_${System.currentTimeMillis()}.jpg"

        val file = File(filesDir, fileName)

        val outputStream = FileOutputStream(file)

        inputStream?.copyTo(outputStream)

        inputStream?.close()
        outputStream.close()

        return file.absolutePath
    }
}