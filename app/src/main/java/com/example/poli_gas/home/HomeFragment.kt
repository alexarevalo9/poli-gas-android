package com.example.poli_gas.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.example.poli_gas.R
import com.example.poli_gas.databinding.FragmentHomeBinding
import kotlinx.android.synthetic.main.activity_main.view.*

class HomeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentHomeBinding.inflate(inflater)
        var change = activity!!.findViewById<View>(R.id.container)
        change.bottomNavigationView.setVisibility(View.VISIBLE)
        // Get a reference to the ViewModel associated with this fragment.
        val homeViewModel= ViewModelProviders.of(this).get(HomeViewModel::class.java)

        binding.homeViewModel = homeViewModel

        binding.setLifecycleOwner(this)

        binding.makeOrderButton.setOnClickListener {
            view!!.findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToTypeRequestFragment())
        }

        // Add an Observer on the state variable for showing a Toast message
        homeViewModel.showToastEvent.observe(this, Observer {
            if (it == 3) { // Observed state is true.
                Toast.makeText(context,"Numero maximo de cilindros alcanzados",Toast.LENGTH_SHORT).show()
                homeViewModel.doneShowingToast()
            }

            if (it == 1){
                Toast.makeText(context,"Numero minimo de cilindros alcanzados",Toast.LENGTH_SHORT).show()
                homeViewModel.doneShowingToast()
            }
        })

        return binding.root
    }

}
