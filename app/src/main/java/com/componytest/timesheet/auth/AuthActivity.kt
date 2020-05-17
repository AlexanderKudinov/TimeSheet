package com.componytest.timesheet.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.componytest.timesheet.App
import com.componytest.timesheet.MainActivity
import com.componytest.timesheet.R
import com.componytest.timesheet.db.DBHelper
import com.componytest.timesheet.profile.Employee
import com.componytest.timesheet.profile.Employee.Companion.DEPARTMENT_ADMINISTRATOR
import com.componytest.timesheet.profile.Employee.Companion.TIMEKEEPER
import com.componytest.timesheet.profile.Employee.Companion.WORKER
import com.componytest.timesheet.profile.Employee.Companion.WORKERS_ADMINISTRATOR
import kotlinx.android.synthetic.main.activity_auth.*

class AuthActivity: AppCompatActivity() {

    private val app by lazy { application as App }
    private val viewModel by lazy {
        ViewModelProvider(this, app.viewModelFactory).get(AuthViewModel::class.java)
    }

    private val employees = arrayListOf<Employee>()
    private lateinit var dbHelper: DBHelper
    private lateinit var spinnerAdapter: ArrayAdapter<String>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayListOf<String>())
        spinnerAuth.adapter = spinnerAdapter

        dbHelper = DBHelper(this)
        if (!app.dbRecorderController.areEmployeesWritten())
            createWorkers()
        else
            viewModel.readEmployeesFromDb(dbHelper.readableDatabase)

        setListeners()
       listenObserver()
    }

    override fun onDestroy() {
        super.onDestroy()
        dbHelper.close()
    }


    private fun setListeners() {
        btnEnterToApp.setOnClickListener {
            enterToApp()
        }
    }

    private fun listenObserver() {
        // заполнение спиннера списком работником после получения из БД или state restore
        viewModel.getEmployees().observe(this, Observer { employees ->
            this.employees.clear()
            this.employees.addAll(employees)
            val spinnerItems = arrayListOf<String>()
            employees.forEach {  employee ->
                spinnerItems.add("${employee.lastName} ${employee.firstName} (${employee.jobPosition})")
            }
            spinnerAdapter.clear()
            spinnerAdapter.addAll(spinnerItems)
        })
    }

    // работники создаются только при первом входе в приложение (происходит заполнение БД),
    // TODO: лишний код, нужно в качестве примера
    private fun createWorkers() {
        val employees = arrayListOf<Employee>()
        employees.add(Employee("Alexander", "Kudinov", WORKERS_ADMINISTRATOR, 1))
        employees.add(Employee("Vladimir", "Kuratov", DEPARTMENT_ADMINISTRATOR, 2))
        employees.add(Employee("Ivan", "Ignatov", TIMEKEEPER, 3))
        employees.add(Employee("Sergey", "Malyshev", WORKER, 4))
        employees.add(Employee("IRINA", "KAZANZEVA", WORKER, 5))
        viewModel.writeEmployeesToDb(employees = employees, db = dbHelper.writableDatabase)
    }

    private fun enterToApp() {
        app.employees.clear()
        app.employees.addAll(employees)
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            employees[spinnerAuth.selectedItemPosition].departmentId = viewModel.getDepartmentId(
                employeeId = employees[spinnerAuth.selectedItemPosition].id,
                db = dbHelper.readableDatabase
            )
            putExtra("user", employees[spinnerAuth.selectedItemPosition])
        }
        startActivity(intent)
        finish()
    }
}