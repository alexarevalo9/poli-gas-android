package com.example.poli_gas.detailorder

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.poli_gas.R
import com.example.poli_gas.database.PoliGas
import com.example.poli_gas.database.PoligasDatabaseDao
import com.example.poli_gas.databinding.FragmentOrderDetailBinding
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.view.*

class OrderDetailFragment : Fragment() {

    val db = FirebaseFirestore.getInstance()
    private lateinit var binding : FragmentOrderDetailBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding =  FragmentOrderDetailBinding.inflate(inflater)

        val navBottonm = activity!!.findViewById<View>(R.id.container)
        navBottonm.bottomNavigationView.setVisibility(View.VISIBLE)

        val arguments = OrderDetailFragmentArgs.fromBundle(arguments!!)

        binding.setLifecycleOwner(this)

        getExpressOrderById(arguments.orderId)

        binding.close.setOnClickListener {
            this.findNavController().navigate(OrderDetailFragmentDirections.actionOrderDetailFragmentToExpressOrderFragment())
        }

        return binding.root
    }

    private fun getExpressOrderById(id : String){

        db.collection("express")
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
            binding.dateString.text = "Fecha de Compra: " + item.date +" - "+ item.hour
            binding.totalCylinders.text = "Cantidad de Cilindros: " +item.totalCylinder
        }
    }
}
