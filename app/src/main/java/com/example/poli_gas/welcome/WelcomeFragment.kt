package com.example.poli_gas.welcome

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.poli_gas.databinding.FragmentWelcomeBinding
import kotlinx.android.synthetic.main.activity_main.view.*
import androidx.appcompat.app.AppCompatActivity
import com.example.poli_gas.R


class WelcomeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentWelcomeBinding.inflate(inflater)

        val navBottonm = activity!!.findViewById<View>(R.id.container)
        navBottonm.bottomNavigationView.setVisibility(View.GONE)

        object : CountDownTimer(2000, 1000) {
            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {
                view!!.findNavController().navigate(WelcomeFragmentDirections.actionStartFragmentToAuthUserFragment())
            }
        }.start()

        return binding.root
    }

}
