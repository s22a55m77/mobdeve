package com.checkinface.dashboard

import com.checkinface.model.DashboardModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
import java.util.Locale

class DashboardDataGenerator {
    companion object {
        fun loadData(): ArrayList<DashboardModel> {
            val data = ArrayList<DashboardModel>()
            data.add(DashboardModel(
                "GEETHIC",
                SimpleDateFormat("24/1/2024", Locale.TAIWAN)
            )
            )
            data.add(DashboardModel(
                "ITPLANN",
                SimpleDateFormat("2/1/2024", Locale.TAIWAN)
            ))
            data.add(DashboardModel(
                "GERPHIS",
                SimpleDateFormat("24/2/2024", Locale.TAIWAN)
            ))
            data.add(DashboardModel(
                "GEMATMW",
                SimpleDateFormat("4/3/2024", Locale.TAIWAN)
            ))
            data.add(DashboardModel(
                "ISDEVOP",
                SimpleDateFormat("12/2/2024", Locale.TAIWAN)
            ))
            return data
        }
    }
}