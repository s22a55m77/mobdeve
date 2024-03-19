package com.checkinface.util

class VariableHolder {
    companion object {
        @Volatile
        private var instance: VariableHolder? = null

        fun getInstance(): VariableHolder {
            return instance ?: synchronized(this) {
                instance ?: VariableHolder().also { instance = it }
            }
        }
    }

    var courseCode: String? = null
    var studentEmail: String? = null
    var eventTime: String? = null
}