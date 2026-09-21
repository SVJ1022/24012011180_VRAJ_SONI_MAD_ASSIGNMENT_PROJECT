package com.example.a24012011180_vraj_soni_mad_assignment_project

import android.app.AlertDialog
import android.graphics.Color
import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.io.File

/**
 * StudentAdapter — full-detail list with search filtering and long-press delete.
 *
 * Original IDs preserved in item_student.xml:
 *   studentName, enrollmentNo, batch, idStatus  — all still bound here.
 * New views in item_student.xml:
 *   avatarInitial (TextView for initial / photo avatar),
 *   tvCollegeDegree, tvBgroupValidity (new TextViews for extra detail lines)
 */
class StudentAdapter(
    masterList: Array<StudentListItem>,
    private val onStudentClick: (StudentListItem) -> Unit,
    private val onStudentDeleted: () -> Unit     // called after a delete so Activity can refresh
) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    // Master list never changes; displayList is filtered subset
    private val master: List<StudentListItem> = masterList.toList()
    private var displayList: MutableList<StudentListItem> = master.toMutableList()

    // ── Search filter ──────────────────────────────────────────────────────────

    /** Returns a TextWatcher ready to wire to a search field. */
    fun buildTextWatcher(): TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, st: Int, c: Int, a: Int) = Unit
        override fun onTextChanged(s: CharSequence?, st: Int, c: Int, a: Int) = Unit
        override fun afterTextChanged(s: Editable?) {
            filter(s?.toString() ?: "")
        }
    }

    fun filter(query: String) {
        val q = query.trim().lowercase()
        displayList = if (q.isEmpty()) {
            master.toMutableList()
        } else {
            master.filter { s ->
                s.fullName.lowercase().contains(q) ||
                s.enrollmentNo.lowercase().contains(q) ||
                s.batch.lowercase().contains(q) ||
                s.college.lowercase().contains(q) ||
                s.idStatus.lowercase().contains(q)
            }.toMutableList()
        }
        notifyDataSetChanged()
    }

    fun getDisplayCount(): Int = displayList.size

    // ── Adapter overrides ──────────────────────────────────────────────────────

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_student, parent, false)
        return StudentViewHolder(view)
    }

    override fun getItemCount(): Int = displayList.size

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = displayList[position]

        // ── Name (original ID: studentName) ──
        holder.tvStudentName.text = student.fullName

        // ── Enrollment No (original ID: enrollmentNo) ──
        holder.tvEnrollmentNo.text =
            if (student.enrollmentNo == "-" || student.enrollmentNo.isEmpty()) "No enrollment"
            else student.enrollmentNo

        // ── Batch line (original ID: batch) — reuse for "College · Degree" ──
        holder.tvBatch.text = if (student.idStatus == "Assigned") {
            buildString {
                if (student.college.isNotEmpty()) append(student.college)
                if (student.college.isNotEmpty() && student.degree.isNotEmpty()) append(" · ")
                if (student.degree.isNotEmpty()) append(student.degree)
            }.ifEmpty { student.batch }
        } else {
            "Not assigned yet"
        }

        // ── Extra line: Blood Group · Valid till (new view tvBgroupValidity) ──
        holder.tvBgroupValidity?.let { tv ->
            if (student.idStatus == "Assigned") {
                val bg = student.bloodGroup.trim()
                val val_ = student.validity.trim()
                tv.text = buildString {
                    if (bg.isNotEmpty()) append("B.Grp: $bg")
                    if (bg.isNotEmpty() && val_.isNotEmpty()) append("  ·  ")
                    if (val_.isNotEmpty()) append("Valid till $val_")
                }
                tv.visibility = View.VISIBLE
            } else {
                tv.visibility = View.GONE
            }
        }

        // ── Avatar (original ID: avatarInitial) ──
        // Show photo if assigned + file exists, else navy initial circle.
        val avatar = holder.itemView.findViewById<TextView>(R.id.avatarInitial)
        if (student.idStatus == "Assigned" && student.photoPath.isNotEmpty()) {
            val file = File(student.photoPath)
            if (file.exists()) {
                // We cannot show an image in a TextView; fall back to showing
                // the ImageView overlay if present, or just show the initial.
                // item_student.xml has an ImageView id=avatarPhoto layered over the
                // initial TextView. If that view exists, show photo there.
                val photoView = holder.itemView.findViewById<ImageView?>(R.id.avatarPhoto)
                if (photoView != null) {
                    photoView.setImageURI(Uri.fromFile(file))
                    photoView.visibility = View.VISIBLE
                    avatar.visibility = View.INVISIBLE
                } else {
                    // No separate photo view — show initial on navy circle
                    avatar.text = student.fullName.firstOrNull()?.uppercaseChar()?.toString() ?: "S"
                    avatar.visibility = View.VISIBLE
                }
            } else {
                showInitial(avatar, student.fullName)
            }
        } else {
            showInitial(avatar, student.fullName)
            // Hide photo overlay if it exists
            holder.itemView.findViewById<ImageView?>(R.id.avatarPhoto)?.visibility = View.GONE
        }

        // ── Status chip (original ID: idStatus) ──
        val assigned = student.idStatus == "Assigned"
        holder.tvIdStatus.text = if (assigned) "Assigned" else "Pending"
        if (assigned) {
            holder.tvIdStatus.setTextColor(
                holder.itemView.context.getColor(R.color.colorSuccess))
            holder.tvIdStatus.setBackgroundResource(R.drawable.bg_chip_success)
        } else {
            holder.tvIdStatus.setTextColor(
                holder.itemView.context.getColor(R.color.colorWarning))
            holder.tvIdStatus.setBackgroundResource(R.drawable.bg_chip_warning)
        }

        // ── Tap → open Add/Edit Digital ID ──
        holder.itemView.setOnClickListener { onStudentClick(student) }

        // ── Long-press → confirm delete (only if ID is assigned) ──
        holder.itemView.setOnLongClickListener {
            if (student.idStatus == "Assigned") {
                AlertDialog.Builder(holder.itemView.context)
                    .setTitle("Remove Digital ID?")
                    .setMessage("Delete the Digital ID for ${student.fullName}? This cannot be undone.")
                    .setPositiveButton("Remove") { _, _ ->
                        val ctx = holder.itemView.context
                        val db  = DatabaseHelper(ctx)
                        // Delete photo file
                        if (student.photoPath.isNotEmpty()) {
                            val f = File(student.photoPath)
                            if (f.exists()) f.delete()
                        }
                        db.deleteDigitalID(student.id)
                        onStudentDeleted()          // Activity will refresh
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
            }
            true
        }
    }

    private fun showInitial(avatar: TextView, name: String) {
        avatar.text = name.firstOrNull()?.uppercaseChar()?.toString() ?: "S"
        avatar.visibility = View.VISIBLE
    }

    // ── ViewHolder ─────────────────────────────────────────────────────────────

    class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Original IDs — unchanged
        val tvStudentName: TextView  = itemView.findViewById(R.id.studentName)
        val tvEnrollmentNo: TextView = itemView.findViewById(R.id.enrollmentNo)
        val tvBatch: TextView        = itemView.findViewById(R.id.batch)
        val tvIdStatus: TextView     = itemView.findViewById(R.id.idStatus)
        // New optional view (null-safe for layout compatibility)
        val tvBgroupValidity: TextView? = itemView.findViewById(R.id.tvBgroupValidity)
    }
}