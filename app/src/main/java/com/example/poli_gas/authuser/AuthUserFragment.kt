package com.example.poli_gas.authuser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.example.poli_gas.R
import com.example.poli_gas.databinding.FragmentAuthUserBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.activity_main.view.*
import java.util.concurrent.TimeUnit

class AuthUserFragment : Fragment() {

    private var verificationId : String? = null
    lateinit var binding : FragmentAuthUserBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentAuthUserBinding.inflate(inflater)
        val navBottonm = activity!!.findViewById<View>(R.id.container)
        navBottonm.bottomNavigationView.setVisibility(View.GONE)
        auth = FirebaseAuth.getInstance()
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.layoutPhone.visibility = View.VISIBLE
        binding.layoutVerification.visibility = View.GONE


        binding.buttonSendVerification.setOnClickListener {

            val phone = binding.editTextPhone.text.toString().trim()

            if (phone.isEmpty() || phone.length != 9) {
                binding.editTextPhone.error = "Enter a valid phone"
                binding.editTextPhone.requestFocus()
                return@setOnClickListener
            }



            val phoneNumber = '+' + binding.ccp.selectedCountryCode + phone

            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    phoneNumber,
                    60,
                    TimeUnit.SECONDS,
                    this.requireActivity(),
                    phoneAuthCallbacks
                )


            binding.layoutPhone.visibility = View.GONE
            binding.layoutVerification.visibility = View.VISIBLE
        }

        binding.buttonVerify.setOnClickListener {
            val code = binding.editTextCode.text.toString().trim()

            if(code.isEmpty()){
                binding.editTextCode.error = "Code required"
                binding.editTextCode.requestFocus()
                return@setOnClickListener
            }


            verificationId?.let{
                val credential = PhoneAuthProvider.getCredential(it, code)
                addPhoneNumber(credential)
            }
        }
    }


    private val phoneAuthCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
            phoneAuthCredential.let {
                addPhoneNumber(phoneAuthCredential)
            }
        }

        override fun onVerificationFailed(exception: FirebaseException) {
            Toast.makeText(context, exception?.message!! ,Toast.LENGTH_SHORT).show()
        }


        override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
            super.onCodeSent(verificationId, token)
            this@AuthUserFragment.verificationId = verificationId
        }

    }

    override fun onStart() {
        super.onStart()
        auth.currentUser?.let {
            view!!.findNavController().navigate(AuthUserFragmentDirections.actionAuthUserFragmentToMapFragment())
        }
    }

    private fun addPhoneNumber(phoneAuthCredential: PhoneAuthCredential) {
        auth.signInWithCredential(phoneAuthCredential)
            .addOnCompleteListener(this.requireActivity()){task ->

                if (task.isSuccessful) {
                    val user = task.result?.user
                    Toast.makeText(context,"Usuario Registrado Exitosamente!!" ,Toast.LENGTH_LONG).show()
                    view!!.findNavController().navigate(AuthUserFragmentDirections.actionAuthUserFragmentToMapFragment())
                } else {
                    Toast.makeText(context,task.exception?.message!! ,Toast.LENGTH_LONG).show()
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(context,task.exception?.message!! ,Toast.LENGTH_LONG).show()
                    }
                }

            }
        /*
        FirebaseAuth.getInstance().currentUser?.updatePhoneNumber(phoneAuthCredential)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context,"Phone added", Toast.LENGTH_SHORT).show()
                view!!.findNavController().navigate(AuthUserFragmentDirections.actionAuthUserFragmentToFeedbackFragment())

                //val action = AuthUserFragmentDirections.actionPhoneVerified()
                //Navigation.findNavController(button_verify).navigate(action)
            } else {
                Toast.makeText(context,task.exception?.message!! ,Toast.LENGTH_SHORT).show()
            }
        }*/
    }
}
