package com.example.poli_gas.feedback

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.poli_gas.R
import com.example.poli_gas.databinding.FragmentFeedbackBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.view.*


class FeedbackFragment : Fragment() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentFeedbackBinding.inflate(inflater)
        val navBottonm = activity!!.findViewById<View>(R.id.container)
        navBottonm.bottomNavigationView.setVisibility(View.VISIBLE)

        binding.sendCommentaryButton.setOnClickListener {

            val comentary =  binding.commentaryTextArea.text.toString()
            val userId = FirebaseAuth.getInstance().uid
            val data = hashMapOf(
                "userId" to userId,
                "commentary" to comentary
            )

            db.collection("commentaries").add(data)


            view!!.findNavController().navigate(FeedbackFragmentDirections.actionFeedbackFragmentToHomeFragment())
        }

        return binding.root
    }

}
