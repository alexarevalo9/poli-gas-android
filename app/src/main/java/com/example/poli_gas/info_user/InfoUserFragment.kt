package com.example.poli_gas.info_user

import android.os.Bundle
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.poli_gas.databinding.FragmentInfoUserBinding

class InfoUserFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentInfoUserBinding.inflate(inflater)
     /*   val arguments  = arguments?.let { InfoUserFragmentArgs.fromBundle(it) }*/


        binding.mapImage.setOnClickListener {
            //view!!.findNavController().navigate(InfoUserFragmentDirections.actionInfoUserFragmentToMapFragment(binding.phoneText.text.toString()))
            //Log.i("InfoUser", binding.phoneText.text.toString())
        }

        return binding.root
    }
}
