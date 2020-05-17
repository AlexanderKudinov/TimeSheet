package com.componytest.timesheet.departments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.componytest.timesheet.R
import kotlinx.android.synthetic.main.rv_card_department.view.*
import java.lang.ref.WeakReference

class DepartmentsRvAdapter(
    private val clickListener: WeakReference<IClickListener>
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
        private val btnAdd = itemView.btnAddEmployeeToDepartment
        private val tvDepartmentName = itemView.tvDepartment_departments

        init {
            btnChange.setOnClickListener {
                clickListener.get()?.changeDepartment(departments[adapterPosition].id, adapterPosition)
            }
            btnDelete.setOnClickListener {
                clickListener.get()?.removeDepartment(departments[adapterPosition].id, adapterPosition)
            }
            btnAdd.setOnClickListener {
                clickListener.get()?.addEmployeeToDepartment(departments[adapterPosition].id)
            }
        }

        fun bind() {
            tvDepartmentName.text = departments[adapterPosition].name
        }
    }



    interface IClickListener {
        fun removeDepartment(departmentId: Int, position: Int)
        fun changeDepartment(departmentId: Int, position: Int)
        fun addEmployeeToDepartment(departmentId: Int)
    }
}