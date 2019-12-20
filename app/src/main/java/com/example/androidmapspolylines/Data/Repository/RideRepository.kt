package com.example.androidmapspolylines.Data.Repository

import com.example.androidmapspolylines.Models.Ride
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.PolyUtil
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject


class RideRepository{

    private val client = OkHttpClient()

    fun getCarService(ride: Ride): Ride {

        var responseRide = Ride()


        //Call for an API to get ride location, basically a match between the user and the driver

        /*
        try {
            val jsonObject = JSONObject()
            jsonObject.put("Latitude", ride.latitude)
            jsonObject.put("Longitude", ride.longitude)
            jsonObject.put("fcmToken", ride.fcmToken)
            jsonObject.put("isForKids", ride.isForKids)
            jsonObject.put("isForLuggage", ride.isForLuggage)
            jsonObject.put("isForPets", ride.isForPets)

            val body = RequestBody.create(MediaType.parse(ApiConfig.CONTENT_TYPE), jsonObject.toString())
            val request = Request.Builder()
                .url(ApiConfig.getCarService)
                .post(body)
                .build()

            val response = client.newCall(request).execute()
            val `object` = JSONObject(response.body()!!.string())

            responseRide = Ride().fromJson(`object`)
        } catch (ex: Exception) {
            responseRide = Ride().networkError()
        }
        */

        responseRide.status = "sucess"
        responseRide.latitude = ride.latitude!! - 0.0100000f
        responseRide.longitude = ride.longitude!! + 0.0100000f

        return responseRide
    }

    fun getDirections(initialPoint:Ride,endPoint:Ride): MutableList<LatLng> {
        var listOfGeoPoints:MutableList<LatLng> = ArrayList()

        try {
            var url: String = "https://maps.googleapis.com/maps/api/directions/json?origin=" +
                    initialPoint.latitude + "," + initialPoint.longitude  + "&destination=" +
                    endPoint.latitude + "," + endPoint.longitude + "&sensor=false&units=metric" +
                    "&key=  KEY HERE    "

            val request = Request.Builder()
                .url(url)
                .build()

            val response = client.newCall(request).execute()
            val jsonResponse = JSONObject(response.body()!!.string())


            var cadena = JSONObject(jsonResponse.getJSONArray("routes")[0].toString()).getJSONObject("overview_polyline")
            val encodedPoints = cadena.getString("points")
            listOfGeoPoints.addAll(
                    PolyUtil.decode(
                        encodedPoints.trim { it <= ' ' }.replace(
                            "\\\\",
                            "\\"
                        )
                    )
                )
        } catch (ex: Exception) {
            println("There was an error ->  ${ex.message}")
        }
        return listOfGeoPoints
    }

}