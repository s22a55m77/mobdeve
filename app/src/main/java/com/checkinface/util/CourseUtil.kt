package com.checkinface.util

import java.security.MessageDigest
import kotlin.random.Random


class CourseUtil {
    companion object {
        fun generateCode(id: String): String {
            val md = MessageDigest.getInstance("MD5")
            md.update(id.toByteArray())
            val byteData = md.digest()

            // Convert the byte array to a positive integer
            val number = byteData.fold(0L) { acc, byteValue -> (acc shl 8) or (byteValue.toLong() and 0xff) }

            // Ensure the number is positive
            val positiveNumber = number and 0x7FFFFFFF

            // Truncate the number to 6 digits
            val sixDigitCode = String.format("%06d", positiveNumber % 1000000)

            return sixDigitCode
        }

        fun generateColor(): String {
            val colorList = arrayOf("#4f8045", "#1a7f93", "#cb3f77", "#63a290", "#8366b4", "#5E8059", "#3D7D8F", "#CF4777", "#6DA391", "#8D68B3")
            val randomIndex = Random.nextInt(colorList.size)
            return colorList[randomIndex]
        }
    }
}