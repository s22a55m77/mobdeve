package com.checkinface.fragment.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.checkinface.R
import com.checkinface.util.CourseUtil
import com.checkinface.util.FirestoreUserHelper
import com.checkinface.util.FirestoreCourseHelper
import com.checkinface.util.UserRole
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.coroutines.launch


class DashboardFragment : Fragment() {
    private var dashboardModelList: ArrayList<DashboardModel> = arrayListOf()
    private lateinit var recyclerView: RecyclerView
    private lateinit var btnAddCourse: ExtendedFloatingActionButton
    private lateinit var ivQrCode: ImageView
    private lateinit var edAddCourse: EditText
    private lateinit var tvCreateCourseCode: TextView
    private val firestoreUserHelper: FirestoreUserHelper = FirestoreUserHelper()
    private val firestoreCourseHelper: FirestoreCourseHelper = FirestoreCourseHelper()
    private var userRole: UserRole? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.recyclerView = view.findViewById(R.id.dashboardRv)

        val gridLayoutManager = GridLayoutManager(activity?.applicationContext, 2)
        this.recyclerView.layoutManager = gridLayoutManager

        lifecycleScope.launch {
            userRole = firestoreUserHelper.getRole()
            // this prevent app from crashing when the user is not yet log in
            if (userRole != null) {
                dashboardModelList = firestoreCourseHelper.getCourses(Firebase.auth.currentUser?.email.toString(), userRole!!)
                recyclerView.adapter = DashboardAdapter(dashboardModelList, userRole!!)
            }

        }

        // Course QR Code Dialog
        val qrModalView = layoutInflater.inflate(R.layout.qr_code_layout, null)
        this.ivQrCode = qrModalView.findViewById(R.id.iv_qr_code_container)
        fun generateQR(code: String) {
            val writer = MultiFormatWriter()
            try {
                val matrix: BitMatrix = writer.encode(code, BarcodeFormat.QR_CODE, 400, 400)
                val encoder = BarcodeEncoder()
                val bitmap = encoder.createBitmap(matrix)
                ivQrCode.setImageBitmap(bitmap)
            } catch (e: WriterException) {
                Log.e("QR Code", "Error in creating QR code")
            }
        }
        val qrDialog = MaterialAlertDialogBuilder(this.requireContext()).setView(qrModalView)
        val qrModal = qrDialog.create()

        // Add Course Dialog
        val modalView = layoutInflater.inflate(R.layout.create_course, null)

        this.btnAddCourse = view.findViewById(R.id.btn_teacher_add_course)
        this.tvCreateCourseCode = qrModalView.findViewById(R.id.tv_create_course_code)
        this.edAddCourse = modalView.findViewById(R.id.ed_add_course_name)

        if (userRole === UserRole.STUDENT) {
            this.edAddCourse.hint = "Course Code"
        }

        // Teacher Dialog for Add Course
        val teacherDialogBuilder = MaterialAlertDialogBuilder(this.requireContext())
            .setView(modalView)
            .setTitle("Add Course")
            .setNegativeButton("Cancel") { dialog, which ->
                dialog.cancel()
            }
            .setPositiveButton("Next") { dialog, which ->
                if(edAddCourse.text.isNullOrEmpty()) {
                    Toast.makeText(this.context, "Please enter the course name", Toast.LENGTH_LONG).show()
                }
                else {
                    dialog.cancel()
                    firestoreCourseHelper.addCourse(edAddCourse.text.toString(), Firebase.auth.currentUser?.email.toString(), fun(courseCode) {
                        generateQR(courseCode)
                        tvCreateCourseCode.text = courseCode
                        qrModal.show()
                        lifecycleScope.launch {
                            dashboardModelList = firestoreCourseHelper.getCourses(Firebase.auth.currentUser?.email!!, userRole!!)
                            recyclerView.adapter = DashboardAdapter(dashboardModelList, userRole!!)
                        }
                    })
                }
            }
        val teacherCourseModal = teacherDialogBuilder.create()

        // Student Dialog For Add Course
        val studentDialogBuilder = MaterialAlertDialogBuilder(this.requireContext())
            .setView(modalView)
            .setTitle("Add Course")
            .setNegativeButton("Cancel") { dialog, which ->
                dialog.cancel()
            }
            .setPositiveButton("Submit") { dialog, which ->
                if(edAddCourse.text.isNullOrEmpty()) {
                    Toast.makeText(this.context, "Please enter the course code", Toast.LENGTH_LONG).show()
                }
                else {
                    firestoreCourseHelper.addStudent(edAddCourse.text.toString(),
                        fun() {
                            Toast.makeText(this.context, "Successfully Added to the Course", Toast.LENGTH_LONG).show()
                        },
                        fun() {
                            Toast.makeText(this.context, "Error While Adding to the Course", Toast.LENGTH_LONG).show()
                    })
                }
            }

        val studentCourseModal = studentDialogBuilder.create()

        // Button Click
        btnAddCourse.setOnClickListener {
            if (userRole == UserRole.TEACHER)
                teacherCourseModal.show()
            else if (userRole == UserRole.STUDENT) {
                edAddCourse.hint = "Course Code"
                studentCourseModal.show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }
}