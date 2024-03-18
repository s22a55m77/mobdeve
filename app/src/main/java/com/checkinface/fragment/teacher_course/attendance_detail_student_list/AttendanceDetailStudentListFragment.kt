package com.checkinface.fragment.teacher_course.attendance_detail_student_list

import android.content.Context
import android.os.Bundle
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.checkinface.R
import com.checkinface.fragment.student_attendance_list.AttendanceStatus
import com.checkinface.util.FirestoreAttendanceHelper
import com.google.android.material.chip.Chip
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch

class AttendanceDetailStudentListFragment : Fragment() {
    private var attendanceDetailStudentList: ArrayList<AttendanceDetailStudentModel> = arrayListOf()
    private lateinit var recyclerView: RecyclerView
    private lateinit var chipPresent: Chip
    private lateinit var chipAbsent: Chip
    private lateinit var chipLate: Chip
    private lateinit var emptyView: LinearLayout
    private val firestoreAttendanceHelper: FirestoreAttendanceHelper = FirestoreAttendanceHelper()

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
        return inflater.inflate(R.layout.fragment_attendance_detail_student_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // stop animation
        postponeEnterTransition()
        this.recyclerView = view.findViewById(R.id.rv_attendance_detail_student_list)
        val linearLayoutManager = LinearLayoutManager(activity?.applicationContext)
        this.recyclerView.layoutManager = linearLayoutManager

        this.emptyView = view.findViewById(R.id.empty_view)

        val sp = view.context.getSharedPreferences("COURSE_FILE", Context.MODE_PRIVATE)
        val courseCode = sp.getString("COURSE_CODE", "")
        val eventTime = sp.getString("EVENT_TIME", "")

        if(courseCode != "" && courseCode != null && eventTime != null) {
            lifecycleScope.launch {
                attendanceDetailStudentList = firestoreAttendanceHelper.getStudentListsBasedOnEvent(courseCode, eventTime)
                if(attendanceDetailStudentList.size == 0)
                    emptyView.visibility = LinearLayout.VISIBLE
                recyclerView.adapter = AttendanceDetailStudentListAdapter(attendanceDetailStudentList)
            }
        }

        // resume animation
        view.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                view.viewTreeObserver.removeOnPreDrawListener(this)
                startPostponedEnterTransition()
                return true
            }
        })

        // Handle filter
        fun filterAttendanceListByStatus(attendanceList: ArrayList<AttendanceDetailStudentModel>, statuses: ArrayList<AttendanceStatus>): ArrayList<AttendanceDetailStudentModel> {
            val filteredList = ArrayList<AttendanceDetailStudentModel>()
            for (student in attendanceList) {
                if (student.status in statuses) {
                    filteredList.add(student)
                }
            }
            return filteredList
        }

        // Selected List
        val selectedStatuses: ArrayList<AttendanceStatus> = arrayListOf(AttendanceStatus.PRESENT, AttendanceStatus.LATE, AttendanceStatus.ABSENT)

        // handle filter check
        this.chipPresent = view.findViewById(R.id.chip_filter_present)
        this.chipAbsent = view.findViewById(R.id.chip_filter_absent)
        this.chipLate = view.findViewById(R.id.chip_filter_late)
        chipPresent.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) {
                chipPresent.chipBackgroundColor = resources.getColorStateList(R.color.md_theme_light_primaryContainer)
                selectedStatuses.add(AttendanceStatus.PRESENT)
                val filterList = filterAttendanceListByStatus(attendanceDetailStudentList, selectedStatuses)
                if(filterList.size == 0)
                    emptyView.visibility = LinearLayout.VISIBLE
                else
                    emptyView.visibility = LinearLayout.GONE
                recyclerView.adapter = AttendanceDetailStudentListAdapter(filterList)
            }
            else {
                chipPresent.chipBackgroundColor = resources.getColorStateList(com.google.android.material.R.color.m3_ref_palette_white)
                selectedStatuses.remove(AttendanceStatus.PRESENT)
                val filterList = filterAttendanceListByStatus(attendanceDetailStudentList, selectedStatuses)
                if(filterList.size == 0)
                    emptyView.visibility = LinearLayout.VISIBLE
                else
                    emptyView.visibility = LinearLayout.GONE
                recyclerView.adapter = AttendanceDetailStudentListAdapter(filterList)
            }
        }

        chipAbsent.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) {
                chipAbsent.chipBackgroundColor = resources.getColorStateList(R.color.md_theme_light_primaryContainer)
                selectedStatuses.add(AttendanceStatus.ABSENT)
                val filterList = filterAttendanceListByStatus(attendanceDetailStudentList, selectedStatuses)
                if(filterList.size == 0)
                    emptyView.visibility = LinearLayout.VISIBLE
                else
                    emptyView.visibility = LinearLayout.GONE
                recyclerView.adapter = AttendanceDetailStudentListAdapter(filterList)
            }
            else {
                chipAbsent.chipBackgroundColor = resources.getColorStateList(com.google.android.material.R.color.m3_ref_palette_white)
                selectedStatuses.remove(AttendanceStatus.ABSENT)
                val filterList = filterAttendanceListByStatus(attendanceDetailStudentList, selectedStatuses)
                if(filterList.size == 0)
                    emptyView.visibility = LinearLayout.VISIBLE
                else
                    emptyView.visibility = LinearLayout.GONE
                recyclerView.adapter = AttendanceDetailStudentListAdapter(filterList)
            }
        }

        chipLate.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) {
                chipLate.chipBackgroundColor = resources.getColorStateList(R.color.md_theme_light_primaryContainer)
                selectedStatuses.add(AttendanceStatus.LATE)
                val filterList = filterAttendanceListByStatus(attendanceDetailStudentList, selectedStatuses)
                if(filterList.size == 0)
                    emptyView.visibility = LinearLayout.VISIBLE
                else
                    emptyView.visibility = LinearLayout.GONE
                recyclerView.adapter = AttendanceDetailStudentListAdapter(filterList)
            }
            else {
                chipLate.chipBackgroundColor = resources.getColorStateList(com.google.android.material.R.color.m3_ref_palette_white)
                selectedStatuses.remove(AttendanceStatus.LATE)
                val filterList = filterAttendanceListByStatus(attendanceDetailStudentList, selectedStatuses)
                if(filterList.size == 0)
                    emptyView.visibility = LinearLayout.VISIBLE
                else
                    emptyView.visibility = LinearLayout.GONE
                recyclerView.adapter = AttendanceDetailStudentListAdapter(filterList)
            }
        }
    }
}