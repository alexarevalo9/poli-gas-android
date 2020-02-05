package com.example.poli_gas.type_request

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.poli_gas.R
import com.example.poli_gas.databinding.FragmentTypeRequestBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class TypeRequestFragment : Fragment() {

    val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentTypeRequestBinding.inflate(inflater)

        val arguments = arguments?.let { TypeRequestFragmentArgs.fromBundle(it) }

        when (arguments?.typeCylinder) {
            1 -> binding.cylinderView.setImageResource(R.drawable.gasindustrial)
            2 -> binding.cylinderView.setImageResource(R.drawable.gasazul)
            3 -> binding.cylinderView.setImageResource(R.drawable.gasamarillo)
            else -> binding.cylinderView.setImageResource(R.drawable.gasindustrial)
        }

        arguments?.totalCylinder.let {
            binding.quantityText.text = it
        }



        binding.expressOrderButton.setOnClickListener {
            view!!.findNavController()
                .navigate(TypeRequestFragmentDirections.actionTypeRequestFragmentToProgressRequestFragment())
        }

        binding.scheduleOrderButton.setOnClickListener {
            view!!.findNavController().navigate(TypeRequestFragmentDirections.actionTypeRequestFragmentToScheduleRequestFragment(arguments!!.totalCylinder, arguments.typeCylinder))
        }


        FirebaseAuth.getInstance().uid?.let {
            db.collection(it)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        Log.i("TypeReHua", "${document.id} => ${document.data}")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.i("TypeReHua", "Error getting documents.", exception)
                }
        }

        return binding.root
    }
}
