package com.example.jake21x.kotlinbasic.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent


/**
 * Created by Jake21x on 11/27/2017.
 */
class AppBroadCast : BroadcastReceiver(){

    override fun onReceive(p0: Context?, p1: Intent?) {

       if(p1!!.action.equals("com.example.jake21x.kotlinbasic")){

           val myIntent = Intent(p0, AppLogger::class.java)
           p0!!.startService(myIntent)

       }
    }

}