package com.checkinface.fragment.student_attendance_list

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.checkinface.R
import com.checkinface.databinding.FragmentStudentAttendanceBinding
import com.checkinface.util.CheckAttendanceUtil
import com.checkinface.util.VariableHolder
import com.checkinface.util.FirestoreEventHelper
import com.checkinface.util.FirestoreStudentHelper
import com.checkinface.util.FirestoreUserHelper
import com.checkinface.util.UserRole
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch


class StudentAttendanceListFragment : Fragment() {

    private var attendanceModelList: ArrayList<StudentAttendanceModel> = arrayListOf()
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewBinding: FragmentStudentAttendanceBinding
    private lateinit var emptyView: LinearLayout
    private lateinit var progressBar: CircularProgressIndicator
    private val firestoreUserHelper: FirestoreUserHelper = FirestoreUserHelper()
    private val firestoreStudentHelper: FirestoreStudentHelper = FirestoreStudentHelper()
    private val firestoreEventHelper: FirestoreEventHelper = FirestoreEventHelper()
    private var userRole: UserRole? = null
    private var patternResult: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = TransitionInflater.from(requireContext())
        enterTransition = inflater.inflateTransition(R.transition.slide)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // stop animation
        postponeEnterTransition()

        this.recyclerView = view.findViewById(R.id.rv_student_attendance_list)

        val linearLayoutManager = LinearLayoutManager(activity?.applicationContext)
        this.recyclerView.layoutManager = linearLayoutManager

        this.emptyView = view.findViewById(R.id.empty_view)
        this.progressBar = view.findViewById(R.id.progress_circular)

//        val sp = view.context.getSharedPreferences("COURSE_FILE", Context.MODE_PRIVATE)
//        val courseCode = sp.getString("COURSE_CODE", "")
        val courseCode = VariableHolder.getInstance().courseCode
        lifecycleScope.launch {
            userRole = firestoreUserHelper.getRole()
            if (userRole != null) {
//                val sp = view.context.getSharedPreferences("COURSE_FILE", Context.MODE_PRIVATE)
//                val studentEmail = sp.getString("STUDENT_EMAIL", "")
                val studentEmail = VariableHolder.getInstance().studentEmail
                if(userRole == UserRole.STUDENT)
                    attendanceModelList = firestoreStudentHelper.getAttendance(courseCode!!, Firebase.auth.currentUser?.email!!)
                else
                    attendanceModelList = firestoreStudentHelper.getAttendance(courseCode!!,studentEmail!!)
                if(attendanceModelList.size == 0)
                    emptyView.visibility = LinearLayout.VISIBLE
                recyclerView.adapter = StudentAttendanceListAdapter(attendanceModelList, userRole!!)
            }
        }.invokeOnCompletion {
            progressBar.hide()
            progressBar.setVisibilityAfterHide(View.GONE)
        }

        // resume animation
        view.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                view.viewTreeObserver.removeOnPreDrawListener(this)
                startPostponedEnterTransition()
                return true
            }
        })

        if (userRole == UserRole.TEACHER) {
            viewBinding.fabCheck.visibility = View.GONE
        }



        //check listener
        this.viewBinding.fabCheck.setOnClickListener {

            lifecycleScope.launch {
                val checkAttendanceUtil = CheckAttendanceUtil(requireActivity(), requireContext())
                checkAttendanceUtil.checkAttendance(onSuccessListener = fun() {
                    lifecycleScope.launch {
                        attendanceModelList = firestoreStudentHelper.getAttendance(courseCode!!, Firebase.auth.currentUser?.email!!)
                        recyclerView.adapter = StudentAttendanceListAdapter(attendanceModelList, userRole!!)
                    }
                })
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewBinding = FragmentStudentAttendanceBinding.inflate(inflater, container, false)
        return viewBinding.root
    }
}