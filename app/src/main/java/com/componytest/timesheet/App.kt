package com.componytest.timesheet

import android.app.Application
import android.content.Context
import com.componytest.timesheet.db.DbRecorderController
import com.componytest.timesheet.profile.Employee

class App: Application() {

    val viewModelFactory by lazy { ViewModelFactory(this) }
    // для проверки записи данных в бд при первом входе (т.к. данные хранятся локально, их нужно записать)
    lateinit var dbRecorderController: DbRecorderController
    val employees = arrayListOf<Employee>()

    override fun onCreate() {
        super.onCreate()
        dbRecorderController = DbRecorderController(
            getSharedPreferences("SharedPrefs", Context.MODE_PRIVATE)
        )
    }
}