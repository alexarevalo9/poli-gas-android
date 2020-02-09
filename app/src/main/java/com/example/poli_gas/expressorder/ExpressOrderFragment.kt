package com.example.poli_gas.expressorder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.poli_gas.R
import com.example.poli_gas.database.PoliGas
import com.example.poli_gas.databinding.FragmentExpressOrderBinding
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.view.*

class ExpressOrderFragment : Fragment() {

    val db = FirebaseFirestore.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentExpressOrderBinding.inflate(inflater)

        val navBottonm = activity!!.findViewById<View>(R.id.container)
        navBottonm.bottomNavigationView.setVisibility(View.VISIBLE)

        val expressOrderViewModel =  ViewModelProviders.of(this).get(ExpressOrderViewModel::class.java)

        binding.expressOrderViewModel = expressOrderViewModel

        binding.setLifecycleOwner(this)


        val manager = LinearLayoutManager(activity)
        binding.expressList.layoutManager = manager

        expressOrderViewModel.navigateToOrderDetailFragment.observe(this, Observer { order ->
            order?.let {

                this.findNavController().navigate(ExpressOrderFragmentDirections.actionExpressOrderFragmentToOrderDetailFragment(order))
                expressOrderViewModel.onSleepDataQualityNavigated()
            }
        })

        val adapter = ExpressOrderAdapter(PoliGasListener { orderId -> expressOrderViewModel.onPoliGasClicked(orderId) })

        binding.expressList.adapter = adapter


        db.collection("express").get()
            .addOnSuccessListener { document ->
                val data = document.toObjects(PoliGas::class.java)
                adapter.addHeaderAndSubmitList(data)
            }

        return binding.root
    }

}
