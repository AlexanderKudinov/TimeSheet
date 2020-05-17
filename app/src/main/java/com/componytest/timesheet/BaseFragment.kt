package com.componytest.timesheet

import androidx.fragment.app.Fragment

open class BaseFragment: Fragment() {

    protected val mainActivity by lazy { activity as MainActivity }
}