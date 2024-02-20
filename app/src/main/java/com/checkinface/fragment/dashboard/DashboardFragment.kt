package com.checkinface.fragment.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.checkinface.R
import com.checkinface.util.UserRole
import com.checkinface.util.UserSharedPreference
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.BarcodeEncoder


class DashboardFragment : Fragment() {
    private val dashboardModelList = DashboardDataGenerator.loadData()
    private lateinit var recyclerView: RecyclerView
    private lateinit var btnAddCourse: ExtendedFloatingActionButton
    private lateinit var ivQrCode: ImageView
    private lateinit var edAddCourse: EditText

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.recyclerView = view.findViewById(R.id.dashboardRv)

        val gridLayoutManager = GridLayoutManager(activity?.applicationContext, 2)
        this.recyclerView.layoutManager = gridLayoutManager

        this.recyclerView.adapter = DashboardAdapter(this.dashboardModelList)

        // Course QR Code Dialog
        val qrModalView = layoutInflater.inflate(R.layout.qr_code_layout, null)
        this.ivQrCode = qrModalView.findViewById(R.id.iv_qr_code_container)
        fun generateQR() {
            val text = "Test"
            val writer = MultiFormatWriter()
            try {
                val matrix: BitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, 400, 400)
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
        this.edAddCourse = modalView.findViewById(R.id.ed_add_course_name)

        val user = UserSharedPreference(requireContext())
        if (user.getRole() === UserRole.STUDENT) {
            this.edAddCourse.hint = "Course Code"
        }

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
                    generateQR()
                    qrModal.show()
                }
            }
        val teacherCourseModal = teacherDialogBuilder.create()

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
                    dialog.cancel()
                }
            }

        val studentCourseModal = studentDialogBuilder.create()

        // Button Click
        btnAddCourse.setOnClickListener {
            if (user.getRole() == UserRole.TEACHER)
                teacherCourseModal.show()
            else if (user.getRole() == UserRole.STUDENT)
                studentCourseModal.show()
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