package com.checkinface.dashboard

import com.checkinface.model.DashboardModel

class DashboardDataGenerator {
    companion object {
        fun loadData(): ArrayList<DashboardModel> {
            val data = ArrayList<DashboardModel>()
            data.add(DashboardModel(
                "GEETHIC",
                3,
                2
            )
            )
            data.add(DashboardModel(
                "ITPLANN",
                5,
                0
            ))
            data.add(DashboardModel(
                "GERPHIS",
                0,
                2
            ))
            data.add(DashboardModel(
                "GEMATMW",
                1,
                3
            ))
            data.add(DashboardModel(
                "ISDEVOP",
                6,
                10
            ))
            return data
        }
    }
}