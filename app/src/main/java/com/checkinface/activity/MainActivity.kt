package com.checkinface.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.checkinface.R
import com.checkinface.databinding.ActivityMainBinding
import com.checkinface.util.UserRole
import com.checkinface.util.UserSharedPreference
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.internal.ViewUtils.hideKeyboard
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var auth: FirebaseAuth

    private var previousDestinationId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth

        // redirect to login page if not login
        if (auth.currentUser == null) {
            val intentToLogin = Intent(this@MainActivity, LoginActivity::class.java)
            intentToLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intentToLogin)
            finish()
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_dashboard, R.id.navigation_user_profile
            )
        )
        setSupportActionBar(findViewById(R.id.tool_bar))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            // control the highlight of the navbar
            navView.menu.findItem(destination.id)?.isChecked = true

            // avoid unnecessary rerender of nav
            if (destination.id == R.id.navigation_teacher_course_student_list &&
                previousDestinationId != R.id.navigation_teacher_course_attendance_list &&
                previousDestinationId != R.id.navigation_teacher_course_student_list &&
                previousDestinationId != R.id.navigation_course
            ) {
                navView.menu.clear()
                navView.inflateMenu(R.menu.bottom_nav_menu_teacher_course)
            }

            if (destination.id == R.id.navigation_dashboard &&
                previousDestinationId != R.id.navigation_user_profile &&
                previousDestinationId != R.id.navigation_dashboard
            ) {
                navView.menu.clear()
                navView.inflateMenu(R.menu.bottom_nav_menu_teacher)
            }

            previousDestinationId = destination.id

        }

        // handle navbar click
        navView.setOnItemSelectedListener {item ->
            when (item.itemId) {
                R.id.navigation_dashboard -> {
                    navController.navigate(R.id.navigation_dashboard)
                    true
                }
                R.id.navigation_user_profile -> {
                    navController.navigate(R.id.navigation_user_profile)
                    true
                }
                R.id.navigation_camera -> {
                    navController.navigate(R.id.navigation_camera)
                    true
                }
                R.id.navigation_teacher_course_student_list -> {
                    navController.navigate(
                        R.id.navigation_teacher_course_student_list,
                        null,
                        NavOptions.Builder()
                            .setPopUpTo(navController.graph.startDestinationId, false)
                            .build())
                    true
                }
                R.id.navigation_teacher_course_attendance_list -> {
                    navController.navigate(
                        R.id.navigation_teacher_course_attendance_list,
                        null,
                        NavOptions.Builder()
                            .setPopUpTo(navController.graph.startDestinationId, false)
                            .build())
                    true
                }
                else -> false
            }
        }

        val userSharedPreference = UserSharedPreference(applicationContext)
        if (userSharedPreference.getRole()?.equals(UserRole.TEACHER) == true) {
            navView.menu.clear()
            navView.inflateMenu(R.menu.bottom_nav_menu_teacher)
        }

    }

    // handle focus for material text field
    @SuppressLint("RestrictedApi")
    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v: View? = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    hideKeyboard(v)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}