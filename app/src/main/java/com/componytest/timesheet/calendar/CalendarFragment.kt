package com.componytest.timesheet.calendar

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.componytest.timesheet.BaseFragment
import com.componytest.timesheet.R
import com.roomorama.caldroid.CaldroidFragment
import java.util.*
import kotlin.collections.HashMap

class CalendarFragment: BaseFragment() {

    private val viewModel by lazy {
        ViewModelProvider(this, mainActivity.app.viewModelFactory)
            .get(CalendarViewModel::class.java)
    }
    private var calendarDialog = CaldroidFragment()  // view календаря, позаимствованный с гитхаба



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.readCalendarFromDb(mainActivity.dbHelper.readableDatabase)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewRoot = inflater.inflate(R.layout.fragment_calendar, container, false)

        createCalendar()
        viewModel.getCalendarDays().observe(viewLifecycleOwner, Observer { days ->
            fillCalendar(days)
        })

        return viewRoot
    }



    private fun createCalendar() {
        calendarDialog.arguments = Bundle().apply {
            putInt(CaldroidFragment.START_DAY_OF_WEEK, CaldroidFragment.MONDAY)
        }
        childFragmentManager.beginTransaction()
            .add(R.id.calendarContainer, calendarDialog)
            .commitAllowingStateLoss()
    }

    // раскраска различными цветами соотвествующих типов дней, полученных с БД
    private fun fillCalendar(days: HashMap<Date, String>) {
        calendarDialog.apply {
            days.forEach { day ->
                var colorDrawable: ColorDrawable? = null
                when (day.value) {
                    DayType.REST_DAY -> colorDrawable = ColorDrawable(resources.getColor(R.color.orange))
                    DayType.HOLIDAY -> colorDrawable = ColorDrawable(resources.getColor(R.color.red))
                    DayType.PRE_HOLIDAY -> colorDrawable = ColorDrawable(resources.getColor(R.color.yellow))
                }
                setBackgroundDrawableForDate(colorDrawable, day.key)
            }
        }
        calendarDialog.refreshView()
    }

}