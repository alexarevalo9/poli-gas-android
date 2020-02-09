package com.example.poli_gas.detailorder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.poli_gas.R
import com.example.poli_gas.database.PoliGas
import com.example.poli_gas.databinding.FragmentSchedulerOrderDetailBinding
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.view.*

class SchedulerOrderDetailFragment : Fragment() {

    val db = FirebaseFirestore.getInstance()
    private lateinit var binding : FragmentSchedulerOrderDetailBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding =  FragmentSchedulerOrderDetailBinding.inflate(inflater)

        val navBottonm = activity!!.findViewById<View>(R.id.container)
        navBottonm.bottomNavigationView.setVisibility(View.VISIBLE)

        val arguments = SchedulerOrderDetailFragmentArgs.fromBundle(arguments!!)

        binding.setLifecycleOwner(this)

        getExpressOrderById(arguments.orderId)

        binding.close.setOnClickListener {
            this.findNavController().navigate(SchedulerOrderDetailFragmentDirections.actionSchedulerOrderDetailFragmentToSchedulerOrderFragment())
        }

        return binding.root
    }

    private fun getExpressOrderById(id : String){

        db.collection("scheduleorder")
            .whereEqualTo("poligasUID", id)
            .get()
            .addOnSuccessListener { documents ->
                val data = documents.toObjects(PoliGas::class.java)
                setGasImage(data.get(0))
                setTextGasString(data.get(0))
            }
    }

    fun setGasImage(item: PoliGas?) {
        item?.let {
            when (item.typeCylinder) {
                1 -> binding.poligasImage.setImageResource(R.drawable.gasindustrial)
                2 -> binding.poligasImage.setImageResource(R.drawable.gasazul)
                3 -> binding.poligasImage.setImageResource(R.drawable.gasamarillo)
                else -> binding.poligasImage.setImageResource(R.drawable.gasindustrial)
            }
        }
    }

    fun setTextGasString(item: PoliGas?) {
        item?.let {
            binding.dateString.text = "Fecha de Agendada: " + item.date +" - "+ item.hour
            binding.totalCylinders.text = "Cantidad de Cilindros: " +item.totalCylinder
        }
    }
}
