package com.componytest.timesheet.auth

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.componytest.timesheet.App
import com.componytest.timesheet.Table.DEPARTMENTS_EMPLOYEES
import com.componytest.timesheet.Table.EMPLOYEES
import com.componytest.timesheet.profile.Employee
import java.lang.Exception

class AuthViewModel(
    private val app: App
): ViewModel() {

    private val employeesLiveData = MutableLiveData<ArrayList<Employee>>()

    fun getEmployees(): LiveData<ArrayList<Employee>> = employeesLiveData

    fun setEmployees(employees: ArrayList<Employee>) {
        employeesLiveData.postValue(employees)
    }


    // TODO: вынести все транзакции с БД в другой поток
    // запись происходит только при первом входе в приложение, т.к. нужно заполнить БД
    // для примера взяты 5 людей: 2 работника, 1 табельщик, 1 администратор отделов, 1 администратор работников
    fun writeEmployeesToDb(employees: ArrayList<Employee>, db: SQLiteDatabase) {
        val contentValues = ContentValues()

        db.beginTransaction()
        try {
            employees.forEach { employee ->
                contentValues.put("firstName", employee.firstName)
                contentValues.put("lastName", employee.lastName)
                contentValues.put("jobPosition", employee.jobPosition)
                db.insert(EMPLOYEES, null, contentValues)
            }

            db.setTransactionSuccessful()
            app.dbRecorderController.markEmployeesWritten()
            setEmployees(employees)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.endTransaction()
            db.close()
        }
    }

    // получение списка работников
    fun readEmployeesFromDb(db: SQLiteDatabase) {
        val employees = arrayListOf<Employee>()
        var cursor: Cursor? = null

        try {
            cursor = db.query(
                EMPLOYEES,
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
                    val firstNamePos = cursor.getColumnIndex("firstName")
                    val lastNamePos = cursor.getColumnIndex("lastName")
                    val jobPositionPos = cursor.getColumnIndex("jobPosition")
                    employees.add(
                        Employee(
                            id = cursor.getInt(idPos),
                            firstName = cursor.getString(firstNamePos),
                            lastName = cursor.getString(lastNamePos),
                            jobPosition = cursor.getString(jobPositionPos)
                        )
                    )
                } while (cursor.moveToNext())
            }

            setEmployees(employees)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
            db.close()
        }
    }

    // получение id департамента, в котором работает человек
    fun getDepartmentId(employeeId: Int, db: SQLiteDatabase): Int {
        var cursor: Cursor? = null
        var departmentId = -1

        try {
            cursor = db.query(
                DEPARTMENTS_EMPLOYEES,
                arrayOf("departmentId"),
                "employeeId = ?",
                arrayOf(employeeId.toString()),
                null,
                null,
                null
            )
            if (cursor.moveToFirst()) {
                val employeeIdPos = cursor.getColumnIndex("employeeId")
                departmentId = cursor.getInt(employeeIdPos)
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