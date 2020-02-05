package com.example.poli_gas.schedule_request

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.poli_gas.R
import com.example.poli_gas.databinding.FragmentScheduleRequestBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class ScheduleRequestFragment : Fragment() {

    private val db = FirebaseFirestore.getInstance()
    private lateinit var data : ScheduleRequestFragmentArgs
    private lateinit var binding: FragmentScheduleRequestBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentScheduleRequestBinding.inflate(inflater)

        data = arguments?.let { ScheduleRequestFragmentArgs.fromBundle(it) }!!

        when (data?.typeCylinder) {
            1 -> binding.cylinderView.setImageResource(R.drawable.gasindustrial)
            2 -> binding.cylinderView.setImageResource(R.drawable.gasazul)
            3 -> binding.cylinderView.setImageResource(R.drawable.gasamarillo)
            else -> binding.cylinderView.setImageResource(R.drawable.gasindustrial)
        }

        data?.totalCylinder.let {
            binding.quantityText.text = it
        }

        binding.scheduleOrderButton.setOnClickListener {
            saveOrder()
        }

        binding.datePickerButton.setOnClickListener {
            val cal = Calendar.getInstance()
            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH)
            val day = cal.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog =
                this.context?.let { it1 ->
                    DatePickerDialog(it1, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                        binding.deliveryDateText.setText("" + dayOfMonth + "/" + (month+1) + "/" + year)
                    },year,month,day)
                }
            if (datePickerDialog != null) {
                datePickerDialog.show()
            }
            }

        binding.timePickerButton.setOnClickListener {
            val cal = Calendar.getInstance()

            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY,hour)
                cal.set(Calendar.MINUTE,minute)

                binding.deliveryHourText.text = SimpleDateFormat("HH:mm").format(cal.time)
            }
            TimePickerDialog(this.context, timeSetListener, cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE), true).show()
        }


        return binding.root
    }


    private fun saveOrder(){

        val userUID = FirebaseAuth.getInstance().uid

        val dataOrder = hashMapOf(
            "userUid" to userUID,
            "typeCylinder" to data?.typeCylinder,
            "totalCylinder" to data?.totalCylinder,
            "date" to binding.deliveryDateText.text,
            "hour" to binding.deliveryHourText.text
        )

        db.collection("scheduleorder")
            .add(dataOrder)
            .addOnSuccessListener {
                Log.i("ScheduleRequest", "DocumentSnapshot successfully written!")
                Toast.makeText(context, "Su pedido ha sido agendado con Exito!!", Toast.LENGTH_LONG).show()
                view!!.findNavController().navigate(ScheduleRequestFragmentDirections.actionScheduleRequestFragmentToProgressRequestFragment())
            }
            .addOnFailureListener {
                    e -> Log.i("ScheduleRequest", "Error writing document", e)
                Toast.makeText(context, "No se ha podido agendar su pedido intentelo mas tarde", Toast.LENGTH_LONG).show()

            }
    }
    private fun getOrder(){
        FirebaseAuth.getInstance().uid?.let { UID ->

            db.collection("scheduleorder")
                .whereEqualTo("userUid", UID)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        Log.i("progressRequest", "${document.id} => ${document.data}")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.i("progressRequest", "Error getting documents: ", exception)
                }
        }
    }
}
