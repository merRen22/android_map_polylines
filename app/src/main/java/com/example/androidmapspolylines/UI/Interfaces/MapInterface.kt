package com.example.androidmapspolylines.UI.Interfaces

import com.example.androidmapspolylines.Models.Ride
import com.google.android.gms.maps.model.LatLng

interface MapInterface {
    fun drawPolyline(rideResponse: Ride,rideRoute:MutableList<LatLng>)
    fun carServiceError()
}