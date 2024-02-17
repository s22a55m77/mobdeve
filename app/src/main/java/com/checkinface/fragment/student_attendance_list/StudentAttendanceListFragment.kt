package com.checkinface.fragment.student_attendance_list

import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andrognito.patternlockview.PatternLockView
import com.andrognito.patternlockview.listener.PatternLockViewListener
import com.andrognito.patternlockview.utils.PatternLockUtils
import com.checkinface.R
import com.checkinface.databinding.FragmentCourseBinding
import com.checkinface.util.UserRole
import com.checkinface.util.UserSharedPreference
import com.google.android.material.dialog.MaterialAlertDialogBuilder


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

        // Pattern Lock
        val patternView = layoutInflater.inflate(R.layout.create_attendance_pattern_lock_layout, null)
        val mPatternLockView: PatternLockView = patternView.findViewById(R.id.pattern_lock_view)
        val mPatternLockViewListener: PatternLockViewListener = object : PatternLockViewListener {
            override fun onStarted() {
                Log.d(javaClass.name, "Pattern drawing started")
            }

            override fun onProgress(progressPattern: List<PatternLockView.Dot>) {
                Log.d(
                    javaClass.name, "Pattern progress: " +
                            PatternLockUtils.patternToString(mPatternLockView, progressPattern)
                )
            }

            override fun onComplete(pattern: List<PatternLockView.Dot>) {
                Log.d(
                    javaClass.name, "Pattern complete: " +
                            PatternLockUtils.patternToString(mPatternLockView, pattern)
                )
            }

            override fun onCleared() {
                Log.d(javaClass.name, "Pattern has been cleared")
            }
        }
        mPatternLockView.addPatternLockListener(mPatternLockViewListener)
        val dialog = MaterialAlertDialogBuilder(this.requireContext()).setView(patternView)
            .setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
            }
            .setPositiveButton("Submit") { dialog, which ->
                dialog.cancel()
            }
        val patternModal = dialog.create()
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