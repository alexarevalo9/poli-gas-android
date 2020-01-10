package com.example.poli_gas.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.poli_gas.R
import com.example.poli_gas.databinding.FragmentHomeBinding
import kotlinx.android.synthetic.main.activity_main.view.*

class HomeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentHomeBinding.inflate(inflater)
        var change = activity!!.findViewById<View>(R.id.container)
        change.bottomNavigationView.setVisibility(View.VISIBLE)
        return binding.root
    }

}
