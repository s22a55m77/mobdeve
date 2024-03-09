package com.checkinface.util

import java.security.MessageDigest
import kotlin.random.Random


class CourseUtil {
    companion object {
        fun generateCode(id: String): String {
            // Hashing the ID using MD5
            val md = MessageDigest.getInstance("MD5")
            md.update(id.toByteArray())
            val byteData = md.digest()

            // Convert the byte array to a positive integer
            val number = byteData.fold(0L) { acc, byteValue -> (acc shl 8) or (byteValue.toLong() and 0xff) }

            // Truncate the number to 6 digits and take the absolute value
            val sixDigitCode = (number % 1000000).toString().takeLast(6).toLong().toString()

            return sixDigitCode
        }

        fun generateColor(): String {
            val colorList = arrayOf("#4f8045", "#1a7f93", "#cb3f77", "#63a290", "#8366b4", "#5E8059", "#3D7D8F", "#CF4777", "#6DA391", "#8D68B3")
            val randomIndex = Random.nextInt(colorList.size)
            return colorList[randomIndex]
        }
    }
}