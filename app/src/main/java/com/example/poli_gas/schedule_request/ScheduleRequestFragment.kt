package com.example.poli_gas.schedule_request

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.poli_gas.R
import com.example.poli_gas.databinding.FragmentScheduleRequestBinding

class ScheduleRequestFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentScheduleRequestBinding.inflate(inflater)
        binding.scheduleOrderButton.setOnClickListener {

        }
        return binding.root
    }
}
