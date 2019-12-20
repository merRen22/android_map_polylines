package com.example.androidmapspolylines.Presentation

import com.example.androidmapspolylines.Models.Ride
import com.google.android.gms.maps.model.LatLng

interface MapPresenter {

    fun getCarService(ride: Ride)
    fun carServiceError()
    fun drawPolyline(rideResponse: Ride, rideRoute:MutableList<LatLng>)
}