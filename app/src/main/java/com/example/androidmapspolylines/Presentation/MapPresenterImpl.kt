package com.example.androidmapspolylines.Presentation

import com.example.androidmapspolylines.Data.Interactor.RideInteractorImpl
import com.example.androidmapspolylines.Models.Ride
import com.example.androidmapspolylines.UI.Interfaces.MapInterface
import com.google.android.gms.maps.model.LatLng

class MapPresenterImpl: MapPresenter{

    private var mapInterface: MapInterface
    private var rideInteractor: RideInteractorImpl = RideInteractorImpl(this)

    constructor(mapInterface: MapInterface){
        this.mapInterface = mapInterface
    }

    override
    fun getCarService(ride: Ride) {
        rideInteractor.getCarService(ride)
    }

    override
    fun carServiceError() {
        mapInterface.carServiceError()
    }

    override
    fun drawPolyline(rideResponse: Ride,rideRoute:MutableList<LatLng>) {
        mapInterface.drawPolyline(rideResponse,rideRoute)
    }

}