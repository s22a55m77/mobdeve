package com.checkinface.fragment.teacher_course.attendance_detail_student_list

import android.content.Context
import android.os.Bundle
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.checkinface.R
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

        val sp = view.context.getSharedPreferences("COURSE_FILE", Context.MODE_PRIVATE)
        val courseCode = sp.getString("COURSE_CODE", "")
        val eventTime = sp.getString("EVENT_TIME", "")

        if(courseCode != "" && courseCode != null && eventTime != null) {
            lifecycleScope.launch {
                attendanceDetailStudentList = firestoreAttendanceHelper.getStudentListsBasedOnEvent(courseCode, eventTime)
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

        // handle filter check
        this.chipPresent = view.findViewById(R.id.chip_filter_present)
        this.chipAbsent = view.findViewById(R.id.chip_filter_absent)
        this.chipLate = view.findViewById(R.id.chip_filter_late)
        chipPresent.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked)
                chipPresent.chipBackgroundColor = resources.getColorStateList(R.color.md_theme_light_primaryContainer)
            else
                chipPresent.chipBackgroundColor = resources.getColorStateList(com.google.android.material.R.color.m3_ref_palette_white)
        }

        chipAbsent.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked)
                chipAbsent.chipBackgroundColor = resources.getColorStateList(R.color.md_theme_light_primaryContainer)
            else
                chipAbsent.chipBackgroundColor = resources.getColorStateList(com.google.android.material.R.color.m3_ref_palette_white)
        }

        chipLate.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked)
                chipLate.chipBackgroundColor = resources.getColorStateList(R.color.md_theme_light_primaryContainer)
            else
                chipLate.chipBackgroundColor = resources.getColorStateList(com.google.android.material.R.color.m3_ref_palette_white)
        }
    }
}