package com.example.poli_gas.feedback

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.poli_gas.databinding.FragmentFeedbackBinding


class FeedbackFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentFeedbackBinding.inflate(inflater)
        return binding.root
    }
}
