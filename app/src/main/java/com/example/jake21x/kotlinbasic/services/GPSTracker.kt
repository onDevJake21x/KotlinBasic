package com.example.jake21x.kotlinbasic.services


import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.app.Service
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings
import android.support.v7.app.AlertDialog
import android.util.Log
import android.widget.Toast

/**
 * Created by Jake21x on 6/1/2017.
 */
class GPSTracker(private val mContext: Context) : Service(), LocationListener {


    internal var isGPSEnabled = false


    internal var isNetworkEnabled = false


    internal var canGetLocation = false

    internal var location: Location? = null
    internal var latitude: Double = 0.toDouble()
    internal var longitude: Double = 0.toDouble()


    protected var locationManager: LocationManager? = null

    init {
        getLocation()
    }

    @SuppressLint("MissingPermission")
    fun getLocation(): Location? {
        try {
            locationManager = mContext
                    .getSystemService(Context.LOCATION_SERVICE) as LocationManager


            isGPSEnabled = locationManager!!
                    .isProviderEnabled(LocationManager.GPS_PROVIDER)


            isNetworkEnabled = locationManager!!
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            if (!isGPSEnabled && !isNetworkEnabled) {

            } else {
                this.canGetLocation = true

                if (isNetworkEnabled) {


                    locationManager!!.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(), this)
                    //    Log.d("Network", "Network");
                    if (locationManager != null) {

                        location = locationManager!!
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                        if (location != null) {
                            latitude = location!!.latitude
                            longitude = location!!.longitude
                        }
                    }
                }

                if (isGPSEnabled) {
                    if (location == null) {

                        locationManager!!.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(), this)
                        Log.d("GPS Enabled", "GPS Enabled")
                        if (locationManager != null) {

                            location = locationManager!!
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER)
                            if (location != null) {
                                latitude = location!!.latitude
                                longitude = location!!.longitude
                            }
                        }
                    }
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return location
    }


    fun stopUsingGPS() {
        if (locationManager != null) {

            locationManager!!.removeUpdates(this@GPSTracker)
        }
    }


    fun getLatitude(): Double {
        if (location != null) {
            latitude = location!!.latitude
        }

        // return latitude
        return latitude
    }


    fun getLongitude(): Double {
        if (location != null) {
            longitude = location!!.longitude
        }


        return longitude
    }


    fun canGetLocation(): Boolean {
        return this.canGetLocation
    }

    fun showSettingsAlert() {
        val alertDialog = AlertDialog.Builder(mContext)


        alertDialog.setTitle("Warning")


        alertDialog.setMessage("Please enabled GPS from settings")


        alertDialog.setPositiveButton("Settings") { dialog, which ->
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            mContext.startActivity(intent)
        }


        alertDialog.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }


        alertDialog.show()
    }

    override fun onLocationChanged(location: Location) {

        val lat = location.latitude
        val longi = location.longitude
        Toast.makeText(mContext, "My Location is \n" + lat + "\n" + longi, Toast.LENGTH_SHORT)
        getLocation()

    }

    override fun onProviderDisabled(provider: String) {}

    override fun onProviderEnabled(provider: String) {}

    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}

    override fun onBind(arg0: Intent): IBinder? {
        return null
    }

    companion object {


        private val MIN_DISTANCE_CHANGE_FOR_UPDATES: Long = 0


        private val MIN_TIME_BW_UPDATES = (500 * 30 * 1).toLong()
    }

}
