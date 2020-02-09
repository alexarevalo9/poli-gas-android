package com.example.poli_gas.mapnavigation

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.poli_gas.R
import com.mapbox.api.directions.v5.models.DirectionsResponse
import com.mapbox.api.directions.v5.models.DirectionsRoute
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.services.android.navigation.ui.v5.NavigationView
import com.mapbox.services.android.navigation.ui.v5.NavigationViewOptions
import com.mapbox.services.android.navigation.ui.v5.OnNavigationReadyCallback
import com.mapbox.services.android.navigation.ui.v5.listeners.NavigationListener
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute
import com.mapbox.services.android.navigation.v5.routeprogress.ProgressChangeListener
import com.mapbox.services.android.navigation.v5.routeprogress.RouteProgress
import kotlinx.android.synthetic.main.activity_main.view.*

import retrofit2.Call
import retrofit2.Response

class NavigationMapFragment : Fragment(), OnNavigationReadyCallback, NavigationListener,
    ProgressChangeListener {

    private var navigationView: NavigationView? = null
    private var directionsRoute: DirectionsRoute? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Mapbox.getInstance(context!!, getString(R.string.mapbox_access_token))
        val binding = inflater.inflate(R.layout.fragment_navigation_map, container, false)
        val navBottonm = activity!!.findViewById<View>(R.id.container)
        navBottonm.bottomNavigationView.setVisibility(View.GONE)
        return binding
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateNightMode()
        navigationView = view.findViewById(R.id.navigation_view_fragment)
        navigationView!!.onCreate(savedInstanceState)
        navigationView!!.initialize(this)
    }


    override fun onStart() {
        super.onStart()
        navigationView!!.onStart()
    }

    override fun onResume() {
        super.onResume()
        navigationView!!.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        navigationView!!.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            navigationView!!.onRestoreInstanceState(savedInstanceState)
        }
    }

    override fun onPause() {
        super.onPause()
        navigationView!!.onPause()
    }

    override fun onStop() {
        super.onStop()
        navigationView!!.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        navigationView!!.onLowMemory()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        navigationView!!.onDestroy()
    }

    override fun onNavigationReady(isRunning: Boolean) {
        val origin = Point.fromLngLat(ORIGIN_LONGITUDE, ORIGIN_LATITUDE)
        val destination = Point.fromLngLat(DESTINATION_LONGITUDE, DESTINATION_LATITUDE)
        fetchRoute(origin, destination)
    }

    override fun onCancelNavigation() {
        navigationView!!.stopNavigation()
        stopNavigation()
    }

    override fun onNavigationFinished() {
        view!!.findNavController().navigate(NavigationMapFragmentDirections.actionNavigationMapFragmentToProgressRequestFragment(true))
    }

    override fun onNavigationRunning() {
       // view!!.findNavController().navigate(FragmentNa)
    }

    override fun onProgressChange(location: Location, routeProgress: RouteProgress) {
        val isInTunnel = routeProgress.inTunnel()
        val wasInTunnel = wasInTunnel()
        if (isInTunnel) {
            if (!wasInTunnel) {
                updateWasInTunnel(true)
                updateCurrentNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        } else {
            if (wasInTunnel) {
                updateWasInTunnel(false)
                updateCurrentNightMode(AppCompatDelegate.MODE_NIGHT_AUTO)
            }
        }
    }

    @SuppressLint("WrongConstant")
    private fun updateNightMode() {
        if (wasNavigationStopped()) {
            updateWasNavigationStopped(false)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO)
            getActivity()?.recreate()
        }
    }

    private fun fetchRoute(origin: Point, destination: Point) {
        NavigationRoute.builder(getContext())
            .accessToken(Mapbox.getAccessToken()!!)
            .origin(origin)
            .destination(destination)
            .build()
            .getRoute(object : SimplifiedCallback() {
                override fun onResponse(
                    call: Call<DirectionsResponse>,
                    response: Response<DirectionsResponse>
                ) {
                    directionsRoute = response.body()!!.routes()[0]
                    startNavigation()
                }
            })
    }

    private fun startNavigation() {
        if (directionsRoute == null) {
            return
        }
        val options = NavigationViewOptions.builder()
            .directionsRoute(directionsRoute)
            .shouldSimulateRoute(true)
            .navigationListener(this@NavigationMapFragment)
            .progressChangeListener(this)
            .build()
        navigationView!!.startNavigation(options)
    }

    private fun stopNavigation() {
        val activity = getActivity()
        if (activity != null) {
            updateWasNavigationStopped(true)
            updateWasInTunnel(false)
        }
    }

    private fun wasInTunnel(): Boolean {
        val context = getActivity()
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getBoolean(context?.getString(R.string.was_in_tunnel), false)
    }

    private fun updateWasInTunnel(wasInTunnel: Boolean) {
        val context = getActivity()
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit()
        editor.putBoolean(context?.getString(R.string.was_in_tunnel), wasInTunnel)
        editor.apply()
    }

    private fun updateCurrentNightMode(nightMode: Int) {
        AppCompatDelegate.setDefaultNightMode(nightMode)
        getActivity()?.recreate()
    }

    private fun wasNavigationStopped(): Boolean {
        val context = getActivity()
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getBoolean(getString(R.string.was_navigation_stopped), false)
    }

    fun updateWasNavigationStopped(wasNavigationStopped: Boolean) {
        val context = getActivity()
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit()
        editor.putBoolean(getString(R.string.was_navigation_stopped), wasNavigationStopped)
        editor.apply()
    }

    companion object {

        private val ORIGIN_LONGITUDE = -78.4823047
        private val ORIGIN_LATITUDE = -0.1926335
        private val DESTINATION_LONGITUDE = -78.4826116
        private val DESTINATION_LATITUDE = -0.193841
    }
}
