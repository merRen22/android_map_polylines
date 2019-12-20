package com.example.androidmapspolylines.UI.Interfaces

import com.example.androidmapspolylines.Models.Ride

interface BottomSheetInterface {
    fun callbackMethod(rideMode: String?)
    fun callbackSpqcialReq(rideReq: Ride)
}