package com.checkinface.fragment.user_profile

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.checkinface.R
import com.checkinface.activity.LoginActivity
import com.checkinface.databinding.FragmentUserProfileBinding
import com.checkinface.util.FirestoreUserHelper
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // animation
        val inflater = TransitionInflater.from(requireContext())
        enterTransition = inflater.inflateTransition(R.transition.slide)
    }

    val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // Permission is granted. Continue the action or workflow in your
                // app.
            } else {
                // Explain to the user that the feature is unavailable because the
                // feature requires a permission that the user has denied. At the
                // same time, respect the user's decision. Don't link to system
                // settings in an effort to convince the user to change their
                // decision.
            }
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
            val firestoreUserHelper = FirestoreUserHelper()
            val role = firestoreUserHelper.getRole()
            if (role == UserRole.TEACHER) {
                viewBinding.cardViewId.visibility = View.GONE
                viewBinding.cardViewNotification.visibility = View.GONE
            }
        }

        viewBinding.switchUserProfileNotification.setOnClickListener {

            val isAllowedNotification = NotificationManagerCompat.from(requireContext()).areNotificationsEnabled()
            if (!isAllowedNotification) {
                requestPermissionLauncher.launch(
                    Manifest.permission.ACCESS_NOTIFICATION_POLICY)
//                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_NOTIFICATION_POLICY), 123)

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