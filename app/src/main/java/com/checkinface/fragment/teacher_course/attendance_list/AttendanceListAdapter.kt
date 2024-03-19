package com.checkinface.fragment.teacher_course.attendance_list

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.checkinface.R
import com.checkinface.activity.edit_attendance.EditAttendanceActivity
import com.checkinface.databinding.AttendanceItemLayoutBinding
import com.checkinface.util.DateUtil
import com.checkinface.util.qr.CheckAttendanceQR
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

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

        // details
        holder.itemView.setOnClickListener {
            val navController = holder.itemView.findNavController()
            navController.navigate(R.id.action_attendance_list_to_detail)
            // Set Event
            val sp = holder.itemView.context.getSharedPreferences("COURSE_FILE", Context.MODE_PRIVATE)
            with(sp.edit()) {
                putString("EVENT_TIME", DateUtil.getFormattedDate("yyyy-MM-dd HH:mm:ss", data[position].date))
                apply()
            }
        }

        val settings = holder.itemView.findViewById<ImageView>(R.id.iv_attendance_settings)
        settings.setOnClickListener {
            val sp = holder.itemView.context.getSharedPreferences("COURSE_FILE", Context.MODE_PRIVATE)
            with(sp.edit()) {
                putString("EVENT_TIME", DateUtil.getFormattedDate("yyyy-MM-dd HH:mm:ss", data[position].date))
                apply()
            }
            val intent = Intent(holder.itemView.context, EditAttendanceActivity::class.java)
            holder.itemView.context.startActivity(intent)
        }

        val qrcode = holder.itemView.findViewById<ImageView>(R.id.iv_attendance_item_qr_code)
        qrcode.setOnClickListener {
            val layoutInflater = LayoutInflater.from(holder.itemView.context)
            val qrModalView = layoutInflater.inflate(R.layout.qr_code_layout, null)
            this.ivQrCode = qrModalView.findViewById(R.id.iv_qr_code_container)
            val sp = holder.itemView.context.getSharedPreferences("COURSE_FILE", Context.MODE_PRIVATE)
            val courseCode = sp.getString("COURSE_CODE", "")
            val eventId = data[position].eventId
            val qr = CheckAttendanceQR(eventId, courseCode!!)
            val jsonString = Json.encodeToString(qr)
            fun generateQR() {
                val text = jsonString
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