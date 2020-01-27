package com.example.poli_gas.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.poli_gas.R
import com.example.poli_gas.databinding.FragmentWelcomeBinding
import kotlinx.android.synthetic.main.activity_main.view.*


class WelcomeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentWelcomeBinding.inflate(inflater)

        binding.initButton.setOnClickListener {
            view!!.findNavController().navigate(WelcomeFragmentDirections.actionStartFragmentToInfoUserFragment(null, null))
        }

        return binding.root
    }
}
