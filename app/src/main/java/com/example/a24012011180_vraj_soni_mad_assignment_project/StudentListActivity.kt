package com.example.a24012011180_vraj_soni_mad_assignment_project

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.search.SearchBar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class StudentListActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var adapter: StudentAdapter
    private lateinit var tvStudentCount: TextView
    private lateinit var tvEmpty: TextView
    private lateinit var recyclerView: RecyclerView

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
        tvStudentCount = findViewById(R.id.StudentsList)   // reusing existing id for count label
        tvEmpty       = findViewById(R.id.tvEmptyState)
        recyclerView  = findViewById(R.id.ListedStudents)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Hide the addBtn (redundant with tapping a row; ID kept per hard rules)
        findViewById<View>(R.id.addBtn).visibility = View.GONE

        // Hide the old column-header card (ID kept)
        findViewById<View>(R.id.TableCard).visibility = View.GONE

        loadList()

        // ── Back button ──────────────────────────────────────────────────────
        findViewById<Button>(R.id.backBtn).setOnClickListener { finish() }

        // ── Search wiring ────────────────────────────────────────────────────
        // The layout uses a TextInputLayout (id=searchInputLayout) wrapping a
        // TextInputEditText (id=searchEditText) instead of the M3 SearchBar
        // so we can get TextWatcher + end-icon clear.
        val searchLayout = findViewById<TextInputLayout?>(R.id.searchInputLayout)
        val searchEdit   = findViewById<TextInputEditText?>(R.id.searchEditText)

        searchEdit?.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, st: Int, c: Int, a: Int) = Unit
            override fun onTextChanged(s: CharSequence?, st: Int, c: Int, a: Int)     = Unit
            override fun afterTextChanged(s: android.text.Editable?) {
                adapter.filter(s?.toString() ?: "")
                updateCountAndEmpty()
            }
        })

        // Clear icon clears the query
        searchLayout?.setEndIconOnClickListener {
            searchEdit?.setText("")
            adapter.filter("")
            updateCountAndEmpty()
        }
    }

    /** Load (or reload) list from DB and rebuild adapter. */
    private fun loadList() {
        val studentList = dbHelper.getAllStudents()

        adapter = StudentAdapter(
            masterList      = studentList,
            onStudentClick  = { student ->
                val intent = Intent(this, AddDigitalIDActivity::class.java)
                intent.putExtra("userId", student.id)
                startActivity(intent)
            },
            onStudentDeleted = {
                // After delete: close DB handles and reload
                loadList()
            }
        )

        recyclerView.adapter = adapter
        updateCountAndEmpty()
    }

    /** Update the student count label and empty-state view. */
    private fun updateCountAndEmpty() {
        val count = adapter.getDisplayCount()
        tvStudentCount.text = if (count == 1) "1 student" else "$count students"
        tvEmpty.visibility  = if (count == 0) View.VISIBLE else View.GONE
    }

    /** Re-query DB when returning from Add/Edit screen. */
    override fun onResume() {
        super.onResume()
        loadList()
    }
}