package com.checkinface.fragment.teacher_course.attendance_list

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.checkinface.R
import com.checkinface.databinding.AttendanceItemLayoutBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.BarcodeEncoder

class AttendanceListAdapter(private val data: ArrayList<TeacherAttendanceModel>): Adapter<AttendanceListViewHolder>() {
    private lateinit var ivQrCode: ImageView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendanceListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AttendanceItemLayoutBinding.inflate(inflater, parent, false)

        return AttendanceListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: AttendanceListViewHolder, position: Int) {
        holder.bindData(data[position])

        val details = holder.itemView.findViewById<ImageView>(R.id.iv_attendance_detail)
        details.setOnClickListener {
            val navController = holder.itemView.findNavController()
            navController.navigate(R.id.action_attendance_list_to_detail)
        }

        val qrcode = holder.itemView.findViewById<ImageView>(R.id.iv_attendance_item_qr_code)
        qrcode.setOnClickListener {
            val layoutInflater = LayoutInflater.from(holder.itemView.context)
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
            val qrDialog = MaterialAlertDialogBuilder(holder.itemView.context).setView(qrModalView)
            val qrModal = qrDialog.create()
            generateQR()
            qrModal.show()
        }
    }
}