package com.example.poli_gas.mysettings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.poli_gas.R
import com.example.poli_gas.databinding.FragmentSettingsBinding
import kotlinx.android.synthetic.main.activity_main.view.*

class SettingsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentSettingsBinding.inflate(inflater)

        val navBottonm = activity!!.findViewById<View>(R.id.container)
        navBottonm.bottomNavigationView.setVisibility(View.VISIBLE)

        NavigationUI.setupWithNavController(binding.navViewSettings, findNavController())
        return binding.root
    }

}
