package com.example.poli_gas.progress_request

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.poli_gas.R
import com.example.poli_gas.databinding.FragmentProgressRequestBinding
import com.google.common.math.IntMath
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.kofigyan.stateprogressbar.StateProgressBar
import androidx.databinding.adapters.TextViewBindingAdapter.setText
import android.os.CountDownTimer

class ProgressRequestFragment : Fragment() {

    private val db = FirebaseFirestore.getInstance()
    private lateinit var binding: FragmentProgressRequestBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentProgressRequestBinding.inflate(inflater)

        binding.commentButton.setOnClickListener {
            view!!.findNavController().navigate(ProgressRequestFragmentDirections.actionProgressRequestFragmentToFeedbackFragment())
        }

        object : CountDownTimer(30000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                binding.progressTextView.setText("seconds remaining: " + millisUntilFinished / 1000)
            }

            override fun onFinish() {
                binding.progressTextView.setText("done!")
            }
        }.start()


        //binding.progressBar.enableAnimationToCurrentState(true)
        //binding.progressBar.setAnimationDuration(10000)
        //binding.progressBar.setAnimationStartDelay(5000)
        binding.progressBar.setMaxStateNumber(StateProgressBar.StateNumber.FOUR);
        binding.progressBar.checkStateCompleted(true)


        binding.progressTextView.setOnClickListener{
            binding.progressBar.enableAnimationToCurrentState(true)
            binding.progressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO)
        }

        binding.textView6.setOnClickListener {
            binding.progressBar.enableAnimationToCurrentState(true)
            binding.progressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE)
        }

        return binding.root
    }

    private fun getProgress(numberProgress : Int){
        when (numberProgress) {
            1 -> binding.progressTextView.setText("Buscando un Proveedor")
            2 -> binding.progressTextView.setText("Tú pedido está en camino")
            3 -> binding.progressTextView.setText("Tú pedido espera por ti!")
            4 -> binding.progressTextView.setText("Gracias por utilizar nuestra app")
            else -> binding.progressTextView.setText("Su pedido se enviara el día: DATE a las HORA")
        }

    }
}
