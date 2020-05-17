package com.componytest.timesheet.departments

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.componytest.timesheet.Table.DEPARTMENTS_EMPLOYEES
import java.lang.Exception
import kotlin.collections.ArrayList

class DepartmentsViewModel: ViewModel() {


    private val depWithEmpLiveData = MutableLiveData<ArrayList<Department_Employee>>()

    fun getDepWithEmp(): LiveData<ArrayList<Department_Employee>> = depWithEmpLiveData

    fun setDepWithEmp(departments: ArrayList<Department_Employee>) {
        depWithEmpLiveData.postValue(departments)
    }


    // TODO: транзакцию с БД нужно вынести в другой поток
    // чтение промежуточной таблицы Департаменты_Работники
    fun readDepWithEmpFromDb(db: SQLiteDatabase) {
        val departments_employees = arrayListOf<Department_Employee>()
        var cursor: Cursor? = null

        try {
            cursor = db.query(
                DEPARTMENTS_EMPLOYEES,
                null,
                null,
                null,
                null,
                null,
                null
            )

            if (cursor.moveToFirst()) {
                do {
                    val departmentIdPos = cursor.getColumnIndex("departmentId")
                    val employeeIdPos = cursor.getColumnIndex("employeeId")
                    departments_employees.add(
                        Department_Employee(
                            departmentId = cursor.getInt(departmentIdPos),
                            employeeId = cursor.getInt(employeeIdPos)
                        )
                    )
                } while (cursor.moveToNext())

                setDepWithEmp(departments_employees)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
            db.close()
        }
    }
}