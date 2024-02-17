package com.checkinface.fragment.teacher_course.attendance_list

import android.content.Intent
import android.os.Bundle
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.checkinface.R
import com.checkinface.activity.create_attendance.CreateAttendanceActivity

class TeacherCourseAttendanceListFragment : Fragment() {
    private val attendanceList: ArrayList<TeacherAttendanceModel> = AttendanceDataGenerator.loadData()
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = TransitionInflater.from(requireContext())
        enterTransition = inflater.inflateTransition(R.transition.slide)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_teacher_course_attendance_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val addAttendanceBtn = view.findViewById<Button>(R.id.btn_add_attendace)

        addAttendanceBtn.setOnClickListener {
            val intent = Intent(requireActivity().applicationContext, CreateAttendanceActivity::class.java)
            startActivity(intent)
        }

        this.recyclerView = view.findViewById(R.id.rv_attendance_list)

        val linearLayoutManager = LinearLayoutManager(activity?.applicationContext)
        this.recyclerView.layoutManager = linearLayoutManager

        this.recyclerView.adapter = AttendanceListAdapter(this.attendanceList)
    }
}