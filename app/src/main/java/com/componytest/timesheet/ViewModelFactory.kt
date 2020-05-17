package com.componytest.timesheet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.componytest.timesheet.auth.AuthViewModel
import com.componytest.timesheet.calendar.CalendarViewModel
import com.componytest.timesheet.departments.DepartmentsViewModel
import com.componytest.timesheet.jobMarks.JobMarksViewModel

class ViewModelFactory(val app: App): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>) = when (modelClass) {
        CalendarViewModel::class.java -> CalendarViewModel() as T
        MainViewModel::class.java -> MainViewModel(app) as T
        JobMarksViewModel::class.java -> JobMarksViewModel(app) as T
        DepartmentsViewModel::class.java -> DepartmentsViewModel() as T
        AuthViewModel::class.java -> AuthViewModel(app) as T
        else -> modelClass.newInstance()
    }
}