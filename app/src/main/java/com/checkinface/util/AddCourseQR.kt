package com.checkinface.util

import kotlinx.serialization.Serializable

@Serializable
data class AddCourseQR(val courseCode: String, val type: String) {
    companion object {
        const val TYPE_ADD = "add"
    }
}