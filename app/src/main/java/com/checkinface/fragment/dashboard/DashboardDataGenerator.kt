package com.checkinface.fragment.dashboard

import java.text.SimpleDateFormat
import java.util.Locale

class DashboardDataGenerator {
    companion object {
        fun loadData(): ArrayList<DashboardModel> {
            val data = ArrayList<DashboardModel>()
            data.add(
                DashboardModel(
                "GEETHIC",
                SimpleDateFormat("24/1/2024", Locale.TAIWAN)
            )
            )
            data.add(
                DashboardModel(
                "ITPLANN",
                SimpleDateFormat("2/1/2024", Locale.TAIWAN)
            )
            )
            data.add(
                DashboardModel(
                "GERPHIS",
                SimpleDateFormat("24/2/2024", Locale.TAIWAN)
            )
            )
            data.add(
                DashboardModel(
                "GEMATMW",
                SimpleDateFormat("4/3/2024", Locale.TAIWAN)
            )
            )
            data.add(
                DashboardModel(
                "ISDEVOP",
                SimpleDateFormat("12/2/2024", Locale.TAIWAN)
            )
            )
            return data
        }
    }
}