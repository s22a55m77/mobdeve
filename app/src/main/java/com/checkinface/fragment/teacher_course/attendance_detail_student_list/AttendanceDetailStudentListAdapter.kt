package com.checkinface.fragment.teacher_course.attendance_detail_student_list

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.checkinface.R
import com.checkinface.databinding.AttendanceDetailStudentItemLayoutBinding
import com.checkinface.fragment.student_attendance_list.AttendanceStatus
import com.checkinface.util.FirestoreAttendanceHelper
import com.checkinface.util.VariableHolder
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AttendanceDetailStudentListAdapter(private val data: ArrayList<AttendanceDetailStudentModel>): Adapter<AttendanceDetailStudentListViewHolder>() {
    private val firestoreAttendanceHelper = FirestoreAttendanceHelper()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AttendanceDetailStudentListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AttendanceDetailStudentItemLayoutBinding.inflate(inflater, parent, false)

        return AttendanceDetailStudentListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: AttendanceDetailStudentListViewHolder, position: Int) {
        holder.bindData(data[position])

        val coroutineScope =
            holder.itemView.findViewTreeLifecycleOwner()?.lifecycleScope ?: CoroutineScope(Dispatchers.IO)

        holder.itemView.setOnClickListener {
            val layoutInflater = LayoutInflater.from(holder.itemView.context)
            val modalView = layoutInflater.inflate(R.layout.modify_student_attendance_modal_layout, null)
            val modal = MaterialAlertDialogBuilder(holder.itemView.context).setView(modalView).create()
            modal.show()

            val btnPresent = modalView.findViewById<Button>(R.id.btn_modify_student_present)
            val btnAbsent = modalView.findViewById<Button>(R.id.btn_modify_student_absent)
            val btnLate = modalView.findViewById<Button>(R.id.btn_modify_student_late)

//            val sp = holder.itemView.context.getSharedPreferences("COURSE_FILE", Context.MODE_PRIVATE)
//            val courseCode = sp.getString("COURSE_CODE", "")
//            val eventTime = sp.getString("EVENT_TIME", "")
            val courseCode = VariableHolder.getInstance().courseCode
            val eventTime = VariableHolder.getInstance().eventTime

            btnPresent.setOnClickListener {
                coroutineScope.launch {
                    firestoreAttendanceHelper.updateAttendance(
                        courseCode!!,
                        data[position].email,
                        eventTime!!,
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
                }
            }

            btnAbsent.setOnClickListener {
                coroutineScope.launch {
                    firestoreAttendanceHelper.updateAttendance(
                        courseCode!!,
                        data[position].email,
                        eventTime!!,
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
                }
            }

            btnLate.setOnClickListener {
                coroutineScope.launch {
                    firestoreAttendanceHelper.updateAttendance(
                        courseCode!!,
                        data[position].email,
                        eventTime!!,
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
                }
            }
        }
    }
}