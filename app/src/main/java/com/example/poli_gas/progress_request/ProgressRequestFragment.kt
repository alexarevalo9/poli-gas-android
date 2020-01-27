package com.example.poli_gas.progress_request

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.poli_gas.databinding.FragmentProgressRequestBinding
import com.kofigyan.stateprogressbar.StateProgressBar

class ProgressRequestFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentProgressRequestBinding.inflate(inflater)
        binding.commentButton.setOnClickListener {
            view!!.findNavController().navigate(ProgressRequestFragmentDirections.actionProgressRequestFragmentToFeedbackFragment())
        }


        //binding.progressBar.enableAnimationToCurrentState(true)
        //binding.progressBar.setAnimationDuration(10000)
        //binding.progressBar.setAnimationStartDelay(5000)
        binding.progressBar.setMaxStateNumber(StateProgressBar.StateNumber.FOUR);
        binding.progressBar.checkStateCompleted(true)


        binding.textView9.setOnClickListener{
            binding.progressBar.enableAnimationToCurrentState(true)
            binding.progressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO)
        }

        binding.textView6.setOnClickListener {
            binding.progressBar.enableAnimationToCurrentState(true)
            binding.progressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE)
        }

        return binding.root
    }
}
