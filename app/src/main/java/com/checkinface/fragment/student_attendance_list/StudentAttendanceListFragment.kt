package com.checkinface.fragment.student_attendance_list

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.checkinface.R
import com.checkinface.databinding.FragmentCourseBinding
import com.checkinface.util.UserRole
import com.checkinface.util.UserSharedPreference


class StudentAttendanceListFragment : Fragment() {

    private val attendanceModelList = StudentAttendanceDataGenerator.loadData()
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewBinding: FragmentCourseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = TransitionInflater.from(requireContext())
        enterTransition = inflater.inflateTransition(R.transition.slide)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()

        this.recyclerView = view.findViewById(R.id.rv_course)

        val linearLayoutManager = LinearLayoutManager(activity?.applicationContext)
        this.recyclerView.layoutManager = linearLayoutManager

        this.recyclerView.adapter = StudentAttendanceListAdapter(this.attendanceModelList)

        view.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                view.viewTreeObserver.removeOnPreDrawListener(this)
                startPostponedEnterTransition()
                return true
            }
        })

        val user = UserSharedPreference(requireContext())

        if (user.getRole() == UserRole.TEACHER) {
            viewBinding.fabCheck.visibility = View.GONE
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewBinding = FragmentCourseBinding.inflate(inflater, container, false)
        return viewBinding.root
    }
}