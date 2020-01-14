package com.example.poli_gas.type_request

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.poli_gas.R
import com.example.poli_gas.databinding.FragmentTypeRequestBinding

class TypeRequestFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentTypeRequestBinding.inflate(inflater)
        binding.expressOrderButton.setOnClickListener {
            view!!.findNavController().navigate(TypeRequestFragmentDirections.actionTypeRequestFragmentToProgressRequestFragment())
        }

        binding.scheduleOrderButton.setOnClickListener {
            view!!.findNavController().navigate(TypeRequestFragmentDirections.actionTypeRequestFragmentToScheduleRequestFragment())
        }

        return binding.root
    }
}
