package com.example.jake21x.kotlinbasic.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.widget.Toast

/**
 * Created by Jake21x on 11/27/2017.
 */

open class AppLogger :Service(){

    override fun onBind(p0: Intent?): IBinder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate() {

        Toast.makeText( getApplicationContext(), "Logger fire!" , Toast.LENGTH_LONG).show();

        stopSelf();
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        return Service.START_STICKY
    }
}