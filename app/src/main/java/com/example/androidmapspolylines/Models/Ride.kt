package com.example.androidmapspolylines.Models

import org.json.JSONObject

open class Ride {

    var fcmToken:String?=null
    var latitude:Double?=null
    var longitude:Double?=null
    var isForKids:Boolean = false
    var isForPets:Boolean = false
    var isForLuggage:Boolean = false

    var status:String?=null

    fun fromJson(jsonObject: JSONObject): Ride {
        var ride = Ride()
        ride.status = jsonObject.getString("Status")
        ride.latitude = jsonObject.getDouble("Latitude")
        ride.longitude = jsonObject.getDouble("Longitude")
        return ride
    }

    fun networkError(): Ride {
        var ride = Ride()
        ride.status = "error"
        ride.latitude = 0.0
        ride.longitude = 0.0
        return ride
    }
}