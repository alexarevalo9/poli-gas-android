package com.example.poli_gas.type_request

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.poli_gas.R
import com.example.poli_gas.databinding.FragmentTypeRequestBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class TypeRequestFragment : Fragment() {

    val db = FirebaseFirestore.getInstance()
    lateinit var data : TypeRequestFragmentArgs

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentTypeRequestBinding.inflate(inflater)

        data = arguments?.let { TypeRequestFragmentArgs.fromBundle(it) }!!

        when (data.typeCylinder) {
            1 -> binding.cylinderView.setImageResource(R.drawable.gasindustrial)
            2 -> binding.cylinderView.setImageResource(R.drawable.gasazul)
            3 -> binding.cylinderView.setImageResource(R.drawable.gasamarillo)
            else -> binding.cylinderView.setImageResource(R.drawable.gasindustrial)
        }

        data.totalCylinder.let {
            binding.quantityText.text = it
        }

        binding.expressOrderButton.setOnClickListener {
            saveExpressOrder()
        }

        binding.scheduleOrderButton.setOnClickListener {
            view!!.findNavController().navigate(TypeRequestFragmentDirections.actionTypeRequestFragmentToScheduleRequestFragment(data.totalCylinder, data.typeCylinder))
        }

        return binding.root
    }

    private fun saveExpressOrder(){

        val userUID = FirebaseAuth.getInstance().uid

        val formatDate = SimpleDateFormat("dd/M/yyyy")
        val formatHour = SimpleDateFormat("hh:mm")
        val date = formatDate.format(Date())
        val hour = formatHour.format(Date())

        val dataOrder = hashMapOf(
            "userUid" to userUID,
            "typeCylinder" to data?.typeCylinder,
            "totalCylinder" to data?.totalCylinder,
            "date" to date,
            "hour" to hour
        )

        db.collection("express")
            .add(dataOrder)
            .addOnSuccessListener {
                Log.i("ScheduleRequest", "DocumentSnapshot successfully written!")
                view!!.findNavController().navigate(TypeRequestFragmentDirections.actionTypeRequestFragmentToProgressRequestFragment(false))
            }
            .addOnFailureListener {
                    e -> Log.i("ScheduleRequest", "Error writing document", e)
                Toast.makeText(context, "No se ha podido realizar su pedido intentelo mas tarde", Toast.LENGTH_LONG).show()

            }
    }
}
