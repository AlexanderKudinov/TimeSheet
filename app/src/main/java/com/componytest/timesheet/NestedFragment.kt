package com.componytest.timesheet

import android.content.Context
import android.view.View.GONE
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*

open class NestedFragment: BaseFragment() {


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity.hideBottomNavigation()
    }

    override fun onDetach() {
        super.onDetach()
        mainActivity.showBottomNavigation()
    }
}