package com.componytest.timesheet.jobMarks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.componytest.timesheet.BaseFragment
import com.componytest.timesheet.R

class JobMarksFragment: BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewRoot = inflater.inflate(R.layout.fragment_job_marks, container, false)

        return viewRoot
    }
}