package com.checkinface.fragment.teacher_course.student_list

import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.checkinface.R
import com.checkinface.util.qr.AddCourseQR
import com.checkinface.util.FirestoreCourseHelper
import com.checkinface.util.VariableHolder
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class TeacherCourseStudentListFragment : Fragment() {
    private val firestoreCourseHelper = FirestoreCourseHelper()
    private var studentList: ArrayList<StudentModel> = arrayListOf()
    private lateinit var recyclerView: RecyclerView
    private lateinit var btnShowQR: ExtendedFloatingActionButton
    private lateinit var ivQrCode: ImageView
    private lateinit var tvQrCode: TextView
    private lateinit var emptyView: LinearLayout
    private lateinit var progressBar: CircularProgressIndicator

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

        this.emptyView = view.findViewById(R.id.empty_view)
        this.progressBar = view.findViewById(R.id.progress_circular)

//        val sp = view.context.getSharedPreferences("COURSE_FILE", Context.MODE_PRIVATE)
//        val courseCode = sp.getString("COURSE_CODE", "")
        val courseCode = VariableHolder.getInstance().courseCode
        if(courseCode != "" && courseCode != null)
            lifecycleScope.launch {
                studentList = firestoreCourseHelper.getStudentLists(courseCode)
                if(studentList.size == 0)
                    emptyView.visibility = LinearLayout.VISIBLE
                recyclerView.adapter = StudentListAdapter(studentList)
            }.invokeOnCompletion {
                progressBar.hide()
                progressBar.setVisibilityAfterHide(View.GONE)
            }



        this.btnShowQR = view.findViewById(R.id.btn_show_qr_code)
        // show QR code for the course
        this.btnShowQR.setOnClickListener {
            val qrModalView = layoutInflater.inflate(R.layout.qr_code_layout, null)
            this.ivQrCode = qrModalView.findViewById(R.id.iv_qr_code_container)
            fun generateQR(courseCode: String) {
                val qr = AddCourseQR(courseCode)
                val jsonString = Json.encodeToString(qr)
                val writer = MultiFormatWriter()
                try {
                    val matrix: BitMatrix = writer.encode(jsonString, BarcodeFormat.QR_CODE, 400, 400)
                    val encoder = BarcodeEncoder()
                    val bitmap = encoder.createBitmap(matrix)
                    this.ivQrCode.setImageBitmap(bitmap)
                } catch (e: WriterException) {
                    Log.e("QR Code", "Error in creating QR code")
                }
            }
            val qrDialog = MaterialAlertDialogBuilder(this.requireContext()).setView(qrModalView)
            val qrModal = qrDialog.create()
//            val sp = view.rootView.context.getSharedPreferences("COURSE_FILE", Context.MODE_PRIVATE)
//            val courseCode = sp.getString("COURSE_CODE", "")
            val courseCode = VariableHolder.getInstance().courseCode
            if(courseCode != "" || courseCode.isNotEmpty()) {
                generateQR(courseCode!!)
                this.tvQrCode = qrModalView.findViewById(R.id.tv_create_course_code)
                tvQrCode.text = courseCode
                tvQrCode.visibility = TextView.VISIBLE
                qrModal.show()
            } else {
                Toast.makeText(view.context, "Error while generating QR Code", Toast.LENGTH_LONG).show()
            }

        }
    }
}