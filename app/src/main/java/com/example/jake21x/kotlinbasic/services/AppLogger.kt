package com.example.jake21x.kotlinbasic.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.widget.Toast
import com.example.jake21x.kotlinbasic.realm.Logs
import com.example.jake21x.kotlinbasic.realm.Session
import io.realm.Realm
import io.realm.RealmConfiguration
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Jake21x on 11/27/2017.
 */

open class AppLogger :Service(){


    var realm:Realm?=null;

    override fun onBind(p0: Intent?): IBinder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate() {

        val config = RealmConfiguration.Builder().name("kotlinbasic").deleteRealmIfMigrationNeeded().build();
        realm = Realm.getInstance(config);

        realm!!.beginTransaction();

        var pk: Long = 1
        if (realm!!.where(Logs::class.java).max("pk") != null) {
            pk = realm!!.where(Logs::class.java).max("pk") as Long + 1
        }
        val db = realm!!.createObject(Logs::class.java, pk)

        db.id  =  pk.toString()
        db.user  = "1"
        db.battery  =  "33%"
        db.date  =  SimpleDateFormat("dd/MM/yyyy").format(Date()).toString()
        db.time  = SimpleDateFormat("hh:mm a").format(Date()).toString()

        realm!!.commitTransaction();

        Toast.makeText( getApplicationContext(), "Logger fire!" , Toast.LENGTH_LONG).show();

        stopSelf();
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        return Service.START_STICKY
    }
}