package com.example.jake21x.kotlinbasic.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast

/**
 * Created by Jake21x on 11/27/2017.
 */
class AppBroadcast : BroadcastReceiver(){

    override fun onReceive(p0: Context?, p1: Intent?) {

       if(p1!!.action.equals("com.example.jake21x.kotlinbasic")){

           val bundle = p1!!.extras
           Toast.makeText( p0, bundle.getString("msg").toString() , Toast.LENGTH_LONG).show();
       }
    }

}