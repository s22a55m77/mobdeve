package com.checkinface.util

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.IBinder
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.Priority
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.tasks.Task
import java.util.concurrent.Executor

class GeolocationService(private val activity: Activity): Service() {
    private val locationManager: LocationManager = activity.applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    override fun onBind(intent: Intent?): IBinder? {
        throw UnsupportedOperationException("Binding not supported")
    }

    private fun isCoarseLocationGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            activity.applicationContext,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun isFineLocationGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            activity.applicationContext,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun getPermissionAndGPS(): Boolean {
        if (!checkPermission()) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
            return false
        }

        if (!isGPSEnabled()) {
            val callGPSSettingIntent = Intent(
                Settings.ACTION_LOCATION_SOURCE_SETTINGS
            )
            activity.startActivity(callGPSSettingIntent)
            return false
        }
        return true
    }

    fun checkPermission(): Boolean {
        return isCoarseLocationGranted() && isFineLocationGranted()
    }

    fun isGPSEnabled(): Boolean {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    @SuppressLint("MissingPermission")
    private fun getGPSLocation(): String? {
        val locationListener = object: LocationListener {
            override fun onLocationChanged(location: Location) {
//                Log.d("LOCATION", location.toString())
//                Toast.makeText(activity.applicationContext, location.longitude.toString(), Toast.LENGTH_LONG).show()
            }
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000L,1f, locationListener);
        val location: Location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            ?: return null
        val lon = location.longitude
        val lat = location.latitude

        return "$lon $lat"
    }

    @SuppressLint("MissingPermission")
    private fun getNetworkLocation(): String? {
        val locationListener = object: LocationListener {
            override fun onLocationChanged(location: Location) {
//                Log.d("LOCATION", location.toString())
//                Toast.makeText(activity.applicationContext, location.longitude.toString(), Toast.LENGTH_LONG).show()
            }
        }

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1000L,1f, locationListener);
        val location: Location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            ?: return null
        val lon = location.longitude
        val lat = location.latitude

        return "$lon $lat"
    }

    @SuppressLint("MissingPermission")
    fun getCurrentLonLat(): String? {
        val gps = getGPSLocation()
        val network = getNetworkLocation()

        return gps ?: network
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000L,1f, locationListener);
//        val location: Location? = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
//        val lon = location?.longitude
//        val lat = location?.latitude
//        Log.d("LON", location?.longitude.toString())
//        return "$lon $lat"
    }

    fun areLocationsEqual(location1: String, location2: String, toleranceMeters: Double = 100.0): Boolean {
        val (lon1, lat1) = location1.split(" ").map { it.toDouble() }
        val (lon2, lat2) = location2.split(" ").map { it.toDouble() }

        val location1 = Location("")
        location1.latitude = lat1
        location1.longitude = lon1

        val location2 = Location("")
        location2.latitude = lat2
        location2.longitude = lon2

        val meter = location1.distanceTo(location2)
        Log.d("Meter", meter.toString())
        return meter <= 100
    }
}