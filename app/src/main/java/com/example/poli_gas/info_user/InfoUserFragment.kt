package com.example.poli_gas.info_user

import android.os.Bundle
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.poli_gas.databinding.FragmentInfoUserBinding

class InfoUserFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentInfoUserBinding.inflate(inflater)
     /*   val arguments  = arguments?.let { InfoUserFragmentArgs.fromBundle(it) }*/


        binding.mapImage.setOnClickListener {
            //view!!.findNavController().navigate(InfoUserFragmentDirections.actionInfoUserFragmentToMapFragment(binding.phoneText.text.toString()))
            //Log.i("InfoUser", binding.phoneText.text.toString())
        }

        /*
        if(arguments?.phone != null){
            binding.phoneText.setText(arguments.phone)
        }

        binding.saveButton.setOnClickListener {
            if(arguments?.latLog != null && binding.phoneText.text.toString() != ""){
                view!!.findNavController().navigate(InfoUserFragmentDirections.actionInfoUserFragmentToHomeFragment())
                Log.i("InfoUser", "${arguments?.latLog?.get(0)}-${arguments?.latLog?.get(1)}")

            }else if(arguments?.latLog == null && binding.phoneText.text.toString() != ""){
                Toast.makeText(context,"Debe elegir una Ubicación!!", Toast.LENGTH_SHORT).show()

            }else if(binding.phoneText.text.toString() == "" && arguments?.latLog != null ){
                Toast.makeText(context,"Debe ingresar un número de teléfono", Toast.LENGTH_SHORT).show()

            }else{
                Toast.makeText(context,"Debe ingresar los datos pedidos", Toast.LENGTH_SHORT).show()
            }

        }*/
        return binding.root
    }
}
