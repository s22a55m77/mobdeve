package com.checkinface.fragment.user_profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.transition.TransitionInflater
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.checkinface.R
import com.checkinface.activity.LoginActivity
import com.checkinface.databinding.FragmentUserProfileBinding
import com.checkinface.util.FirestoreUserHelper
import com.checkinface.util.SettingSharedPreference
import com.checkinface.util.UserRole
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch


class UserProfileFragment : Fragment() {
    private lateinit var viewBinding: FragmentUserProfileBinding
    private val authUser = Firebase.auth.currentUser
    private val firestoreUserHelper = FirestoreUserHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // animation
        val inflater = TransitionInflater.from(requireContext())
        enterTransition = inflater.inflateTransition(R.transition.slide)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.viewBinding.tvUserEmail.text = authUser?.email
        this.viewBinding.tvUsername.text = authUser?.displayName.takeUnless { it.isNullOrEmpty() } ?: "Not Login"
        if(authUser?.photoUrl != null)
            Picasso.get().load(authUser?.photoUrl).into(this.viewBinding.ivAvatar)

        // logout
        viewBinding.btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val googleSignInClient = GoogleSignIn.getClient(requireActivity().applicationContext, GoogleSignInOptions.DEFAULT_SIGN_IN)
            googleSignInClient.signOut()

            val intentToLogin = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intentToLogin)
        }

        lifecycleScope.launch {
            // Set view based on Role
            val firestoreUserHelper = FirestoreUserHelper()
            val role = firestoreUserHelper.getRole()
            if (role == UserRole.TEACHER) {
                viewBinding.cardViewId.visibility = View.GONE
                viewBinding.cardViewNotification.visibility = View.GONE
            }

            // Set student Id
            if(role == UserRole.STUDENT) {
                val student = firestoreUserHelper.getId(Firebase.auth.currentUser?.email.toString())
                Log.d("TEST", student.toString())
                if(student != null) {
                    viewBinding.textInputUserId.setText(student)
                }
            }
        }

        val settingSharedPreference = SettingSharedPreference(requireContext())

        viewBinding.switchUserProfileNotification.isChecked = settingSharedPreference.isNotification()

        viewBinding.switchUserProfileNotification.setOnClickListener {

            val isAllowedNotification = NotificationManagerCompat.from(requireContext()).areNotificationsEnabled()
            if (!isAllowedNotification) {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", context?.packageName!!, null)
                intent.setData(uri);
                startActivity(intent);
                viewBinding.switchUserProfileNotification.isChecked = false
            } else {

                if (viewBinding.switchUserProfileNotification.isChecked) {
                    settingSharedPreference.setNotification(true)
                } else {
                    settingSharedPreference.setNotification(false)
                }

            }

        }

        // Handle edit student id
        viewBinding.textInputUserId.setOnFocusChangeListener { view, isFocus ->
            if (!isFocus) {
                if(viewBinding.textInputUserId.text.toString() != "") {
                    lifecycleScope.launch {
                        firestoreUserHelper.updateId(
                            Firebase.auth.currentUser?.email.toString(),
                            viewBinding.textInputUserId.text.toString(),
                            fun() {
                                Toast.makeText(view.context, "Change Applied", Toast.LENGTH_LONG).show()
                            },
                            fun (e){
                                Toast.makeText(view.context, "Error: ${e.message.toString()}", Toast.LENGTH_LONG).show()
                            })
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        this.viewBinding = FragmentUserProfileBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

}