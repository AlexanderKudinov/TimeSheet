package com.componytest.timesheet.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.componytest.timesheet.BaseFragment
import com.componytest.timesheet.R
import com.componytest.timesheet.databinding.FragmentProfileBinding
import com.componytest.timesheet.profile.Employee.Companion.DEPARTMENT_ADMINISTRATOR
import com.componytest.timesheet.profile.Employee.Companion.WORKERS_ADMINISTRATOR
import kotlinx.android.synthetic.main.fragment_profile.view.*

class ProfileFragment: BaseFragment() {

    private lateinit var btnDepartments: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // dataBinding
        val binding: FragmentProfileBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_profile,
            container,
            false
        )

        Log.d("kkkk", "ddd")
        binding.user = mainActivity.user
        findViews(binding.root)
        setListeners()
        checkRights()

        return binding.root
    }



    private fun findViews(viewRoot: View) {
        btnDepartments = viewRoot.btnDepartments
    }

    private fun setListeners() {
        btnDepartments.setOnClickListener {
            findNavController().navigate(R.id.action_navProfile_to_departmentsFragment)
        }
    }

    private fun checkRights() {
        if (mainActivity.user?.jobPosition == WORKERS_ADMINISTRATOR ||
            mainActivity.user?.jobPosition == DEPARTMENT_ADMINISTRATOR) {
                btnDepartments.visibility = VISIBLE
        }
    }
}