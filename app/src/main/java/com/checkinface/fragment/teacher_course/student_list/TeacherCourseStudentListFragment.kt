package com.checkinface.fragment.teacher_course.student_list

import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.checkinface.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.BarcodeEncoder

class TeacherCourseStudentListFragment : Fragment() {
    private val studentList: ArrayList<StudentModel> = StudentDataGenerator.loadData()
    private lateinit var recyclerView: RecyclerView
    private lateinit var btnShowQR: ExtendedFloatingActionButton
    private lateinit var ivQrCode: ImageView

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
        return inflater.inflate(R.layout.fragment_teacher_course_student_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.recyclerView = view.findViewById(R.id.rv_student_list)

        val linearLayoutManager = LinearLayoutManager(activity?.applicationContext)
        this.recyclerView.layoutManager = linearLayoutManager

        this.recyclerView.adapter = StudentListAdapter(this.studentList)

        this.btnShowQR = view.findViewById(R.id.btn_show_qr_code)
        // show QR code for the course
        this.btnShowQR.setOnClickListener {
            val qrModalView = layoutInflater.inflate(R.layout.qr_code_layout, null)
            this.ivQrCode = qrModalView.findViewById(R.id.iv_qr_code_container)
            fun generateQR() {
                val text = "Test"
                val writer = MultiFormatWriter()
                try {
                    val matrix: BitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, 400, 400)
                    val encoder = BarcodeEncoder()
                    val bitmap = encoder.createBitmap(matrix)
                    this.ivQrCode.setImageBitmap(bitmap)
                } catch (e: WriterException) {
                    Log.e("QR Code", "Error in creating QR code")
                }
            }
            val qrDialog = MaterialAlertDialogBuilder(this.requireContext()).setView(qrModalView)
            val qrModal = qrDialog.create()
            generateQR()
            qrModal.show()
        }
    }
}