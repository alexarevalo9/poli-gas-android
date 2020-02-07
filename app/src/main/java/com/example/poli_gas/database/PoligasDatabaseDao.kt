package com.example.poli_gas.database

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.poli_gas.type_request.TypeRequestFragmentDirections
import com.google.firebase.firestore.FirebaseFirestore

class PoligasDatabaseDao {

    private val db = FirebaseFirestore.getInstance()

    fun insertScheduleOrder(order: PoliGas, context: Context?){

        db.collection("scheduleorder")
            .add(order)
            .addOnSuccessListener {
                Toast.makeText(context, "Su pedido ha sido agendado con Exito!!", Toast.LENGTH_LONG)
                    .show()
            }
            .addOnFailureListener {
                Toast.makeText(
                    context,
                    "No se ha podido agendar su pedido intentelo mas tarde",
                    Toast.LENGTH_LONG
                ).show()
            }
    }

    fun insertExpressOrder(order: PoliGas, context: Context?, view : View?) {

        db.collection("express")
            .add(order)
            .addOnSuccessListener {
                view!!.findNavController().navigate(TypeRequestFragmentDirections.actionTypeRequestFragmentToProgressRequestFragment(false))

            }
            .addOnFailureListener {
                Toast.makeText(
                    context,
                    "No se ha podido realizar su pedido intentelo mas tarde",
                    Toast.LENGTH_LONG
                ).show()
            }
    }

}

