package com.example.poli_gas.start

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.poli_gas.R
import com.example.poli_gas.databinding.FragmentStartBinding
import kotlinx.android.synthetic.main.activity_main.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.view.*


class StartFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentStartBinding.inflate(inflater)
        var change = activity!!.findViewById<View>(R.id.container)
        change.bottomNavigationView.setVisibility(View.GONE)
        binding.initButton.setOnClickListener {
            view!!.findNavController().navigate(StartFragmentDirections.actionStartFragmentToHomeFragment())
        }
        return binding.root
    }
}
