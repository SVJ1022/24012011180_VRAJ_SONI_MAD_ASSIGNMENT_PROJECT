package com.example.a24012011180_vraj_soni_mad_assignment_project

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.io.File

class UserManagementActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var adapter: UserAdapter
    private lateinit var tvCount: TextView
    private lateinit var tvEmpty: TextView
    private lateinit var recyclerView: RecyclerView
    private var loggedInUserId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_user_management)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val sys = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(sys.left, sys.top, sys.right, sys.bottom)
            insets
        }

        loggedInUserId = intent.getIntExtra("loggedInUserId", -1)

        dbHelper     = DatabaseHelper(this)
        tvCount      = findViewById(R.id.tvUserCount)
        tvEmpty      = findViewById(R.id.tvUsersEmptyState)
        recyclerView = findViewById(R.id.ListedUsers)
        recyclerView.layoutManager = LinearLayoutManager(this)

        loadList()

        // ── Search ──────────────────────────────────────────────────────────
        val searchLayout = findViewById<TextInputLayout>(R.id.searchInputLayout)
        val searchEdit   = findViewById<TextInputEditText>(R.id.searchEditText)

        searchEdit.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, st: Int, c: Int, a: Int) = Unit
            override fun onTextChanged(s: CharSequence?, st: Int, c: Int, a: Int) = Unit
            override fun afterTextChanged(s: android.text.Editable?) {
                adapter.filter(s?.toString() ?: "")
                updateCountAndEmpty()
            }
        })
        searchLayout.setEndIconOnClickListener {
            searchEdit.setText("")
            adapter.filter("")
            updateCountAndEmpty()
        }

        // ── FAB: open UserFormActivity in CREATE mode ────────────────────────
        findViewById<FloatingActionButton>(R.id.fabAddUser).setOnClickListener {
            Intent(this, UserFormActivity::class.java).also {
                it.putExtra("loggedInUserId", loggedInUserId)
                startActivity(it)
            }
        }
    }

    private fun loadList() {
        val users = dbHelper.getAllUsers()

        adapter = UserAdapter(
            masterList       = users,
            onUserClick      = { user ->
                // Open form in EDIT mode
                Intent(this, UserFormActivity::class.java).also {
                    it.putExtra("userId", user.Id)
                    it.putExtra("loggedInUserId", loggedInUserId)
                    startActivity(it)
                }
            },
            onUserLongClick  = { user -> confirmDelete(user) }
        )

        recyclerView.adapter = adapter
        updateCountAndEmpty()
    }

    private fun updateCountAndEmpty() {
        val n = adapter.getDisplayCount()
        tvCount.text       = if (n == 1) "1 user" else "$n users"
        tvEmpty.visibility = if (n == 0) View.VISIBLE else View.GONE
    }

    private fun confirmDelete(user: User) {
        // Guard: cannot delete self
        if (user.Id == loggedInUserId) {
            Toast.makeText(this,
                getString(R.string.err_cannot_delete_self), Toast.LENGTH_SHORT).show()
            return
        }
        // Guard: cannot delete last admin
        if (user.role.equals("ADMIN", ignoreCase = true) && dbHelper.countAdmins() <= 1) {
            Toast.makeText(this,
                getString(R.string.err_last_admin), Toast.LENGTH_SHORT).show()
            return
        }

        AlertDialog.Builder(this)
            .setTitle(R.string.dlg_delete_user_title)
            .setMessage("${getString(R.string.dlg_delete_user_msg)}\n\nUser: ${user.fullName}")
            .setPositiveButton(R.string.dlg_delete_confirm) { _, _ ->
                val photoPath = dbHelper.deleteUserById(user.Id)
                if (photoPath.isNotEmpty()) {
                    val f = File(photoPath)
                    if (f.exists()) f.delete()
                }
                loadList()
            }
            .setNegativeButton(R.string.btn_cancel, null)
            .show()
    }

    override fun onResume() {
        super.onResume()
        loadList()
    }
}
