package com.checkinface.fragment.user_profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.checkinface.databinding.FragmentUserProfileBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.squareup.picasso.Picasso

class UserProfileFragment : Fragment() {
    private lateinit var viewBinding: FragmentUserProfileBinding
    private val authUser = Firebase.auth.currentUser

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.viewBinding.tvUserEmail.text = authUser?.email.takeUnless { it.isNullOrEmpty() } ?: "no user"
        this.viewBinding.tvUsername.text = authUser?.displayName
        Picasso.get().load(authUser?.photoUrl).into(this.viewBinding.ivAvatar)
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