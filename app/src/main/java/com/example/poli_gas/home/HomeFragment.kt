package com.example.poli_gas.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.poli_gas.R
import com.example.poli_gas.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jama.carouselview.CarouselScrollListener
import com.jama.carouselview.enums.IndicatorAnimationType
import com.jama.carouselview.enums.OffsetType
import kotlinx.android.synthetic.main.activity_main.view.*

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeBinding.inflate(inflater)
        val change = activity!!.findViewById<View>(R.id.container)
        change.bottomNavigationView.setVisibility(View.VISIBLE)
        // Get a reference to the ViewModel associated with this fragment.
        val homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)

        var typeCylinder = 0

        binding.homeViewModel = homeViewModel

        binding.setLifecycleOwner(this)

        binding.makeOrderButton.setOnClickListener {
            view!!.findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToTypeRequestFragment(
                    typeCylinder,
                    homeViewModel.quantity_cilynders.value.toString()
                )
            )
        }

        // Add an Observer on the state variable for showing a Toast message
        homeViewModel.showToastEvent.observe(this, Observer {
            if (it == 3) { // Observed state is true.
                Toast.makeText(context, "Numero maximo de cilindros alcanzados", Toast.LENGTH_SHORT)
                    .show()
                homeViewModel.doneShowingToast()
            }

            if (it == 1) {
                Toast.makeText(context, "Numero minimo de cilindros alcanzados", Toast.LENGTH_SHORT)
                    .show()
                homeViewModel.doneShowingToast()
            }
        })


        val images =
            arrayListOf(R.drawable.gasindustrial, R.drawable.gasazul, R.drawable.gasamarillo)

        val scrollListener = object : CarouselScrollListener {
            override fun onScrollStateChanged(
                recyclerView: RecyclerView,
                newState: Int,
                position: Int
            ) {
                typeCylinder = position + 1
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {}
        }

        binding.carousel.apply {
            size = images.size
            resource = R.layout.carousel_item
            indicatorAnimationType = IndicatorAnimationType.THIN_WORM
            carouselOffset = OffsetType.CENTER
            setCarouselViewListener { view, position ->
                // Example here is setting up a full image carousel
                val imageView = view.findViewById<ImageView>(R.id.imageView)
                imageView.setImageDrawable(resources.getDrawable(images[position]))
            }
            carouselScrollListener = scrollListener
            // After you finish setting up, show the CarouselView
            show()
        }

        return binding.root
    }

}
