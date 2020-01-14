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
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.Layer
import com.mapbox.android.core.permissions.PermissionsManager
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.mapbox.api.geocoding.v5.GeocodingCriteria
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.annotations.IconFactory
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.style.layers.Property
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.mapbox.api.geocoding.v5.MapboxGeocoding
import com.mapbox.api.geocoding.v5.models.GeocodingResponse
import com.mapbox.core.exceptions.ServicesException

class MapFragment :  Fragment(), PermissionsListener, OnMapReadyCallback {

    private var mapView: MapView? = null
    private val DROPPED_MARKER_LAYER_ID = "DROPPED_MARKER_LAYER_ID"
    private var mapboxMap: MapboxMap? = null
    private var selectLocationButton: Button? = null
    private var permissionsManager: PermissionsManager? = null
    private var hoveringMarker: ImageView? = null
    private var droppedMarkerLayer: Layer? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        Mapbox.getInstance(context!!, getString(R.string.mapbox_access_token))

        val binding = FragmentMapBinding.inflate(inflater)
        mapView = binding.mapView
        mapView?.onCreate(savedInstanceState)
        mapView?.getMapAsync(this) /*{ mapboxMap ->

            val icon = IconFactory.getInstance(context!!).fromResource(R.drawable.mapbox_marker_icon_default)
            val latLng = LatLng(-0.2101769, -78.490869)

            // Add the marker to the map
            val marker = MarkerOptions().icon(icon).position(latLng)
            mapboxMap.addMarker(marker)

            mapboxMap.setStyle(Style.MAPBOX_STREETS)

        }*/

        return binding.root
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView!!.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        mapView!!.onDestroy()

    }



    override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {
        Toast.makeText(context, R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show()
    }

    override fun onPermissionResult(granted: Boolean) {
        if (granted && mapboxMap != null) {
            val style = mapboxMap!!.style
            if (style != null) {
                enableLocationPlugin(style)
            }
        } else {
            Toast.makeText(context, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show()
        }
    }


    override fun onMapReady(mapboxMap: MapboxMap) {
        this@MapFragment.mapboxMap = mapboxMap
        mapboxMap.setStyle(Style.MAPBOX_STREETS) { style ->
            enableLocationPlugin(style)

            // Toast instructing user to tap on the mapboxMap
            Toast.makeText(context, getString(R.string.move_map_instruction), Toast.LENGTH_SHORT
            ).show()

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
            selectLocationButton = view!!.findViewById(R.id.select_location_button)
            selectLocationButton!!.setOnClickListener {
                if (hoveringMarker!!.visibility == View.VISIBLE) {

                    // Use the map target's coordinates to make a reverse geocoding search
                    val mapTargetLatLng = mapboxMap.cameraPosition.target

                    // Hide the hovering red hovering ImageView marker
                    hoveringMarker!!.visibility = View.INVISIBLE

                    // Transform the appearance of the button to become the cancel button
                    selectLocationButton!!.setBackgroundColor(
                        ContextCompat.getColor(context!!, R.color.colorAccent)
                    )
                    selectLocationButton!!.text = getString(R.string.location_picker_select_location_button_cancel)

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

                    // Use the map camera target's coordinates to make a reverse geocoding search
                    reverseGeocode(
                        Point.fromLngLat(
                            mapTargetLatLng.longitude,
                            mapTargetLatLng.latitude
                        )
                    )

                } else {

                    // Switch the button appearance back to select a location.
                    selectLocationButton!!.setBackgroundColor(
                        ContextCompat.getColor(context!!, R.color.colorPrimary)
                    )
                    selectLocationButton!!.text = getString(R.string.location_picker_select_location_button_select)

                    // Show the red hovering ImageView marker
                    hoveringMarker!!.visibility = View.VISIBLE

                    // Hide the selected location SymbolLayer
                    droppedMarkerLayer = style.getLayer(MapFragment.DROPPED_MARKER_LAYER_ID)
                    if (droppedMarkerLayer != null) {
                        droppedMarkerLayer!!.setProperties(PropertyFactory.visibility(Property.NONE))
                    }
                }
            }
        }
    }

    private fun initDroppedMarker(loadedMapStyle: Style) {
        // Add the marker image to map
        loadedMapStyle.addImage(
            "dropped-icon-image", BitmapFactory.decodeResource(
                resources, R.drawable.mapbox_marker_icon_default
            )
        )
        loadedMapStyle.addSource(GeoJsonSource("dropped-marker-source-id"))
        loadedMapStyle.addLayer(
            SymbolLayer(
                MapFragment.DROPPED_MARKER_LAYER_ID,
                "dropped-marker-source-id"
            ).withProperties(
                PropertyFactory.iconImage("dropped-icon-image"),
                PropertyFactory.visibility(Property.NONE),
                PropertyFactory.iconAllowOverlap(true),
                PropertyFactory.iconIgnorePlacement(true)
            )
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        permissionsManager!!.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    private fun enableLocationPlugin(loadedMapStyle: Style) {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(context)) {

            // Get an instance of the component. Adding in LocationComponentOptions is also an optional
            // parameter
            val locationComponent = mapboxMap!!.locationComponent
            locationComponent.activateLocationComponent(
                LocationComponentActivationOptions.builder(
                    context!!, loadedMapStyle
                ).build()
            )
            locationComponent.isLocationComponentEnabled = true

            // Set the component's camera mode
            locationComponent.cameraMode = CameraMode.TRACKING
            locationComponent.renderMode = RenderMode.NORMAL

        } else {
            permissionsManager = PermissionsManager(this)
            permissionsManager!!.requestLocationPermissions(this.activity)
        }
    }

    private fun reverseGeocode(point: Point) {
        try {
            val client = MapboxGeocoding.builder()
                .accessToken(getString(R.string.mapbox_access_token))
                .query(Point.fromLngLat(point.longitude(), point.latitude()))
                .geocodingTypes(GeocodingCriteria.TYPE_ADDRESS)
                .build()

            client.enqueueCall(object : Callback<GeocodingResponse> {
                override fun onResponse(
                    call: Call<GeocodingResponse>,
                    response: Response<GeocodingResponse>
                ) {

                    if (response.body() != null) {
                        val results = response.body()!!.features()
                        if (results.size > 0) {
                            val feature = results.get(0)

                            // If the geocoder returns a result, we take the first in the list and show a Toast with the place name.
                            mapboxMap!!.getStyle { style ->
                                if (style.getLayer(DROPPED_MARKER_LAYER_ID) != null) {
                                    Toast.makeText(
                                        context,
                                        String.format(
                                            getString(R.string.location_picker_place_name_result),
                                            feature.placeName()
                                        ), Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }

                        } else {
                            Toast.makeText(
                                context,
                                getString(R.string.location_picker_dropped_marker_snippet_no_results),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

                override fun onFailure(call: Call<GeocodingResponse>, throwable: Throwable) {
                    Log.i("MapFragment", throwable.message)
                }
            })
        } catch (servicesException: ServicesException) {
            Log.i("MapFragment","Error geocoding: ${servicesException.toString()}")
            servicesException.printStackTrace()
        }

    }

    companion object {

        private val DROPPED_MARKER_LAYER_ID = "DROPPED_MARKER_LAYER_ID"
    }


}
