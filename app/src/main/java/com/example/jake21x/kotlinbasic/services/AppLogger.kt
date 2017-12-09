package com.example.jake21x.kotlinbasic.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.jake21x.kotlinbasic.utils.Db

/**
 * Created by Jake21x on 11/27/2017.
 */

open class AppLogger :Service(){

    override fun onBind(p0: Intent?): IBinder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate() {

        val db = Db.Instance(this);
        db.createlog(db);

//        db.use {
//            insert(
//                    Logs.TABLE_NAME ,
//                    Logs.user_id to "1",
//                    Logs.battery to "34",
//                    Logs.created_date to SimpleDateFormat("dd/MM/yyyy").format(Date()).toString(),
//                    Logs.created_time to SimpleDateFormat("hh:mm a").format(Date()).toString()
//
//            )
//        }



        onDestroy();
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        return Service.START_STICKY
    }

    override fun onDestroy() {
        stopSelf();
        super.onDestroy()
    }
}