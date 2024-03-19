package com.checkinface.util.qr

import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject

class QrSerializer: JsonContentPolymorphicSerializer<CommonQR>(CommonQR::class) {
    override fun selectDeserializer(element: JsonElement) = when {
        "eventId" in element.jsonObject -> CheckAttendanceQR.serializer()
        else -> AddCourseQR.serializer()
    }
}