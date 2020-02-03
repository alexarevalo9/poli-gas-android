package com.example.poli_gas.schedule_request

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.poli_gas.R
import com.example.poli_gas.databinding.FragmentScheduleRequestBinding
import java.text.SimpleDateFormat
import java.util.*

class ScheduleRequestFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentScheduleRequestBinding.inflate(inflater)
        binding.scheduleOrderButton.setOnClickListener {

        }

        binding.datePickerButton.setOnClickListener {
            val cal = Calendar.getInstance()
            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH)
            val day = cal.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog =
                this.context?.let { it1 ->
                    DatePickerDialog(it1, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                        binding.deliveryDateText.setText("" + dayOfMonth + "/" + (month+1) + "/" + year)
                    },year,month,day)
                }
            if (datePickerDialog != null) {
                datePickerDialog.show()
            }
            }

        binding.timePickerButton.setOnClickListener {
            val cal = Calendar.getInstance()

            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY,hour)
                cal.set(Calendar.MINUTE,minute)

                binding.deliveryHourText.text = SimpleDateFormat("HH:mm").format(cal.time)
            }
            TimePickerDialog(this.context, timeSetListener, cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE), true).show()
        }


        return binding.root
    }
}
