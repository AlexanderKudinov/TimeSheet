package com.componytest.timesheet.departments

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import androidx.core.content.contentValuesOf
import com.componytest.timesheet.Table
import com.componytest.timesheet.Table.DEPARTMENTS
import com.componytest.timesheet.Table.DEPARTMENTS_EMPLOYEES
import java.lang.Exception
import java.lang.ref.WeakReference

interface IDepartmentPresenter {
    fun addDepartmentToDb(departmentName: String, db: SQLiteDatabase)
    fun changeDepartmentInDb(department: Department, position: Int, db: SQLiteDatabase)
    fun removeDepartmentFromDb(departmentId: Int, db: SQLiteDatabase)
    fun addEmployeeToDepartment(departmentId: Int, employeeId: Int, db: SQLiteDatabase)
    fun clearPresenter()
}

interface IDepartmentView {
    fun addDepartmentToAdapter(department: Department)
    fun changeDepartmentInAdapter(position: Int, department: Department)
}

// TODO: можно создать репозиторий и вынести туда большую часть логики
class DepartmentsPresenter(
    private val departmentView: WeakReference<IDepartmentView>
): IDepartmentPresenter {


    override fun addDepartmentToDb(departmentName: String, db: SQLiteDatabase) {
        if (departmentName.isEmpty()) return
        val contentValues = ContentValues()
        var cursor: Cursor? = null
        contentValues.put("name", departmentName)

        try {
            db.insert(DEPARTMENTS, null, contentValues)
            cursor = db.rawQuery(
                "SELECT id FROM $DEPARTMENTS WHERE rowid=last_insert_rowid();",
                null
            )
            // добавление нового департамента в recyclerView
            if (cursor.moveToFirst()) {
                val id = cursor.getInt(0)
                departmentView.get()?.addDepartmentToAdapter(Department(id, departmentName))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
            db.close()
        }
    }

    override fun changeDepartmentInDb(department: Department, position: Int, db: SQLiteDatabase) {
        if (department.name.isEmpty()) return
        val contentValues = ContentValues()
        var cursor: Cursor? = null
        contentValues.put("name", department.name)

        try {
            db.update(
                DEPARTMENTS,
                contentValues,
                "id = ?",
                arrayOf(department.id.toString())
            )

            departmentView.get()?.changeDepartmentInAdapter(position, department)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
            db.close()
        }
    }


    override fun removeDepartmentFromDb(departmentId: Int, db: SQLiteDatabase) {
        var cursor: Cursor? = null
        try {
            db.delete(DEPARTMENTS, "ID = ?", arrayOf(departmentId.toString()))
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
            db.close()
        }
    }

    override fun addEmployeeToDepartment(departmentId: Int, employeeId: Int, db: SQLiteDatabase) {
        val contentValues = ContentValues()
        var cursor: Cursor? = null
        try {
            // удаление записи с предыдущем департамента работника
            db.delete(DEPARTMENTS_EMPLOYEES, "employeeId = ?", arrayOf(employeeId.toString()))
            contentValues.put("departmentId", departmentId)
            contentValues.put("employeeId", employeeId)
            db.insert(DEPARTMENTS_EMPLOYEES, null, contentValues)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
            db.close()
        }
    }

    override fun clearPresenter() {
        departmentView.clear()
    }
}