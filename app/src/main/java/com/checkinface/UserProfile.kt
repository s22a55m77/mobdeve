package com.checkinface

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.checkinface.databinding.ActivityUserProfileBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.squareup.picasso.Picasso

class UserProfile : AppCompatActivity() {
    private lateinit var viewBinding: ActivityUserProfileBinding
    private val authUser = Firebase.auth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.viewBinding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        this.viewBinding.emailTv.text = authUser?.email
        this.viewBinding.usernameTv.text = authUser?.displayName
        Picasso.get().load(authUser?.photoUrl).into(this.viewBinding.avatarIv)
    }
}