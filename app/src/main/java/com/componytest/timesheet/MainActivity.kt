package com.componytest.timesheet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.componytest.timesheet.db.DBHelper
import com.componytest.timesheet.departments.Department
import com.componytest.timesheet.departments.DepartmentEmployeesConnector
import com.componytest.timesheet.profile.Employee
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity: AppCompatActivity() {

    val app by lazy { application as App}
    private val viewModel by lazy {
        ViewModelProvider(this, app.viewModelFactory).get(MainViewModel::class.java)
    }

    val departments = arrayListOf<Department>()
    lateinit var dbHelper: DBHelper
    var user: Employee? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        user = intent.getParcelableExtra("user")
        dbHelper = DBHelper(this)
        listenObservers()

        user?.departmentId = viewModel.readDepartmentForUser(user?.id, dbHelper.readableDatabase)
        viewModel.readDepartmentsFromDb(dbHelper.readableDatabase)

        val navController = findNavController(R.id.navHostFragment)
        val appBarConfiguration = AppBarConfiguration(
            topLevelDestinationIds = setOf(
                R.id.navProfile,
                R.id.navJobMarks,
                R.id.navCalendar
            )
        )
        bottomNavView.setupWithNavController(navController)
        setupActionBarWithNavController(navController, appBarConfiguration)

        if (!app.dbRecorderController.isCalendarWritten())
            viewModel.writeCalendarToDb(db = dbHelper.writableDatabase)
    }

    override fun onDestroy() {
        super.onDestroy()
        dbHelper.close()
    }



    fun showBottomNavigation() {
        bottomNavView.visibility = View.VISIBLE
    }

    fun hideBottomNavigation() {
        bottomNavView.visibility = View.GONE
    }

    private fun listenObservers() {
        viewModel.getDepartments().observe(this, Observer { departments ->
            this.departments.clear()
            this.departments.addAll(departments)
            DepartmentEmployeesConnector().findDepartmentForUser(departments, user)
        })
    }
}
