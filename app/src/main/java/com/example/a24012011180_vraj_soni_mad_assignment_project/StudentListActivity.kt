package com.example.a24012011180_vraj_soni_mad_assignment_project

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class StudentListActivity : AppCompatActivity() {
    lateinit var dbHelper: DatabaseHelper
    lateinit var studentList: Array<StudentListItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_student_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dbHelper = DatabaseHelper(this)

        studentList = dbHelper.getAllStudents()

        val recyclerView = findViewById<RecyclerView>(R.id.ListedStudents)

        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = StudentAdapter(studentList) { student ->
            val intent = Intent(this, AddDigitalIDActivity::class.java)
            intent.putExtra("userId", student.id)
            startActivity(intent)
        }

        recyclerView.adapter = adapter

        val backBtn = findViewById<Button>(R.id.backBtn)

        backBtn.setOnClickListener {
            finish()
        }
    }
}