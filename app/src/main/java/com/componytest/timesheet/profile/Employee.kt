package com.componytest.timesheet.profile

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Employee(
    var firstName: String,
    var lastName: String,
    var jobPosition: String,
    var id: Int = -1,
    var departmentId: Int = -1,
    var department: String = ""
): Parcelable {
    companion object {
        const val WORKER = "Worker"  // работник
        const val TIMEKEEPER = "Timekeeper"  // табельщик
        const val DEPARTMENT_ADMINISTRATOR = "Department Administrator"  // Администратор справочника департаментов
        const val WORKERS_ADMINISTRATOR = "Workers Administrator"  // Администратор справочника работников
    }
}