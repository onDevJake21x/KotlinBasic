package com.example.jake21x.kotlinbasic.services

import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.IBinder
import android.widget.Toast
import com.example.jake21x.kotlinbasic.model.Logs
import com.example.jake21x.kotlinbasic.model.Session
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Jake21x on 11/27/2017.
 */

open class AppLogger :Service(){

    override fun onBind(p0: Intent?): IBinder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate() {

        stopSelf();
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        return Service.START_STICKY
    }
}