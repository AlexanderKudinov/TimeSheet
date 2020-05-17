package com.componytest.timesheet.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.componytest.timesheet.Table.CALENDAR
import com.componytest.timesheet.Table.DEPARTMENTS
import com.componytest.timesheet.Table.DEPARTMENTS_EMPLOYEES
import com.componytest.timesheet.Table.EMPLOYEES
import com.componytest.timesheet.Table.EMPLOYEES_MARKS
import com.componytest.timesheet.Table.JOB_MARKS

class DBHelper(context: Context):
    SQLiteOpenHelper(context,
        DATABASE_NAME, null,
        VERSION
    ) {

    override fun onCreate(db: SQLiteDatabase?) {
        // table of departments
        db?.execSQL("CREATE TABLE $DEPARTMENTS (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name VARCHAR);"
        )
        // table between departments and employees
        db?.execSQL("CREATE TABLE $DEPARTMENTS_EMPLOYEES (" +
                "departmentId INTEGER," +
                "employeeId INTEGER);"
        )
        // table of employees
        db?.execSQL("CREATE TABLE $EMPLOYEES (" +
                "id INTEGER PRIMARY KEY," +
                "firstName VARCHAR," +
                "lastName VARCHAR," +
                "jobPosition VARCHAR);"
        )
        // table between employees and marks
        db?.execSQL("CREATE TABLE $EMPLOYEES_MARKS (" +
                "employeeId INTEGER," +
                "markId INTEGER);"
        )
        // table of job marks
        db?.execSQL("CREATE TABLE $JOB_MARKS (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "date TEXT," +
                "type VARCHAR);"
        )
        // calendar table
        db?.execSQL("CREATE TABLE $CALENDAR (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "date TEXT," +
                "dayType VARCHAR);"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {

    }


    companion object {
        const val DATABASE_NAME = "DATABASE"
        const val VERSION = 1
    }
}