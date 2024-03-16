package com.checkinface.util.qr

import kotlinx.serialization.Serializable

@Serializable
sealed class CommonQR(val action: String) {
    companion object {
        const val ACTION_ADD = "add"
        const val ACTION_CHECK = "check"
    }
}