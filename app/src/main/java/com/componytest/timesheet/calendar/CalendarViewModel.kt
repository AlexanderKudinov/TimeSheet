package com.componytest.timesheet.calendar

import android.database.sqlite.SQLiteDatabase
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.componytest.timesheet.Table.CALENDAR
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class CalendarViewModel(

): ViewModel() {

    private val calendarLiveData = MutableLiveData<HashMap<Date, String>>()

    fun getCalendarDays(): LiveData<HashMap<Date, String>> = calendarLiveData

    fun setCalendarDays(days: HashMap<Date, String>) {
        calendarLiveData.postValue(days)
    }


    fun readCalendarFromDb(db: SQLiteDatabase) {
        val days = HashMap<Date, String>()
        val cursor = db.query(
            CALENDAR,
            null,
            null,
            null,
            null,
            null,
            null
        )

        try {
            if (cursor.moveToFirst()) {
                do {
                    var datePos = cursor.getColumnIndex("date")
                    var typePos = cursor.getColumnIndex("dayType")
                    try {
                        days[SimpleDateFormat("dd-MM-yyyy").parse(cursor.getString(datePos))] =
                            cursor.getString(typePos)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } while (cursor.moveToNext())

            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.close()
        }

        cursor.close()
        setCalendarDays(days)
    }
}