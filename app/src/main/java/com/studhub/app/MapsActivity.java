package com.studhub.app;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.studhub.app.databinding.ActivityMapsBinding;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Move camera to EPFL location
        LatLng epfl = new LatLng(46.520536, 6.568318);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(epfl, 15f));

        // Add a customized marker at Satellite bar location
        LatLng satellite = new LatLng(46.520544, 6.567825);
        MarkerOptions markerOptions = new MarkerOptions()
                .position(satellite)
                .title("Satellite")
                .alpha(0.8f);
        Marker marker = mMap.addMarker(markerOptions);

        // Convert longitude and latitude coordinates to an address
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(46.520536, 6.568318, 1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String address = addresses.get(0).getAddressLine(0);

        // Add a listener to the click of the information window of the marker
        if (marker != null) {
            marker.setTag(new LatLng(46.520544, 6.567825));
            mMap.setOnMarkerClickListener(marker1 -> {
                LatLng position = (LatLng) marker1.getTag();
                if (position != null) {
                    // Print a message with the coordinates of the Marker
                    Toast.makeText(MapsActivity.this, "Marker clicked at: " +
                            address, Toast.LENGTH_SHORT).show();
                }
                return false;
            });
        }

        // Limit the zoom and set a bound that the map cannot cross
        LatLngBounds bounds = new LatLngBounds(
                new LatLng(46.515086, 6.561732),       // Southwest corner
                new LatLng(46.526764, 6.578053));      // Northeast corner
        mMap.setLatLngBoundsForCameraTarget(bounds);
        mMap.setMinZoomPreference(14.0f);
        mMap.setMaxZoomPreference(18.0f);
    }
}