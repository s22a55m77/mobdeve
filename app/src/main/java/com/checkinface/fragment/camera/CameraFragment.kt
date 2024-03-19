package com.checkinface.fragment.camera

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.checkinface.R
import com.checkinface.util.CheckAttendanceUtil
import com.checkinface.util.FirestoreCourseHelper
import com.checkinface.util.qr.AddCourseQR
import com.checkinface.util.qr.CheckAttendanceQR
import com.checkinface.util.qr.QrSerializer
import com.google.android.gms.common.moduleinstall.ModuleInstall
import com.google.android.gms.common.moduleinstall.ModuleInstallRequest
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class CameraFragment : Fragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val optional = GmsBarcodeScanning.getClient(requireContext())

        val client = ModuleInstall.getClient(requireContext())
        client.areModulesAvailable(optional)
            .addOnSuccessListener { result ->
                if (result.areModulesAvailable()) {
                    // start scanner if the scanner module is already installed
                    startScan()
                } else {
                    // install first before starting the scanner
                    val installRequest = ModuleInstallRequest.newBuilder()
                        .addApi(optional)
                        .build()
                    client.installModules(installRequest)
                        .addOnFailureListener { exception ->
                            Log.d("FAIL", exception.stackTraceToString())
                        }
                        .addOnSuccessListener {
                            startScan()
                        }
                }
            }


    }

    fun startScan() {
        val options = GmsBarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_QR_CODE)
            .enableAutoZoom()
            .build()

        val scanner = GmsBarcodeScanning.getClient(requireContext(), options)
        scanner.startScan()
            .addOnFailureListener { exception ->
                Log.d("FAIL", exception.stackTraceToString())
            }
            .addOnSuccessListener { barcode ->
                barcode.rawValue?.let {

                    val code = Json.decodeFromString(QrSerializer(), it)

                    when (code) {
                        is AddCourseQR -> {
                            val firestoreCourseHelper = FirestoreCourseHelper()
                            lifecycleScope.launch {
                                firestoreCourseHelper.addStudent(code.courseCode)
                                val navController = findNavController()
                                navController.navigate(R.id.navigation_dashboard)
                            }
                        }
                        is CheckAttendanceQR -> {
                            val checkAttendanceUtil = CheckAttendanceUtil(requireActivity(), requireContext())
                            lifecycleScope.launch {
                                checkAttendanceUtil.checkAttendance(code.courseCode, code.eventId)
                            }
                        }
                        else -> {

                        }
                    }

                }
            }
    }

    override fun onResume() {
        super.onResume()
        val navController = findNavController()
        navController.navigate(R.id.navigation_dashboard)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_camera, container, false)
    }
}