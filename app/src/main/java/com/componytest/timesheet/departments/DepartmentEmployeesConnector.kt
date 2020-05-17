package com.componytest.timesheet.departments

import android.util.Log
import com.componytest.timesheet.profile.Employee

class DepartmentEmployeesConnector {

    // если departmentId = id любому из департаментов в списке, значит у него есть работники
    fun hasEmployees (
        departmentsWithEmployees: ArrayList<Department_Employee>,
        departmentId: Int
    ): Boolean {
        var hasEmployees = false
        run looper@ {
            departmentsWithEmployees.forEach { department ->
                if (departmentId == department.departmentId) {
                    hasEmployees = true
                    return@looper
                }
            }
        }
        return hasEmployees
    }

    // поиск названия департамента по id для текущего работника (если работник привязан к департаменту)
    fun findDepartmentForUser(departments: ArrayList<Department>, user: Employee?): Boolean {
        var isDepartmentFound = false
        run loop@{
            departments.forEach { department ->
                if (user?.departmentId == department.id) {
                    user.department = department.name
                    isDepartmentFound = true
                    return@loop
                }
            }
        }
        // если работник не привязан ни к одному из департаментов
        if (!isDepartmentFound) {
            user?.departmentId = -1
            user?.department = ""
        }

        return isDepartmentFound
    }

    // удаление связи работника с департаментом
    fun removeDepartmentWithUser(departmentsWithEmployee: ArrayList<Department_Employee>, userId: Int?) {
        var needToDelete = -1
        run loop@{
            for (i in 0 until departmentsWithEmployee.size) {
                if (userId == departmentsWithEmployee[i].employeeId) {
                    needToDelete = i
                    return@loop
                }
            }
        }

        // удаление связи
        if (needToDelete != -1)
            departmentsWithEmployee.removeAt(needToDelete)
    }
}