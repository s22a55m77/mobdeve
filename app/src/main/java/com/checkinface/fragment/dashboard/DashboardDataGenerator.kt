package com.checkinface.fragment.dashboard

import com.checkinface.util.DateUtil

class DashboardDataGenerator {
    companion object {
        fun loadData(): ArrayList<DashboardModel> {
            val data = ArrayList<DashboardModel>()
            data.add(
                DashboardModel(
                    "GEETHIC",
                    "#4f8045",
                    DateUtil.getDate("2024-02-12T07:00:01+08:00"),
                    25
            )
            )
            data.add(
                DashboardModel(
                "ITPLANN",
                    "#1a7f93",
                    DateUtil.getDate("2024-02-12T07:00:01+08:00"),
                    30
            )
            )
            data.add(
                DashboardModel(
                "GERPHIS",
                    "#cb3f77",
                    DateUtil.getDate("2024-02-12T07:00:01+08:00"),
                    40
            )
            )
            data.add(
                DashboardModel(
                "GEMATMW",
                    "#63a290",
                    DateUtil.getDate("2024-02-12T07:00:01+08:00"),
                    33
            )
            )
            data.add(
                DashboardModel(
                "ISDEVOP",
                    "#8366b4",
                    DateUtil.getDate("2024-02-12T07:00:01+08:00"),
                    12
            )
            )
            return data
        }
    }
}