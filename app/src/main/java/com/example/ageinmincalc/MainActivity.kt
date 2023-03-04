package com.example.ageinmincalc

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private var tvSelectedDate: TextView? = null
    private var dateinMinutes: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val datePicker = findViewById<Button>(R.id.btnDatePicker)
        tvSelectedDate = findViewById(R.id.tvSelectedDate)
        dateinMinutes= findViewById(R.id.tvAgeInMinutes)

        datePicker.setOnClickListener {
            clickDatePicker()
        }
    }

    fun clickDatePicker() {
        val myCalendar = Calendar.getInstance()
        val year: Int = myCalendar.get(Calendar.YEAR)
        val month: Int = myCalendar.get(Calendar.MONTH)
        val day: Int = myCalendar.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(this,
            DatePickerDialog.OnDateSetListener{view, selectedYear, selectedMonth, selectedDayOfMonth ->
                val selectedDate = "$selectedDayOfMonth/${selectedMonth+1}/$selectedYear"
                tvSelectedDate?.text = selectedDate

                val sdf = SimpleDateFormat("dd/MM/yyy", Locale.ENGLISH)
                val theDate = sdf.parse(selectedDate)
                
                theDate?.let {
                    //using .time we obtain a value in miliseconds which represents tha time passed
                    //from the 1 January 1970, so we should divide by 1000 to obtain seconds and then
                    //again by 60 to obtain minutes
                    //we obtain the minutes passed from 1970 to the selected date
                    val selectedDateInMinutes = theDate.time / 60000
                    //we obtain the minutes passed from 1970 to today
                    val currentDate = sdf.parse(sdf.format(System.currentTimeMillis()))
                    currentDate?.let {
                        val currentDateInMinutes = currentDate.time / 60000
                        val differenceInMinutes = currentDateInMinutes - selectedDateInMinutes
                        dateinMinutes?.text = differenceInMinutes.toString()
                    }

                }


            },
            year, month, day)
        //limit the selectable date to yesterday, can't select date in the future
        dpd.datePicker.maxDate = System.currentTimeMillis() - 86400000
        dpd.show()
    }
}