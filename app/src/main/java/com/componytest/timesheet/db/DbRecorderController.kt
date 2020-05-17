package com.componytest.timesheet.db

import android.content.SharedPreferences

class DbRecorderController(
    private val sharedPreferences: SharedPreferences
) {

    fun markCalendarWritten() {
        sharedPreferences.edit().putBoolean(CALENDAR_RECORD, true).apply()
    }

    fun isCalendarWritten() = sharedPreferences.getBoolean(CALENDAR_RECORD, false)

    fun markEmployeesWritten() {
        sharedPreferences.edit().putBoolean(EMPLOYEES_RECORD, true).apply()
    }

    fun areEmployeesWritten() = sharedPreferences.getBoolean(EMPLOYEES_RECORD, false)



    companion object {
        const val CALENDAR_RECORD = "CALENDAR_RECORD"
        const val EMPLOYEES_RECORD = "EMPLOYEES_RECORD"
    }
}