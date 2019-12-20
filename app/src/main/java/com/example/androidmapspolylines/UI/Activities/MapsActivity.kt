package com.example.androidmapspolylines.UI.Activities

import android.annotation.SuppressLint
import android.graphics.Color
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.androidmapspolylines.R
import com.example.androidmapspolylines.Models.Ride
import com.example.androidmapspolylines.Presentation.MapPresenter
import com.example.androidmapspolylines.Presentation.MapPresenterImpl
import com.example.androidmapspolylines.UI.Interfaces.BottomSheetInterface
import com.example.androidmapspolylines.UI.Fragments.BottomFragmentUserPreferences
import com.example.androidmapspolylines.UI.Interfaces.MapInterface
import com.example.androidmapspolylines.UI.Fragments.BottomFragmentSpecialReq
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_maps.*

import kotlinx.android.synthetic.main.activity_request_permission.*

class MapsActivity :
    AppCompatActivity()
    , BottomSheetInterface
    , MapInterface
    , OnMapReadyCallback {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mMap: GoogleMap

    private lateinit var bottomSheetDialogFragment: BottomSheetDialogFragment
    private var presenter: MapPresenter?=null
    private var currentLocation: LatLng?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = MapPresenterImpl(this)
        validatePermission()
    }

    private fun validatePermission() {
        Dexter.withActivity(this)
            .withPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    setContentView(R.layout.activity_maps)
                    val mapFragment = supportFragmentManager
                        .findFragmentById(R.id.map) as SupportMapFragment
                    mapFragment.getMapAsync(this@MapsActivity)

                    fusedLocationClient = LocationServices.getFusedLocationProviderClient(this@MapsActivity)
                    bottomSheetDialogFragment = BottomFragmentUserPreferences()

                    fab_request_ride.setOnClickListener{
                        ll_trip_found.visibility = View.GONE
                        mMap.clear()
                        mMap.addMarker(MarkerOptions().icon(
                            BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)
                        ).position(currentLocation!!).title("U are here"))
                        bottomSheetDialogFragment.show(supportFragmentManager,"Dialog")
                    }
                }

                override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken?) {
                    AlertDialog.Builder(this@MapsActivity)
                        .setTitle("Permissions")
                        .setMessage("This app need use gps to get your current location")
                        .setCancelable(false)
                        .setNegativeButton(android.R.string.cancel) { dialogInterface, i ->
                            dialogInterface.dismiss()
                            token?.cancelPermissionRequest()
                        }
                        .setPositiveButton(android.R.string.ok) { dialogInterface, i ->
                            dialogInterface.dismiss()
                            token?.continuePermissionRequest()
                        }
                        .show()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                    Toast.makeText(this@MapsActivity, "Denied Permisionn", Toast.LENGTH_LONG).show()

                    this@MapsActivity.setContentView(R.layout.activity_request_permission)

                    button.setOnClickListener {
                        validatePermission()
                    }

                }
            }
            ).check()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        setMarkerLocation(mMap)
    }

    @SuppressLint("MissingPermission")
    fun setMarkerLocation(googleMap: GoogleMap){
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                currentLocation  = LatLng(location!!.latitude, location.longitude)
                googleMap.addMarker(MarkerOptions().icon(
                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)
                ).position(currentLocation!!).title("Ur location"))
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 16.0f))
                googleMap.uiSettings.isMapToolbarEnabled = false
                googleMap.uiSettings.isCompassEnabled = false
                googleMap.uiSettings.isMyLocationButtonEnabled = false
            }
    }

    override fun callbackMethod(rideMode: String?) {
        when (rideMode) {
            "private" -> {
                bottomSheetDialogFragment = BottomFragmentSpecialReq()
                bottomSheetDialogFragment.show(supportFragmentManager,"Dialog")
            }
            "pool" -> {
                ll_loading_trip.visibility = View.VISIBLE
                fab_request_ride.hide()
                var rideReq = Ride()
                rideReq.latitude = currentLocation!!.latitude
                rideReq.longitude = currentLocation!!.longitude
                presenter!!.getCarService(rideReq)
            }
        }
    }

    override fun callbackSpqcialReq(rideReq: Ride) {
        ll_loading_trip.visibility = View.VISIBLE
        fab_request_ride.hide()
        rideReq.latitude = currentLocation!!.latitude
        rideReq.longitude = currentLocation!!.longitude
        presenter!!.getCarService(rideReq)
    }



    override fun drawPolyline(rideResponse: Ride,rideRoute:MutableList<LatLng>) {
        ll_loading_trip.visibility = View.GONE
        var rideRequest = Ride()
        rideRequest.latitude =currentLocation!!.latitude
        rideRequest.longitude =currentLocation!!.longitude

        if (rideRoute.isNotEmpty()) {
            var opts:PolylineOptions = PolylineOptions().addAll(rideRoute).color(Color.BLACK).width(10F)
            mMap.addPolyline(opts)
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(rideRequest.latitude!!,rideRequest.longitude!!), 13.5F))
        mMap.addMarker(MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)
        ).position(LatLng(rideResponse.latitude!!,rideResponse.longitude!!)).title("Ur ride is on the way"))
        ll_trip_found.visibility = View.VISIBLE
        fab_request_ride.show()
        bottomSheetDialogFragment = BottomFragmentUserPreferences()
    }

    override fun carServiceError() {
        Toast.makeText(this@MapsActivity, "There was a connection error", Toast.LENGTH_LONG).show()
        fab_request_ride.show()
        ll_loading_trip.visibility = View.GONE
    }

}