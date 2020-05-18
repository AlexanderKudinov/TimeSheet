package com.componytest.timesheet.departments

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.componytest.timesheet.BaseFragment
import com.componytest.timesheet.NestedFragment
import com.componytest.timesheet.R
import com.componytest.timesheet.profile.Employee
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.dialog_create_department.view.*
import kotlinx.android.synthetic.main.fragment_departments.view.*
import java.lang.ref.WeakReference

class DepartmentsFragment: NestedFragment(), IDepartmentView, DepartmentsRvAdapter.IClickListener {

    private val viewModel by lazy {
        ViewModelProvider(this, mainActivity.app.viewModelFactory).get(DepartmentsViewModel::class.java)
    }

    private var departmentsWithEmployees = arrayListOf<Department_Employee>()
    private lateinit var adapter: DepartmentsRvAdapter
    private lateinit var btnAddDepartment: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var departmentPresenter: IDepartmentPresenter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.readDepWithEmpFromDb(mainActivity.dbHelper.readableDatabase)
        adapter = DepartmentsRvAdapter(WeakReference(this), mainActivity.user?.jobPosition)
        adapter.setDepartments(mainActivity.departments)
        departmentPresenter = DepartmentsPresenter(WeakReference(this))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewRoot = inflater.inflate(R.layout.fragment_departments, container, false)

        findViews(viewRoot)
        recyclerView.adapter = adapter
        setListeners()
        checkRights(viewRoot)

        viewModel.getDepWithEmp().observe(viewLifecycleOwner, Observer { em ->
            departmentsWithEmployees = em
        })

        return viewRoot
    }

    override fun onDestroy() {
        super.onDestroy()
        departmentPresenter.clearPresenter()
    }



    private fun findViews(viewRoot: View) {
        recyclerView = viewRoot.rvDepartments
        btnAddDepartment = viewRoot.btnAddDepartment
    }

    private fun setListeners() {
        btnAddDepartment.setOnClickListener {
            createDialogForAdding()
        }
    }

    private fun createDialogForAdding() {
        val dialogBuilder = AlertDialog.Builder(context)
        val dialogView = LayoutInflater.from(context)
            .inflate(R.layout.dialog_create_department, null)
        val etEnterDepartment = dialogView.etEnterDepartment_dialog

        dialogBuilder.apply {
            setView(dialogView)
            setCancelable(false)
            setPositiveButton(resources.getString(R.string.ok)) { _, _ ->
                departmentPresenter.addDepartmentToDb(
                    departmentName = etEnterDepartment.text.toString(),
                    db = mainActivity.dbHelper.writableDatabase
                )
            }
            setNegativeButton(resources.getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
        }.create().show()
    }

    private fun createDialogForChanging(departmentId: Int, position: Int) {
        val dialogBuilder = AlertDialog.Builder(context)
        val dialogView = LayoutInflater.from(context)
            .inflate(R.layout.dialog_create_department, null)
        val etEnterDepartment = dialogView.etEnterDepartment_dialog

        dialogBuilder.apply {
            setView(dialogView)
            setCancelable(false)
            setPositiveButton(resources.getString(R.string.ok)) { _, _ ->
                departmentPresenter.changeDepartmentInDb(
                    department = Department(departmentId, etEnterDepartment.text.toString()),
                    position = position,
                    db = mainActivity.dbHelper.writableDatabase
                )
            }
            setNegativeButton(resources.getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
        }.create().show()
    }

    private fun createDialogForAddingEmployeeToDep(departmentId: Int) {
        val dialogBuilder = AlertDialog.Builder(context)
        val employeesNames = arrayListOf<String>()
        mainActivity.app.employees.forEach { employee ->
            employeesNames.add("${employee.lastName} ${employee.firstName}")
        }

        dialogBuilder.apply {
            setSingleChoiceItems(
                employeesNames.toTypedArray(),
                -1,
                DialogInterface.OnClickListener { dialog, pos ->
                    // удаление связи перед созданием новой
                    DepartmentEmployeesConnector().removeDepartmentWithUser(
                        departmentsWithEmployees,
                        mainActivity.app.employees[pos].id
                    )
                    departmentsWithEmployees.add(
                        Department_Employee(
                            departmentId = departmentId,
                            employeeId = mainActivity.app.employees[pos].id)
                    )
                    viewModel.setDepWithEmp(departmentsWithEmployees)
                    departmentPresenter.addEmployeeToDepartment(
                        departmentId = departmentId,
                        employeeId = mainActivity.app.employees[pos].id,
                        db = mainActivity.dbHelper.writableDatabase
                    )
                    dialog.dismiss()
                }
            )
        }.create().show()
    }

    private fun checkRights(viewRoot: View) {
        if (mainActivity.user?.jobPosition == Employee.WORKERS_ADMINISTRATOR)
            btnAddDepartment.hide()
    }





    /** implementations of Adapter Click Listener interface */
    override fun removeDepartment(departmentId: Int, position: Int) {
        if (DepartmentEmployeesConnector().hasEmployees(departmentsWithEmployees, departmentId)) {
            Snackbar.make(
                btnAddDepartment,
                "Удалить департамент нельзя, в департаменте работают люди!",
                Snackbar.LENGTH_SHORT
            ).show()
        }
        else {
            adapter.removeDepartment(position)
            mainActivity.departments.removeAt(position)
            departmentPresenter.removeDepartmentFromDb(
                departmentId,
                mainActivity.dbHelper.writableDatabase
            )
        }
    }

    override fun changeDepartment(departmentId: Int, position: Int) {
        createDialogForChanging(departmentId, position)
    }

    override fun addEmployeeToDepartment(departmentId: Int) {
        createDialogForAddingEmployeeToDep(departmentId)
    }




    /** implementations of View interface */
    override fun addDepartmentToAdapter(department: Department) {
        mainActivity.departments.add(department)
        adapter.addDepartment(department)
    }

    override fun changeDepartmentInAdapter(position: Int, department: Department) {
        mainActivity.departments[position] = department
        adapter.changeDepartment(position, department)
    }
}