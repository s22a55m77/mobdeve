package com.checkinface.fragment.student_attendance_list

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.checkinface.R
import com.checkinface.databinding.StudentAttendanceItemLayoutBinding
import com.checkinface.util.DateUtil
import com.checkinface.util.FirestoreAttendanceHelper
import com.checkinface.util.UserRole
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StudentAttendanceListAdapter(private val data: ArrayList<StudentAttendanceModel>, private val role: UserRole): Adapter<StudentAttendanceListViewHolder>() {
    private val firestoreAttendanceHelper = FirestoreAttendanceHelper()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentAttendanceListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = StudentAttendanceItemLayoutBinding.inflate(inflater, parent, false)

        return StudentAttendanceListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: StudentAttendanceListViewHolder, position: Int) {
        holder.bindData(data[position])

        val coroutineScope = holder.itemView.findViewTreeLifecycleOwner()?.lifecycleScope ?: CoroutineScope(Dispatchers.IO)


        // only show modal on teacher side
        if (role == UserRole.TEACHER) {
            holder.itemView.setOnClickListener {
                val layoutInflater = LayoutInflater.from(holder.itemView.context)
                val modalView = layoutInflater.inflate(R.layout.modify_student_attendance_modal_layout, null)
                val modal = MaterialAlertDialogBuilder(holder.itemView.context).setView(modalView).create()
                modal.show()

                // Initialize buttons
                val btnPresent = modalView.findViewById<Button>(R.id.btn_modify_student_present)
                val btnAbsent = modalView.findViewById<Button>(R.id.btn_modify_student_absent)
                val btnLate = modalView.findViewById<Button>(R.id.btn_modify_student_late)

                val sp = holder.itemView.context.getSharedPreferences("COURSE_FILE", Context.MODE_PRIVATE)
                val courseCode = sp.getString("COURSE_CODE", "")
                val studentEmail = sp.getString("STUDENT_EMAIL", "")

                btnPresent.setOnClickListener {
                    coroutineScope.launch {
                        try {
                            firestoreAttendanceHelper.updateAttendance(
                                courseCode!!,
                                studentEmail!!,
                                DateUtil.getFormattedDate("yyyy-MM-dd HH:mm:ss", data[position].date),
                                "PRESENT",
                                fun() {
                                    Toast.makeText(holder.itemView.context, "Change Applied", Toast.LENGTH_LONG).show()
                                    data[position].status = AttendanceStatus.PRESENT
                                    notifyItemChanged(position)
                                    modal.dismiss()
                                },
                                fun(e) {
                                    Toast.makeText(holder.itemView.context, "Error: ${e.message.toString()}", Toast.LENGTH_LONG).show()
                                }
                            )
                        } catch (e: Exception) {
                            Toast.makeText(holder.itemView.context, "Error: $e", Toast.LENGTH_LONG).show()
                        }
                    }
                }

                btnLate.setOnClickListener {
                    coroutineScope.launch {
                        try {
                            firestoreAttendanceHelper.updateAttendance(
                                courseCode!!,
                                studentEmail!!,
                                DateUtil.getFormattedDate("yyyy-MM-dd HH:mm:ss", data[position].date),
                                "LATE",
                                fun() {
                                    Toast.makeText(holder.itemView.context, "Change Applied", Toast.LENGTH_LONG).show()
                                    data[position].status = AttendanceStatus.LATE
                                    notifyItemChanged(position)
                                    modal.dismiss()
                                },
                                fun(e) {
                                    Toast.makeText(holder.itemView.context, "Error: ${e.message.toString()}", Toast.LENGTH_LONG).show()
                                }
                            )
                        } catch (e: Exception) {
                            Toast.makeText(holder.itemView.context, "Error: $e", Toast.LENGTH_LONG).show()
                        }
                    }
                }

                btnAbsent.setOnClickListener {
                    coroutineScope.launch {
                        try {
                            firestoreAttendanceHelper.updateAttendance(
                                courseCode!!,
                                studentEmail!!,
                                DateUtil.getFormattedDate("yyyy-MM-dd HH:mm:ss", data[position].date),
                                "ABSENT",
                                fun() {
                                    Toast.makeText(holder.itemView.context, "Change Applied", Toast.LENGTH_LONG).show()
                                    data[position].status = AttendanceStatus.ABSENT
                                    notifyItemChanged(position)
                                    modal.dismiss()
                                },
                                fun(e) {
                                    Toast.makeText(holder.itemView.context, "Error: ${e.message.toString()}", Toast.LENGTH_LONG).show()
                                }
                            )
                        } catch (e: Exception) {
                            Toast.makeText(holder.itemView.context, "Error: $e", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }


    }

}