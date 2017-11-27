package com.example.jake21x.kotlinbasic.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.os.IBinder

/**
 * Created by Jake21x on 11/27/2017.
 */
open class GPSTracker(val context: Context) :  Service(),  LocationListener{


    var mContext:Context?=null;

    var isGPSEnabled:Boolean?=false;

    var isNetworkEnabled :Boolean?=false;

    var  canGetLocation:Boolean?=false;

    var location :Location?=null;

    var latitude :Double?=null;

    var longitude :Double?=null;


    //-Service
    override fun onBind(p0: Intent?): IBinder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        return Service.START_STICKY
    }
    //<service android:name="com.example.kotlin.myapplication.BroadcastReceiver.UserlocationUpdateService" />



    //-LocationListener
    override fun onLocationChanged(p0: Location?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onProviderEnabled(p0: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onProviderDisabled(p0: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}