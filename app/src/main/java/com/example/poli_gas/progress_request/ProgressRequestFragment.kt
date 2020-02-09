package com.example.poli_gas.progress_request

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.poli_gas.databinding.FragmentProgressRequestBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.kofigyan.stateprogressbar.StateProgressBar
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import com.example.poli_gas.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.view.*

class ProgressRequestFragment : Fragment() {

    private val db = FirebaseFirestore.getInstance()
    private lateinit var binding: FragmentProgressRequestBinding
    private var ORIGIN_LONGITUDE = ""
    private var ORIGIN_LATITUDE = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentProgressRequestBinding.inflate(inflater)

        val navBottonm = activity!!.findViewById<View>(R.id.container)
        navBottonm.bottomNavigationView.setVisibility(View.GONE)

        getCoordinates()

        binding.progressBar.checkStateCompleted(true)
        binding.progressBar.setMaxStateNumber(StateProgressBar.StateNumber.FOUR);

        binding.commentButton.setOnClickListener {
            view!!.findNavController().navigate(ProgressRequestFragmentDirections.actionProgressRequestFragmentToFeedbackFragment())
        }

        hideFeedbackResources()

        val arguments = arguments?.let { ProgressRequestFragmentArgs.fromBundle(it) }

        if(arguments?.progressState == true){
            animateProgressBar(StateProgressBar.StateNumber.THREE)
            showProgressText(3)
            expressOrder(3)
        }else{
            showProgressText(1)
            expressOrder(1)
        }

        return binding.root
    }


    private fun expressOrder(progressState : Int){
        countDownTimer(progressState)
    }

    private fun countDownTimer(progressState : Int){
        object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {
                when(progressState){
                    1->{
                        showProgressText(2)
                        animateProgressBar(StateProgressBar.StateNumber.TWO)
                        countDownTimer(2)
                    }

                    2 -> {
                        showProgressText(3)
                        animateProgressBar(StateProgressBar.StateNumber.THREE)
                        view!!.findNavController().navigate(ProgressRequestFragmentDirections.actionProgressRequestFragmentToNavigationMapFragment(ORIGIN_LATITUDE, ORIGIN_LONGITUDE))
                    }

                    3 -> {
                        showProgressText(4)
                        animateProgressBar(StateProgressBar.StateNumber.FOUR)
                        showFeedbackResources()
                    }

                    else ->{
                        Toast.makeText(context, "ERROR", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }.start()
    }

    private fun getCoordinates(){
        FirebaseAuth.getInstance().uid?.let { UID ->

            db.collection("coordinates").document(UID)
                .get()
                .addOnSuccessListener { document ->
                    ORIGIN_LATITUDE = document.get("latitud").toString()
                    ORIGIN_LONGITUDE = document.get("longitud").toString()
                }
        }
    }

    private fun animateProgressBar(currentState : StateProgressBar.StateNumber){
        binding.progressBar.enableAnimationToCurrentState(true)
        binding.progressBar.setCurrentStateNumber(currentState)
        if(currentState == StateProgressBar.StateNumber.FOUR){
            binding.progressBar.setAllStatesCompleted(true)
        }
    }

    private fun showProgressText(numberProgress : Int){
        when (numberProgress) {
            1 -> binding.progressTextView.setText(getString(R.string.buscandoprovee))
            2 -> binding.progressTextView.setText(getString(R.string.pedidolisto))
            3 -> binding.progressTextView.setText(getString(R.string.pedidoentregado))
            4 -> binding.progressTextView.setText(getString(R.string.gracias))
            else -> binding.progressTextView.setText("Su pedido se enviara el día: DATE a las HORA")
        }
    }

    private fun showFeedbackResources(){
        binding.commentButton.visibility = View.VISIBLE
        binding.commentTextView.visibility = View.VISIBLE
        binding.progressBar2.visibility = View.INVISIBLE
    }

    private fun hideFeedbackResources(){
        binding.commentButton.visibility = View.INVISIBLE
        binding.commentTextView.visibility = View.INVISIBLE
    }
}
