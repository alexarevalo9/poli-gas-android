package com.example.poli_gas.info_user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.poli_gas.R
import com.example.poli_gas.databinding.FragmentInfoUserBinding

class InfoUserFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentInfoUserBinding.inflate(inflater)
        binding.saveButton.setOnClickListener {
            view!!.findNavController().navigate(InfoUserFragmentDirections.actionInfoUserFragmentToHomeFragment())
        }
        return binding.root
    }
}
