package com.componytest.timesheet.departments

import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.componytest.timesheet.R
import com.componytest.timesheet.profile.Employee.Companion.DEPARTMENT_ADMINISTRATOR
import com.componytest.timesheet.profile.Employee.Companion.WORKERS_ADMINISTRATOR
import kotlinx.android.synthetic.main.rv_card_department.view.*
import java.lang.ref.WeakReference

class DepartmentsRvAdapter(
    private val clickListener: WeakReference<IClickListener>,
    private val userJob: String?
): RecyclerView.Adapter<DepartmentsRvAdapter.DepartmentViewHolder>() {

    private val departments = arrayListOf<Department>()


    // обновление списка происходит только при перерисовке фрагмента или при полной замене списка
    fun setDepartments(departments: ArrayList<Department>) {
        this.departments.clear()
        this.departments.addAll(departments)
        notifyDataSetChanged()
    }

    fun addDepartment(department: Department) {
        departments.add(department)
        notifyItemInserted(departments.size - 1)
    }

    fun removeDepartment(position: Int) {
        departments.removeAt(position)
        notifyItemRemoved(position)
    }

    fun changeDepartment(position: Int, department: Department) {
        if (departments.size > position) {
            departments[position] = department
            notifyItemChanged(position)
        }
    }


    override fun getItemCount() = departments.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DepartmentViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.rv_card_department,
            parent,
            false
        )
        return DepartmentViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DepartmentViewHolder, position: Int) {
        holder.bind()
    }



    inner class DepartmentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val btnChange = itemView.btnChangeDepartment
        private val btnDelete = itemView.btnDeleteDepartment
        private val btnAddEmployee = itemView.btnAddEmployeeToDepartment
        private val tvDepartmentName = itemView.tvDepartment_departments

        init {
            btnChange.setOnClickListener {
                clickListener.get()?.changeDepartment(departments[adapterPosition].id, adapterPosition)
            }
            btnDelete.setOnClickListener {
                clickListener.get()?.removeDepartment(departments[adapterPosition].id, adapterPosition)
            }
            btnAddEmployee.setOnClickListener {
                clickListener.get()?.addEmployeeToDepartment(departments[adapterPosition].id)
            }
        }

        fun bind() {
            tvDepartmentName.text = departments[adapterPosition].name
            if (userJob == DEPARTMENT_ADMINISTRATOR)
                btnChange.visibility = VISIBLE
            else if (userJob == WORKERS_ADMINISTRATOR) {
                btnAddEmployee.visibility = VISIBLE
                btnDelete.visibility = INVISIBLE
            }
        }
    }



    interface IClickListener {
        fun removeDepartment(departmentId: Int, position: Int)
        fun changeDepartment(departmentId: Int, position: Int)
        fun addEmployeeToDepartment(departmentId: Int)
    }
}