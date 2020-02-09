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
import com.example.poli_gas.database.PoliGas
import com.example.poli_gas.database.PoligasDatabaseDao
import com.example.poli_gas.databinding.FragmentTypeRequestBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.view.*
import java.text.SimpleDateFormat
import java.util.*

class TypeRequestFragment : Fragment() {

    val db = FirebaseFirestore.getInstance()
    lateinit var data : TypeRequestFragmentArgs

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentTypeRequestBinding.inflate(inflater)

        val navBottonm = activity!!.findViewById<View>(R.id.container)
        navBottonm.bottomNavigationView.setVisibility(View.VISIBLE)


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
        val uuidPoligas = UUID.randomUUID().toString()

        val formatDate = SimpleDateFormat("dd/M/yyyy")
        val formatHour = SimpleDateFormat("hh:mm")
        val date = formatDate.format(Date())
        val hour = formatHour.format(Date())

        val poligas = PoligasDatabaseDao()
        val dataPoligas = PoliGas(
            userUID,
            uuidPoligas,
            data.typeCylinder,
            data.totalCylinder,
            date,
            hour
        )

        poligas.insertExpressOrder(dataPoligas, context, view)

    }
}
