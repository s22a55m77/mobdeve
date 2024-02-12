package com.checkinface.fragment.user_profile

import android.content.Intent
import android.os.Bundle
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.checkinface.LoginActivity
import com.checkinface.R
import com.checkinface.databinding.FragmentUserProfileBinding
import com.checkinface.util.UserSharedPreference
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.squareup.picasso.Picasso

class UserProfileFragment : Fragment() {
    private lateinit var viewBinding: FragmentUserProfileBinding
    private val authUser = Firebase.auth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = TransitionInflater.from(requireContext())
        enterTransition = inflater.inflateTransition(R.transition.slide)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.viewBinding.tvUserEmail.text = authUser?.email
        this.viewBinding.tvUsername.text = authUser?.displayName.takeUnless { it.isNullOrEmpty() } ?: "Not Login"
        if(authUser?.photoUrl != null)
            Picasso.get().load(authUser?.photoUrl).into(this.viewBinding.ivAvatar)

        viewBinding.btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val googleSignInClient = GoogleSignIn.getClient(requireActivity().applicationContext, GoogleSignInOptions.DEFAULT_SIGN_IN)
            googleSignInClient.signOut()

            val userPreference = UserSharedPreference(requireContext())
            userPreference.removeUserData()

            val intentToLogin = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intentToLogin)
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