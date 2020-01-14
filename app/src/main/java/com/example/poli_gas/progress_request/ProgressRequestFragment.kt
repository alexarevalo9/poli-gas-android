package com.example.poli_gas.progress_request

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.poli_gas.databinding.FragmentProgressRequestBinding

class ProgressRequestFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentProgressRequestBinding.inflate(inflater)
        binding.commentButton.setOnClickListener {
            view!!.findNavController().navigate(ProgressRequestFragmentDirections.actionProgressRequestFragmentToFeedbackFragment())
        }
        return binding.root
    }
}
