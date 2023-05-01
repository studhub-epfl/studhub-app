package com.studhub.app

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest

class MeetingPointPickerActivity : AppCompatActivity(), OnMapReadyCallback, CoroutineScope{

    private lateinit var mapView: MapView
    private var confirmButton: Button? = null
    private lateinit var searchView: AutoCompleteTextView
    private lateinit var googleMap: GoogleMap
    private var selectedLatLng: LatLng? = null
    private lateinit var searchButton: Button
    private lateinit var location: LatLng
    private var viewOnly = false

    companion object {
        private const val TAG = "MeetingPointPicker"
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1234
    }

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreate(savedInstanceState: Bundle?) {

        val viewOnly = intent.getBooleanExtra("viewOnly", false)

        super.onCreate(savedInstanceState)

        setupSearchBar()

        mapView = MapView(this).apply {
            id = View.generateViewId()
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

        if (!viewOnly) {
            confirmButton = Button(this).apply {
                id = View.generateViewId()
                text = "Confirm Location"
                layoutParams = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
                    addRule(RelativeLayout.ALIGN_PARENT_END)
                    bottomMargin = 16.dpToPx(this@MeetingPointPickerActivity)
                    marginEnd = 16.dpToPx(this@MeetingPointPickerActivity)
                }
            }
        }



        val mapViewFrame = FrameLayout(this).apply {
            id = View.generateViewId()
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            addView(mapView)
        }

        val layout = RelativeLayout(this).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            addView(mapViewFrame) // Change this line to add mapViewFrame instead of mapView
            addView(searchView)
            addView(searchButton)
            if (!viewOnly) {
                addView(confirmButton)
            }
        }

        setContentView(layout)

        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }

    private fun setupSearchBar() {

        // Initialize the Places SDK
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, "AIzaSyCxsRHWa5Wo_wLLq91ndpQmXi3JedpSpZc")
        }

        searchView =  AutoCompleteTextView(this).apply {
            id = View.generateViewId()
            layoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                addRule(RelativeLayout.ALIGN_PARENT_TOP)
                addRule(RelativeLayout.ALIGN_PARENT_START)
                topMargin = 86.dpToPx(this@MeetingPointPickerActivity)
                marginStart = 16.dpToPx(this@MeetingPointPickerActivity)
            }
            hint = "Search location"
            inputType = InputType.TYPE_CLASS_TEXT
            setSingleLine()
            dropDownHeight = ViewGroup.LayoutParams.WRAP_CONTENT
        }

        searchButton = Button(this).apply {
            id = View.generateViewId()
            text = "Search"
            layoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                addRule(RelativeLayout.BELOW, searchView.id)
                addRule(RelativeLayout.ALIGN_PARENT_START)
                topMargin = 16.dpToPx(this@MeetingPointPickerActivity)
                marginStart = 16.dpToPx(this@MeetingPointPickerActivity)
            }
        }

        searchButton.setOnClickListener {
            val query = searchView.text.toString()
            searchLocation(query)
        }

        searchView.threshold = 1

        val autocompleteAdapter =
            ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line)
        searchView.setAdapter(autocompleteAdapter)

        searchView.setOnItemClickListener { _, _, position, _ ->
            val selectedItem =
                autocompleteAdapter.getItem(position) ?: return@setOnItemClickListener
            searchLocation(selectedItem)
        }

        searchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null) {
                    launchPlacesAutocompleteRequest(s.toString(), autocompleteAdapter)
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })


        searchView.inputType = InputType.TYPE_CLASS_TEXT
        searchView.setSingleLine()
        searchView.dropDownHeight = ViewGroup.LayoutParams.WRAP_CONTENT


    }

    private fun launchPlacesAutocompleteRequest(query: String, adapter: ArrayAdapter<String>) {
        Log.d("PRED", "method started")
        val placesClient = Places.createClient(this)

        val request = FindAutocompletePredictionsRequest.builder()
            .setQuery(query)
            .build()

        placesClient.findAutocompletePredictions(request).addOnSuccessListener { response ->
            Log.d("PRED", "Predictions count: ${response.autocompletePredictions.size}")

            adapter.clear()
            response.autocompletePredictions.forEach {
                adapter.add(it.getPrimaryText(null).toString())
            }
            adapter.notifyDataSetChanged()
            searchView.showDropDown()
        }.addOnFailureListener { exception ->
            Log.d("PRED", "Exception $exception")
            Log.e(TAG, "Place not found", exception)
        }.addOnCompleteListener {
            Log.d("PRED", "On complete")
            if (!it.isSuccessful) {
                it.exception?.printStackTrace()
            }
        }
    }

    private fun searchLocation(locationName: String) {
        launch {
            try {
                withTimeout(5000) { // Set a 5-second timeout for the Geocoder request
                    val geocoder = Geocoder(this@MeetingPointPickerActivity)
                    val addresses = geocoder.getFromLocationName(locationName, 1)

                    if (!addresses.isNullOrEmpty()) {
                        val address = addresses[0]
                        val latLng = LatLng(address.latitude, address.longitude)
                        googleMap.clear()
                        googleMap.addMarker(MarkerOptions().position(latLng))
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                    }
                }
            } catch (e: TimeoutCancellationException) {
                // Handle the situation when the request takes too long
                Log.e(TAG, "Geocoding request took too long", e)
            } catch (e: Exception) {
                Log.e(TAG, "Geocoding request failed", e)
            }
        }
    }



    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            setupMap()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun setupMap() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            googleMap.isMyLocationEnabled = true
//            googleMap.uiSettings.isMyLocationButtonEnabled = true
        }

        val initialLatLng = LatLng(0.0, 0.0)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialLatLng, 10f))
        if (confirmButton != null) {
            googleMap.setOnMapClickListener { latLng ->
                googleMap.clear()
                googleMap.addMarker(MarkerOptions().position(latLng))
                selectedLatLng = latLng
                confirmButton?.isEnabled = true
            }

            confirmButton?.setOnClickListener {
                selectedLatLng?.let { latLng ->
                    val resultIntent = Intent().apply {
                        putExtra("location", latLng)
                    }
                    setResult(Activity.RESULT_OK, resultIntent)
                    finish()
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupMap()
            }
        }
    }


    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        mapView.onPause()
        super.onPause()
    }

    override fun onStop() {
        mapView.onStop()
        super.onStop()
    }

    override fun onDestroy() {
        job.cancel()
        mapView.onDestroy()
        super.onDestroy()
    }


    override fun onSaveInstanceState(outState: Bundle) {
        mapView.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}

fun Int.dpToPx(context: Context): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        context.resources.displayMetrics
    ).toInt()
}
