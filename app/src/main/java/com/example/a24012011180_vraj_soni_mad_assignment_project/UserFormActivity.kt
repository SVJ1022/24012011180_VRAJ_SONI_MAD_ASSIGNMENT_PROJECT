package com.example.a24012011180_vraj_soni_mad_assignment_project

import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

/**
 * UserFormActivity — shared CREATE and EDIT form for users.
 *
 * Intent extras:
 *   "userId"         Int  — present → EDIT mode; absent → CREATE mode
 *   "loggedInUserId" Int  — passed through for context (not used in form itself)
 *
 * Password rule:
 *   CREATE: must not be blank, min 4 chars
 *   EDIT:   blank = keep existing password (fetched from DB); non-blank = update it
 */
class UserFormActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private var editUserId: Int = -1   // -1 means CREATE mode
    private var existingPassword: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_user_form)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val sys = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(sys.left, sys.top, sys.right, sys.bottom)
            insets
        }

        dbHelper    = DatabaseHelper(this)
        editUserId  = intent.getIntExtra("userId", -1)
        val isEdit  = editUserId != -1

        // ── Title ──────────────────────────────────────────────────────────
        val formTitle = findViewById<TextView>(R.id.formTitle)
        formTitle.text = getString(if (isEdit) R.string.lbl_edit_user else R.string.lbl_add_user)

        // ── Fields ─────────────────────────────────────────────────────────
        val etName     = findViewById<TextInputEditText>(R.id.etFullName)
        val etEmail    = findViewById<TextInputEditText>(R.id.etEmail)
        val etPhone    = findViewById<TextInputEditText>(R.id.etPhone)
        val etBatch    = findViewById<TextInputEditText>(R.id.etBatch)
        val etPassword = findViewById<TextInputEditText>(R.id.etPassword)
        val tilPassword = findViewById<TextInputLayout>(R.id.tilPassword)
        val spinnerRole = findViewById<Spinner>(R.id.spinnerRole)

        // ── Role spinner ───────────────────────────────────────────────────
        val roles = arrayOf("STUDENT", "ADMIN")
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, roles)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerRole.adapter = spinnerAdapter

        // ── Password hint differs between create and edit ──────────────────
        if (isEdit) {
            tilPassword.hint = getString(R.string.hint_password_edit)
        }

        // ── Pre-fill if editing ─────────────────────────────────────────────
        if (isEdit) {
            val user = dbHelper.getUserById(editUserId)
            if (user != null) {
                existingPassword = user.password
                etName.setText(user.fullName)
                etEmail.setText(user.email)
                etPhone.setText(user.phone)
                etBatch.setText(user.batch)
                // Never pre-fill password; leave blank so admin must re-enter or leave blank
                val roleIndex = roles.indexOf(user.role.uppercase())
                if (roleIndex >= 0) spinnerRole.setSelection(roleIndex)
            }
        }

        // ── Save ────────────────────────────────────────────────────────────
        findViewById<Button>(R.id.btnSave).setOnClickListener {
            val name     = etName.text.toString().trim()
            val email    = etEmail.text.toString().trim()
            val phone    = etPhone.text.toString().trim()
            val batch    = etBatch.text.toString().trim()
            val password = etPassword.text.toString()
            val role     = spinnerRole.selectedItem.toString()

            // Validate required fields
            if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || batch.isEmpty()) {
                Toast.makeText(this, R.string.err_fill_all_fields, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validate email format
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, R.string.err_invalid_email, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Check duplicate email (exclude self in edit mode)
            val excludeId = if (isEdit) editUserId else -1
            if (dbHelper.emailExists(email, excludeId)) {
                Toast.makeText(this, R.string.err_email_exists, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Password rules
            val finalPassword: String
            if (isEdit) {
                finalPassword = if (password.isEmpty()) existingPassword else password
                if (password.isNotEmpty() && password.length < 4) {
                    Toast.makeText(this, R.string.err_password_short, Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            } else {
                if (password.isEmpty()) {
                    Toast.makeText(this, R.string.err_fill_all_fields, Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if (password.length < 4) {
                    Toast.makeText(this, R.string.err_password_short, Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                finalPassword = password
            }

            // Persist
            if (isEdit) {
                dbHelper.updateUser(User(
                    Id       = editUserId,
                    fullName = name,
                    email    = email,
                    phone    = phone,
                    batch    = batch,
                    password = finalPassword,
                    role     = role
                ))
                Toast.makeText(this, R.string.msg_user_updated, Toast.LENGTH_SHORT).show()
            } else {
                dbHelper.insertUser(User(
                    fullName = name,
                    email    = email,
                    phone    = phone,
                    batch    = batch,
                    password = finalPassword,
                    role     = role
                ))
                Toast.makeText(this, R.string.msg_user_created, Toast.LENGTH_SHORT).show()
            }

            finish()   // Return to UserManagementActivity (onResume will reload)
        }

        // ── Cancel ──────────────────────────────────────────────────────────
        findViewById<Button>(R.id.btnCancelForm).setOnClickListener { finish() }
    }
}
