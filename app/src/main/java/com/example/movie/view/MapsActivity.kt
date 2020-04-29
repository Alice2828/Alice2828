package com.example.movie.view

import android.os.Bundle
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.movie.R
import com.example.movie.model.Cinema
import com.example.movie.view_model.LoginViewModel
import com.example.movie.view_model.ViewModelProviderFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.clustering.ClusterManager

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var cinemaMapViewModel: LoginViewModel
    private lateinit var cinemaList: List<Cinema>
    private lateinit var progressBar: ProgressBar
    private lateinit var clusterManager: ClusterManager<MyItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        progressBar = findViewById(R.id.progressBar)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val viewModelProviderFactory = ViewModelProviderFactory(context = this)
        cinemaMapViewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(LoginViewModel::class.java)

        cinemaList = cinemaMapViewModel.getCinemaList()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        setUpClusterer()
        mMap.setOnInfoWindowClickListener(this)
    }

    private fun setUpClusterer() {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(43.238949, 76.889709), 10f))
        clusterManager = ClusterManager(this, mMap)
        mMap.setOnCameraIdleListener(clusterManager)
        mMap.setOnMarkerClickListener(clusterManager)
        addItems()
    }

    private fun addItems() {
        for (cinema in cinemaList) {
            val lat = cinema.latitude
            val lng = cinema.longitude
            val title = cinema.title
            val snippet = ""
            val offsetItem = MyItem(lat, lng, title, snippet)
            clusterManager.addItem(offsetItem)
        }
    }

    override fun onInfoWindowClick(p0: Marker?) {
        Toast.makeText(
            this, "Info window clicked",
            Toast.LENGTH_SHORT
        ).show()
    }
}


