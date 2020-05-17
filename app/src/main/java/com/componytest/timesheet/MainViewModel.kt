package com.componytest.timesheet

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.componytest.timesheet.Table.CALENDAR
import com.componytest.timesheet.Table.JOB_MARKS
import com.componytest.timesheet.calendar.DayType
import com.componytest.timesheet.departments.Department
import com.componytest.timesheet.departments.Department_Employee
import java.lang.Exception

class MainViewModel(
    private val app: App
): ViewModel() {


    private val departmentsLiveData = MutableLiveData<ArrayList<Department>>()

    fun getDepartments(): LiveData<ArrayList<Department>> = departmentsLiveData

    fun setDepartments(departments: ArrayList<Department>) {
        departmentsLiveData.value = departments
    }



    fun readDepartmentsFromDb(db: SQLiteDatabase) {
        val departments = arrayListOf<Department>()
        var cursor: Cursor? = null

        try {
            cursor = db.query(
                Table.DEPARTMENTS,
                null,
                null,
                null,
                null,
                null,
                null
            )

            if (cursor.moveToFirst()) {
                do {
                    val idPos = cursor.getColumnIndex("id")
                    val nameId = cursor.getColumnIndex("name")
                    departments.add(
                        Department(
                            id = cursor.getInt(idPos),
                            name = cursor.getString(nameId)
                        )
                    )
                } while (cursor.moveToNext())

                setDepartments(departments)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
            db.close()
        }
    }

    // запись происходит только при первом входе в приложение, т.к. нужно заполнить БД
    // для примера взят май
    // TODO: ненужный код, нужен для примера
    fun writeCalendarToDb(db: SQLiteDatabase) {
        val contentValues = ContentValues()

        db.beginTransaction()
        try {
            contentValues.put("date", "01-05-2020")
            contentValues.put("dayType", DayType.HOLIDAY)
            db.insert(CALENDAR, null, contentValues)

            contentValues.put("date", "02-05-2020")
            contentValues.put("dayType", DayType.HOLIDAY)
            db.insert(CALENDAR, null, contentValues)

            contentValues.put("date", "03-05-2020")
            contentValues.put("dayType", DayType.REST_DAY)
            db.insert(CALENDAR, null, contentValues)

            contentValues.put("date", "04-05-2020")
            contentValues.put("dayType", DayType.REST_DAY)
            db.insert(CALENDAR, null, contentValues)

            contentValues.put("date", "05-05-2020")
            contentValues.put("dayType", DayType.WORKING_DAY)
            db.insert(CALENDAR, null, contentValues)

            contentValues.put("date", "06-05-2020")
            contentValues.put("dayType", DayType.WORKING_DAY)
            db.insert(CALENDAR, null, contentValues)

            contentValues.put("date", "07-05-2020")
            contentValues.put("dayType", DayType.WORKING_DAY)
            db.insert(CALENDAR, null, contentValues)

            contentValues.put("date", "08-05-2020")
            contentValues.put("dayType", DayType.PRE_HOLIDAY)
            db.insert(CALENDAR, null, contentValues)

            contentValues.put("date", "09-05-2020")
            contentValues.put("dayType", DayType.HOLIDAY)
            db.insert(CALENDAR, null, contentValues)

            contentValues.put("date", "10-05-2020")
            contentValues.put("dayType", DayType.REST_DAY)
            db.insert(CALENDAR, null, contentValues)

            contentValues.put("date", "11-05-2020")
            contentValues.put("dayType", DayType.REST_DAY)
            db.insert(CALENDAR, null, contentValues)

            contentValues.put("date", "12-05-2020")
            contentValues.put("dayType", DayType.WORKING_DAY)
            db.insert(CALENDAR, null, contentValues)

            contentValues.put("date", "13-05-2020")
            contentValues.put("dayType", DayType.WORKING_DAY)
            db.insert(CALENDAR, null, contentValues)

            contentValues.put("date", "14-05-2020")
            contentValues.put("dayType", DayType.WORKING_DAY)
            db.insert(CALENDAR, null, contentValues)

            contentValues.put("date", "15-05-2020")
            contentValues.put("dayType", DayType.WORKING_DAY)
            db.insert(CALENDAR, null, contentValues)

            contentValues.put("date", "16-05-2020")
            contentValues.put("dayType", DayType.REST_DAY)
            db.insert(CALENDAR, null, contentValues)

            contentValues.put("date", "17-05-2020")
            contentValues.put("dayType", DayType.REST_DAY)
            db.insert(CALENDAR, null, contentValues)

            contentValues.put("date", "18-05-2020")
            contentValues.put("dayType", DayType.WORKING_DAY)
            db.insert(CALENDAR, null, contentValues)

            contentValues.put("date", "19-05-2020")
            contentValues.put("dayType", DayType.WORKING_DAY)
            db.insert(CALENDAR, null, contentValues)

            contentValues.put("date", "20-05-2020")
            contentValues.put("dayType", DayType.WORKING_DAY)
            db.insert(CALENDAR, null, contentValues)

            contentValues.put("date", "21-05-2020")
            contentValues.put("dayType", DayType.WORKING_DAY)
            db.insert(CALENDAR, null, contentValues)

            contentValues.put("date", "22-05-2020")
            contentValues.put("dayType", DayType.WORKING_DAY)
            db.insert(CALENDAR, null, contentValues)

            contentValues.put("date", "23-05-2020")
            contentValues.put("dayType", DayType.REST_DAY)
            db.insert(CALENDAR, null, contentValues)

            contentValues.put("date", "24-05-2020")
            contentValues.put("dayType", DayType.REST_DAY)
            db.insert(CALENDAR, null, contentValues)

            contentValues.put("date", "25-05-2020")
            contentValues.put("dayType", DayType.WORKING_DAY)
            db.insert(CALENDAR, null, contentValues)

            contentValues.put("date", "26-05-2020")
            contentValues.put("dayType", DayType.WORKING_DAY)
            db.insert(CALENDAR, null, contentValues)

            contentValues.put("date", "27-05-2020")
            contentValues.put("dayType", DayType.WORKING_DAY)
            db.insert(CALENDAR, null, contentValues)

            contentValues.put("date", "28-05-2020")
            contentValues.put("dayType", DayType.WORKING_DAY)
            db.insert(CALENDAR, null, contentValues)

            contentValues.put("date", "29-05-2020")
            contentValues.put("dayType", DayType.WORKING_DAY)
            db.insert(CALENDAR, null, contentValues)

            contentValues.put("date", "30-05-2020")
            contentValues.put("dayType", DayType.REST_DAY)
            db.insert(CALENDAR, null, contentValues)

            contentValues.put("date", "31-05-2020")
            contentValues.put("dayType", DayType.REST_DAY)
            db.insert(CALENDAR, null, contentValues)

            db.setTransactionSuccessful()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.endTransaction()
            db.close()
        }

        app.dbRecorderController.markCalendarWritten()  // отмечаем, что больше не надо записывать в БД
    }


    fun readDepartmentForUser(userId: Int?, db: SQLiteDatabase): Int {
        var cursor: Cursor? = null
        var departmentId = -1

        try {
            cursor = db.query(
                Table.DEPARTMENTS_EMPLOYEES,
                arrayOf("departmentId"),
                "employeeId = ?",
                arrayOf(userId.toString()),
                null,
                null,
                null
            )

            if (cursor.moveToFirst()) {
                val departmentIdPos = cursor.getColumnIndex("departmentId")
                departmentId = cursor.getInt(departmentIdPos)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
            db.close()
        }

        return departmentId
    }
}