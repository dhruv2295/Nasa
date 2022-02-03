package com.nasa.image.ActivityFunctionManager

import android.app.DatePickerDialog
import android.content.Context
import android.view.View
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import com.nasa.image.utils.AppConstraint.Companion.SDF
import java.text.ParseException
import java.util.*

class MainManager(private val context: Context) {

    fun showDateTimePicker(t: TextView, v: View) {
        val currentDate = Calendar.getInstance()
        val calendar = Calendar.getInstance()
        try {
            currentDate.time = SDF.parse(t.text.toString())
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        DatePickerDialog(
            context,
            { _: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                calendar.set(year, monthOfYear, dayOfMonth)
                if (calendar.time.after(SDF.parse(getToday()))) {
                    Toast.makeText(context, "Please select past date", Toast.LENGTH_SHORT).show()
                    v.visibility = View.GONE
                } else {
                    v.visibility = View.VISIBLE
                    t.text = SDF.format(calendar.time)
                }
            },
            currentDate[Calendar.YEAR], currentDate[Calendar.MONTH], currentDate[Calendar.DATE]
        ).show()
        v.visibility = View.GONE
    }

    fun getToday(): String = SDF.format(Date())

    fun getLastMonth(): String{
        val cal = Calendar.getInstance()
        cal.add(Calendar.MONTH, -1)
        return SDF.format(cal.time)
    }
}