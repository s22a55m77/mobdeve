package com.checkinface.fragment.teacher_course.attendance_list

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.checkinface.R
import com.checkinface.activity.create_attendance.CreateAttendanceActivity
import com.checkinface.util.FirestoreAttendanceHelper
import kotlinx.coroutines.launch

class TeacherCourseAttendanceListFragment : Fragment() {
    private val firestoreAttendanceHelper = FirestoreAttendanceHelper()
    private var attendanceList: ArrayList<TeacherAttendanceModel> = arrayListOf()
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // animation
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

        val sp = view.context.getSharedPreferences("COURSE_FILE", Context.MODE_PRIVATE)
        val courseCode = sp.getString("COURSE_CODE", "")
        if(courseCode != "" && courseCode != null)
            lifecycleScope.launch {
                attendanceList = firestoreAttendanceHelper.getEventsBasedOnCourse(courseCode)
                recyclerView.adapter = AttendanceListAdapter(attendanceList)
            }

    }

    override fun onResume() {
        super.onResume()
        val view = requireView()
        val sp = view.context.getSharedPreferences("COURSE_FILE", Context.MODE_PRIVATE)
        val courseCode = sp.getString("COURSE_CODE", "")
        if(courseCode != "" && courseCode != null)
            lifecycleScope.launch {
                attendanceList = firestoreAttendanceHelper.getEventsBasedOnCourse(courseCode)
                recyclerView.adapter = AttendanceListAdapter(attendanceList)
            }
    }
}