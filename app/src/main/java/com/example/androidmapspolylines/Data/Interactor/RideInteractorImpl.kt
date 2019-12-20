package com.example.androidmapspolylines.Data.Interactor

import android.os.AsyncTask
import com.example.androidmapspolylines.Data.Repository.RideRepository
import com.example.androidmapspolylines.Models.Ride
import com.example.androidmapspolylines.Presentation.MapPresenterImpl
import com.google.android.gms.maps.model.LatLng

class RideInteractorImpl: RideInteractor{

    private val rideRepository = RideRepository()
    private var mapPresenter: MapPresenterImpl?=null
    private var initialPoint: Ride?=null
    private var endPoint: Ride?=null

    constructor(mapPresenter: MapPresenterImpl) {
        this.mapPresenter = mapPresenter
    }

    override
    fun getCarService(ride: Ride) {
        this.initialPoint = ride
        getCarService_Task().execute()
    }

    private inner class getCarService_Task : AsyncTask<Void, Void, Ride>() {

        override fun doInBackground(vararg voids: Void): Ride {
            return rideRepository.getCarService(initialPoint!!)
        }

        override fun onPostExecute(rideResponse: Ride) {
            if (rideResponse.status == "Error") {
                mapPresenter!!.carServiceError()
            } else {
                endPoint = rideResponse
                getDirections_Task().execute()
            }
        }
    }

    private inner class getDirections_Task : AsyncTask<Void, Void, MutableList<LatLng>>() {

        override fun doInBackground(vararg voids: Void): MutableList<LatLng> {
            return rideRepository.getDirections(initialPoint!!,endPoint!!)
        }

        override fun onPostExecute(rideRoute: MutableList<LatLng>) {
            mapPresenter!!.drawPolyline(endPoint!!,rideRoute)
        }
    }

}