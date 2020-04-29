package com.example.movie.view

import android.os.Bundle
import android.widget.ProgressBar
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.movie.R
import com.example.movie.database.CinemaDao
import com.example.movie.database.CinemaDatabase
import com.example.movie.model.Cinema
import com.example.movie.view_model.CinemaMapViewModel
import com.example.movie.view_model.ViewModelProviderFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : FragmentActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var cinemaMapViewModel: CinemaMapViewModel
    private lateinit var cinemaList: List<Cinema>
    private lateinit var progressBar: ProgressBar



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        progressBar = findViewById(R.id.progressBar)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val viewModelProviderFactory = ViewModelProviderFactory(context = this)
        cinemaMapViewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(CinemaMapViewModel::class.java)

        cinemaList = cinemaMapViewModel.getCinemaList()

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
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        for(cinema in cinemaList) {
            val position = LatLng(cinema.latitude, cinema.longitude)
            mMap.addMarker(MarkerOptions().position(position).title(cinema.title))
            mMap.moveCamera(CameraUpdateFactory.newLatLng(position))

        }
  }
}






        // Add a marker in Sydney and move the camera
//        val sydney = LatLng(-34.0, 151.0)
//        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
////        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
//
//
//        val location1 = LatLng(43.240248,76.9061647)
//        googleMap.addMarker(MarkerOptions().position(location1).title("Bekmambetov Cinema"))
////        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location1))
//
//        val location2 = LatLng(43.262044,76.941684)
//        googleMap.addMarker(MarkerOptions().position(location2).title("Lumiera Cinema"))
////        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location2,10f))
//
//        val location3 = LatLng(43.232963,76.955780)
//        googleMap.addMarker(MarkerOptions().position(location3).title("CINEMAX Dostyk Multiplex"))
////        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location3,10f))
//
//        val location4 = LatLng(43.225303,76.907712)
//        googleMap.addMarker(MarkerOptions().position(location4).title("Kinopark 5 Atakent"))
////        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location4,10))




