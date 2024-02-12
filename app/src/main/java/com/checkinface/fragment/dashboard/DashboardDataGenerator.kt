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
                    "#4f8045",
                    SimpleDateFormat("24/1/2024", Locale.ENGLISH),
                    25
            )
            )
            data.add(
                DashboardModel(
                "ITPLANN",
                    "#1a7f93",
                SimpleDateFormat("2/1/2024", Locale.TAIWAN),
                    30
            )
            )
            data.add(
                DashboardModel(
                "GERPHIS",
                    "#cb3f77",
                SimpleDateFormat("24/2/2024", Locale.TAIWAN),
                    40
            )
            )
            data.add(
                DashboardModel(
                "GEMATMW",
                    "#63a290",
                SimpleDateFormat("4/3/2024", Locale.TAIWAN),
                    33
            )
            )
            data.add(
                DashboardModel(
                "ISDEVOP",
                    "#8366b4",
                SimpleDateFormat("12/2/2024", Locale.TAIWAN),
                    12
            )
            )
            return data
        }
    }
}