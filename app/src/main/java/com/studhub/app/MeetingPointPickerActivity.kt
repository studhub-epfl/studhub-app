package com.studhub.app

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
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
import androidx.test.espresso.idling.CountingIdlingResource
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.studhub.app.BuildConfig.MAPS_API_KEY
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import kotlin.coroutines.CoroutineContext

class MeetingPointPickerActivity : AppCompatActivity(), OnMapReadyCallback, CoroutineScope {

    private lateinit var mapView: MapView
    private var confirmButton: Button? = null
    private lateinit var searchView: AutoCompleteTextView
    private lateinit var googleMap: GoogleMap
    private var selectedLatLng: LatLng? = null
    private lateinit var searchButton: Button
    private var viewOnly = false

    val idlingResourceSearchLocation = CountingIdlingResource("Search")
    val idlingResourceMapClick = CountingIdlingResource("MapClick")
    val idlingResourceConfirmButton = CountingIdlingResource("ConfirmButton")

    companion object {
        private const val TAG = "MeetingPointPicker"
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1234
    }

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreate(savedInstanceState: Bundle?) {
        viewOnly = intent.getBooleanExtra("viewOnly", false)
        super.onCreate(savedInstanceState)

        setupSearchBar()
        createMapView()
        createConfirmButton()

        val layout = createLayout()

        setContentView(layout)
        initializeMapView(savedInstanceState)
    }

    private fun setupSearchBar() {

        // Initialize the Places SDK
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, MAPS_API_KEY)
        }

        searchView = AutoCompleteTextView(this).apply {
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

        searchView.visibility = if (viewOnly) View.GONE else View.VISIBLE
        searchButton.visibility = if (viewOnly) View.GONE else View.VISIBLE

        searchButton.setOnClickListener {
            if (!viewOnly) {
                val query = searchView.text.toString()
                searchLocation(query)
            }
        }

        searchView.threshold = 1

        val autocompleteAdapter =
            ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line)
        searchView.setAdapter(autocompleteAdapter)

        searchView.setOnItemClickListener { _, _, position, _ ->
            if (!viewOnly) {
                val selectedItem =
                    autocompleteAdapter.getItem(position) ?: return@setOnItemClickListener
                searchLocation(selectedItem)
            }
        }


        searchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!viewOnly && s != null) {
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

    private fun createMapView() {
        mapView = MapView(this).apply {
            id = View.generateViewId()
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
    }

    private fun createConfirmButton() {
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
    }

    private fun createLayout(): RelativeLayout {
        val mapViewFrame = FrameLayout(this).apply {
            id = View.generateViewId()
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            addView(mapView)
        }

        return RelativeLayout(this).apply {
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
    }

    private fun initializeMapView(savedInstanceState: Bundle?) {
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }

    private fun launchPlacesAutocompleteRequest(query: String, adapter: ArrayAdapter<String>) {
        val placesClient = Places.createClient(this)

        val request = FindAutocompletePredictionsRequest.builder()
            .setQuery(query)
            .build()

        placesClient.findAutocompletePredictions(request).addOnSuccessListener { response ->
            adapter.clear()
            response.autocompletePredictions.forEach {
                adapter.add(it.getPrimaryText(null).toString())
            }
            adapter.notifyDataSetChanged()
            searchView.showDropDown()
        }.addOnFailureListener {
        }.addOnCompleteListener {
            if (!it.isSuccessful) {
                it.exception?.printStackTrace()
            }
        }
    }

    private fun searchLocation(locationName: String) {
        idlingResourceSearchLocation.increment()
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
                        // Enable the confirm button
                        selectedLatLng = latLng
                        confirmButton?.post {
                            idlingResourceConfirmButton.increment()
                            confirmButton?.isEnabled = true
                            idlingResourceConfirmButton.decrement()
                        }
                    }
                }
            } catch (e: TimeoutCancellationException) {
                // Handle the situation when the request takes too long
//                Log.e(TAG, "Geocoding request took too long", e)
            } catch (e: Exception) {
//                Log.e(TAG, "Geocoding request failed", e)
            } finally {
                idlingResourceSearchLocation.decrement()
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

        val initialLatitude = intent.getDoubleExtra("latitude", 0.0)
        val initialLongitude = intent.getDoubleExtra("longitude", 0.0)

        if (initialLatitude != 0.0 && initialLongitude != 0.0) {
            val initialLatLng = LatLng(initialLatitude, initialLongitude)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialLatLng, 15f))
            googleMap.addMarker(MarkerOptions().position(initialLatLng))
        } else {
            val initialLatLng = LatLng(0.0, 0.0)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialLatLng, 10f))
        }
    }


    private fun setupMap() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            googleMap.isMyLocationEnabled = true
        }

        val initialLatLng = LatLng(0.0, 0.0)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialLatLng, 10f))
        if (confirmButton != null) {
            googleMap.setOnMapClickListener { latLng ->
                // Increment the counter before the map click action
                idlingResourceMapClick.increment()
                googleMap.clear()
                googleMap.addMarker(MarkerOptions().position(latLng))
                selectedLatLng = latLng
                idlingResourceConfirmButton.increment()
                confirmButton?.isEnabled = true
                idlingResourceConfirmButton.decrement()
                // Decrement the counter after the map click action
                idlingResourceMapClick.decrement()
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

private fun Int.dpToPx(context: Context): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        context.resources.displayMetrics
    ).toInt()
}
