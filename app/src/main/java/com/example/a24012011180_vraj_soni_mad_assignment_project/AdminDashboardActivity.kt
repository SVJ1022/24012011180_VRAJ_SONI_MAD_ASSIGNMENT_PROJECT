package com.example.a24012011180_vraj_soni_mad_assignment_project

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.card.MaterialCardView

class AdminDashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin_dashboard)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Receive the logged-in admin's userId so sub-screens can guard against self-delete
        val loggedInUserId = intent.getIntExtra("userId", -1)

        // ── Card 1: User Management ──────────────────────────────────────────
        findViewById<MaterialCardView>(R.id.MaterialCardAdmin).setOnClickListener {
            Intent(this, UserManagementActivity::class.java).also {
                it.putExtra("loggedInUserId", loggedInUserId)
                startActivity(it)
            }
        }

        // ── Card 2: Manage Digital IDs ───────────────────────────────────────
        findViewById<MaterialCardView>(R.id.digitalIdCard).setOnClickListener {
            Intent(this, StudentListActivity::class.java).also {
                startActivity(it)
            }
        }

        // ── ManageStudentBtn: kept per hard rule, hidden (redundant with cards) ──
        // The existing Kotlin still calls setOnClickListener on it, so keep it wired:
        findViewById<Button>(R.id.ManageStudentBtn).also { btn ->
            btn.visibility = View.GONE
            btn.setOnClickListener {
                Intent(this, StudentListActivity::class.java).also {
                    startActivity(it)
                }
            }
        }

        // ── Logout ───────────────────────────────────────────────────────────
        findViewById<Button>(R.id.LogoutBtnAdmin).setOnClickListener {
            Intent(this, LoginActivity::class.java).also {
                startActivity(it)
            }
        }
    }
}