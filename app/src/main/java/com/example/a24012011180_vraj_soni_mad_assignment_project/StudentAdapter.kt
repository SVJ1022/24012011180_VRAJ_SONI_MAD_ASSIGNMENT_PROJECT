package com.example.a24012011180_vraj_soni_mad_assignment_project

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StudentAdapter(val studentList: Array<StudentListItem>,
                     val onStudentClick: (StudentListItem) -> Unit):
    RecyclerView.Adapter<StudentAdapter.StudentViewHolder>(){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StudentViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_student, parent, false)

        return StudentViewHolder(itemView)
    }

    override fun onBindViewHolder(
        holder: StudentViewHolder,
        position: Int
    ) {
        val student = studentList[position]

        holder.tvStudentName.text = student.fullName
        holder.tvEnrollmentNo.text = student.enrollmentNo
        holder.tvBatch.text = student.batch
        holder.tvIdStatus.text = student.idStatus

        holder.itemView.setOnClickListener {
            onStudentClick(student)
        }
    }

    override fun getItemCount(): Int {
        return studentList.size
    }

    class StudentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val tvStudentName: TextView =
            itemView.findViewById<TextView>(R.id.studentName)

        val tvEnrollmentNo: TextView =
            itemView.findViewById<TextView>(R.id.enrollmentNo)

        val tvBatch: TextView =
            itemView.findViewById<TextView>(R.id.batch)

        val tvIdStatus: TextView =
            itemView.findViewById<TextView>(R.id.idStatus)
    }
}