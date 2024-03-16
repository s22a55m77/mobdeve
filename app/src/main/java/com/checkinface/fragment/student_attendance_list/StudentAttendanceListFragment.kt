package com.checkinface.fragment.student_attendance_list

import android.content.Context
import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andrognito.patternlockview.PatternLockView
import com.andrognito.patternlockview.listener.PatternLockViewListener
import com.andrognito.patternlockview.utils.PatternLockUtils
import com.checkinface.R
import com.checkinface.databinding.FragmentStudentAttendanceBinding
import com.checkinface.util.FirestoreStudentHelper
import com.checkinface.util.FirestoreUserHelper
import com.checkinface.util.UserRole
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import kotlinx.coroutines.launch


class StudentAttendanceListFragment : Fragment() {

    private var attendanceModelList: ArrayList<StudentAttendanceModel> = arrayListOf()
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewBinding: FragmentStudentAttendanceBinding
    private val firestoreUserHelper: FirestoreUserHelper = FirestoreUserHelper()
    private val firestoreStudentHelper: FirestoreStudentHelper = FirestoreStudentHelper()
    private var userRole: UserRole? = null

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

        val sp = view.context.getSharedPreferences("COURSE_FILE", Context.MODE_PRIVATE)
        val courseCode = sp.getString("COURSE_CODE", "")

        lifecycleScope.launch {
            userRole = firestoreUserHelper.getRole()
            if (userRole != null) {
                val sp = view.context.getSharedPreferences("COURSE_FILE", Context.MODE_PRIVATE)
                val studentEmail = sp.getString("STUDENT_EMAIL", "")
                if(userRole == UserRole.STUDENT)
                    attendanceModelList = firestoreStudentHelper.getAttendance(courseCode!!, Firebase.auth.currentUser?.email!!)
                else
                    attendanceModelList = firestoreStudentHelper.getAttendance(courseCode!!,studentEmail!!)
                recyclerView.adapter = StudentAttendanceListAdapter(attendanceModelList, userRole!!)
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

        if (userRole == UserRole.TEACHER) {
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
        //modal for pattern lock
        val dialog = MaterialAlertDialogBuilder(this.requireContext()).setView(patternView)
            .setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
            }
            .setPositiveButton("Submit") { dialog, which ->
                Toast.makeText(
                    requireContext(),
                    "Attendance Checked",
                    Toast.LENGTH_LONG
                ).show()
                dialog.cancel()
            }
        val patternModal = dialog.create()

        //check listener
        this.viewBinding.fabCheck.setOnClickListener {
            val options = GmsBarcodeScannerOptions.Builder()
                .setBarcodeFormats(
                    Barcode.FORMAT_QR_CODE)
                .enableAutoZoom()
                .build()

            val scanner = GmsBarcodeScanning.getClient(requireActivity().applicationContext, options)

            scanner.startScan()
                .addOnSuccessListener { barcode ->
                    barcode.rawValue?.let {
                        patternModal.show()
                        Log.d("CheckAttendance", it)
                    }
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