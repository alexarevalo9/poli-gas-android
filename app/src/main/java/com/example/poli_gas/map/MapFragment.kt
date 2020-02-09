package com.example.poli_gas.map

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.poli_gas.R
import com.example.poli_gas.databinding.FragmentMapBinding
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.Layer
import com.mapbox.android.core.permissions.PermissionsManager
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.style.layers.Property
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import kotlinx.android.synthetic.main.activity_main.view.*

class MapFragment :  Fragment(), PermissionsListener {

    private var mapView: MapView? = null
    private var map: MapboxMap? = null
    private var permissionsManager: PermissionsManager? = null
    private var hoveringMarker: ImageView? = null
    private var droppedMarkerLayer: Layer? = null
    private var latLong : Array<String>? = null
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        Mapbox.getInstance(context!!, getString(R.string.mapbox_access_token))

        val binding = FragmentMapBinding.inflate(inflater)
        val navBottonm = activity!!.findViewById<View>(R.id.container)
        navBottonm.bottomNavigationView.setVisibility(View.GONE)
        mapView = binding.mapView
        mapView?.onCreate(savedInstanceState)
        mapView?.getMapAsync{mapboxMap ->

            mapboxMap.setStyle(Style.MAPBOX_STREETS){style ->

                map = mapboxMap
                enableLocationPlugin(style, mapboxMap)

                map = mapboxMap

                // When user is still picking a location, we hover a marker above the mapboxMap in the center.
                // This is done by using an image view with the default marker found in the SDK. You can
                // swap out for your own marker image, just make sure it matches up with the dropped marker.
                hoveringMarker = ImageView(context)
                hoveringMarker!!.setImageResource(R.drawable.mapbox_marker_icon_default)
                val params = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER
                )
                hoveringMarker!!.layoutParams = params
                mapView!!.addView(hoveringMarker)

                // Initialize, but don't show, a SymbolLayer for the marker icon which will represent a selected location.
                initDroppedMarker(style)

                // Button for user to drop marker or to pick marker back up.
                binding.selectLocationButton!!.setOnClickListener {
                    if (hoveringMarker!!.visibility == View.VISIBLE) {

                        // Use the map target's coordinates to make a reverse geocoding search
                        val mapTargetLatLng = mapboxMap!!.cameraPosition.target

                        // Hide the hovering red hovering ImageView marker
                        hoveringMarker!!.visibility = View.INVISIBLE

                        // Transform the appearance of the button to become the cancel button
                        binding.selectLocationButton!!.setBackgroundColor(
                            ContextCompat.getColor(context!!, R.color.colorAccent)
                        )
                        binding.selectLocationButton!!.text = getString(R.string.location_picker_select_location_button_cancel)

                        // Show the SymbolLayer icon to represent the selected map location
                        if (style.getLayer(MapFragment.DROPPED_MARKER_LAYER_ID) != null) {
                            val source = style.getSourceAs<GeoJsonSource>("dropped-marker-source-id")
                            source?.setGeoJson(
                                Point.fromLngLat(
                                    mapTargetLatLng.longitude,
                                    mapTargetLatLng.latitude
                                )
                            )
                            droppedMarkerLayer = style.getLayer(MapFragment.DROPPED_MARKER_LAYER_ID)
                            if (droppedMarkerLayer != null) {
                                droppedMarkerLayer!!.setProperties(PropertyFactory.visibility(Property.VISIBLE))
                            }
                        }

                        latLong = arrayOf(mapTargetLatLng.latitude.toString(),mapTargetLatLng.longitude.toString())

                    } else {

                        // Switch the button appearance back to select a location.
                        binding.selectLocationButton.setBackgroundColor(
                            ContextCompat.getColor(context!!, R.color.colorPrimary)
                        )
                        binding.selectLocationButton.text = getString(R.string.location_picker_select_location_button_select)

                        // Show the red hovering ImageView marker
                        hoveringMarker!!.visibility = View.VISIBLE

                        // Hide the selected location SymbolLayer
                        droppedMarkerLayer = style.getLayer(MapFragment.DROPPED_MARKER_LAYER_ID)
                        if (droppedMarkerLayer != null) {
                            droppedMarkerLayer!!.setProperties(PropertyFactory.visibility(Property.NONE))
                        }
                    }
                }

                binding.loadLocalStyleButton.setOnClickListener{
                    if(latLong?.get(0) != null && latLong?.get(1) != null){
                        saveCoordinates()
                        view!!.findNavController().navigate(MapFragmentDirections.actionMapFragmentToHomeFragment())
                    }else{
                        Toast.makeText(context, "Debe Elegir una Ubicación", Toast.LENGTH_LONG).show()
                    }

                }

            }

        }


        return binding.root
    }

    private fun saveCoordinates(){

        if(latLong?.get(0) != null && latLong?.get(1) != null){
            val coordinates = hashMapOf(
                "latitud" to latLong?.get(0),
                "longitud" to latLong?.get(1)
            )

            FirebaseAuth.getInstance().uid?.let { UID ->
                db.collection("coordinates").document(UID)
                    .set(coordinates)
                    .addOnSuccessListener { Log.i("MapFragmentS", "DocumentSnapshot successfully written!") }
                    .addOnFailureListener { e -> Log.i("MapFragmentS", "Error writing document", e) }
            }
        }

    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }
/*
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView!!.onSaveInstanceState(outState)
    }
*/
    override fun onDestroyView() {
        super.onDestroyView()

        mapView!!.onDestroy()

    }

    override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {
        Toast.makeText(context, R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show()
    }

    override fun onPermissionResult(granted: Boolean) {
        if (granted && map != null) {
            val style = map!!.style
            if (style != null) {
                enableLocationPlugin(style, map!!)
            }
        } else {
            Toast.makeText(context, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show()
        }
    }


    private fun initDroppedMarker(loadedMapStyle: Style) {
        // Add the marker image to map
        loadedMapStyle.addImage("dropped-icon-image", BitmapFactory.decodeResource(resources, R.drawable.mapbox_marker_icon_default))
        loadedMapStyle.addSource(GeoJsonSource("dropped-marker-source-id"))
        loadedMapStyle.addLayer(SymbolLayer(MapFragment.DROPPED_MARKER_LAYER_ID, "dropped-marker-source-id").withProperties(
                PropertyFactory.iconImage("dropped-icon-image"),
                PropertyFactory.visibility(Property.NONE),
                PropertyFactory.iconAllowOverlap(true),
                PropertyFactory.iconIgnorePlacement(true)
            )
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        permissionsManager!!.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    private fun enableLocationPlugin(loadedMapStyle: Style, mapboxMap : MapboxMap) {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(context)) {

            // Get an instance of the component. Adding in LocationComponentOptions is also an optional
            // parameter
            val locationComponent = mapboxMap!!.locationComponent
            locationComponent.activateLocationComponent(LocationComponentActivationOptions.builder(context!!, loadedMapStyle).build())
            locationComponent.isLocationComponentEnabled = true

            // Set the component's camera mode
            locationComponent.cameraMode = CameraMode.TRACKING
            locationComponent.renderMode = RenderMode.NORMAL

        } else {
            permissionsManager = PermissionsManager(this)
            permissionsManager!!.requestLocationPermissions(this.activity)
        }
    }

    companion object {

        private val DROPPED_MARKER_LAYER_ID = "DROPPED_MARKER_LAYER_ID"
    }


}
