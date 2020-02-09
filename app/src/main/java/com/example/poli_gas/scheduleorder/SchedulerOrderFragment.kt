package com.example.poli_gas.scheduleorder

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
import com.example.poli_gas.databinding.FragmentSchedulerOrderBinding
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.view.*

class SchedulerOrderFragment : Fragment() {

    val db = FirebaseFirestore.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentSchedulerOrderBinding.inflate(inflater)

        val navBottonm = activity!!.findViewById<View>(R.id.container)
        navBottonm.bottomNavigationView.setVisibility(View.VISIBLE)

        val schedulerOrderViewModel =  ViewModelProviders.of(this).get(SchedulerOrderViewModel::class.java)

        binding.schedulerOrderViewModel = schedulerOrderViewModel

        binding.setLifecycleOwner(this)


        val manager = LinearLayoutManager(activity)
        binding.schedulerList.layoutManager = manager

        schedulerOrderViewModel.navigateToOrderDetailScheFragment.observe(this, Observer { order ->
            order?.let {

                this.findNavController().navigate(SchedulerOrderFragmentDirections.actionSchedulerOrderFragmentToSchedulerOrderDetailFragment(order))
                schedulerOrderViewModel.onOrderDetailScheFragmentNavigated()
            }
        })

        val adapter = SchedulerOrderAdapter(PoliGasSchedulerListener { orderId -> schedulerOrderViewModel.onPoliGasClicked(orderId) })

        binding.schedulerList.adapter = adapter


        db.collection("scheduleorder").get()
            .addOnSuccessListener { document ->
                val data = document.toObjects(PoliGas::class.java)
                adapter.addHeaderAndSubmitList(data)
            }

        return binding.root
    }
}
