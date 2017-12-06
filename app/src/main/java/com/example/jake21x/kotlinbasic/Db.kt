package com.example.jake21x.kotlinbasic

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.widget.Toast
import com.example.jake21x.kotlinbasic.model.Logs
import com.example.jake21x.kotlinbasic.model.Session
import org.jetbrains.anko.db.*
import org.jetbrains.anko.toast
import java.text.SimpleDateFormat
import java.util.*
import android.os.BatteryManager
import android.content.Intent
import android.content.IntentFilter
import com.example.jake21x.kotlinbasic.model.Tasks


/**
 * Created by Jake21x on 11/28/2017.
 */
class Db(ctx: Context) : ManagedSQLiteOpenHelper(ctx, "marca-native-app.db", null, 1) {

    var dbuser : Session? = null
    var dblogs : Logs? = null

    companion object {
        private var instance: Db? = null

        var  mContext:Context?=null;

        @Synchronized
        fun Instance(context: Context): Db {

            mContext = context;

            if (instance == null) {
                instance = Db(context.applicationContext)
            }
            return instance!!
        }
    }

    override fun onCreate(database: SQLiteDatabase) {
        database.createTable(Logs.TABLE_NAME, true,
                Logs.id to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
                Logs.user_id to TEXT,
                Logs.battery to TEXT,
                Logs.created_date to TEXT,
                Logs.created_time to TEXT,
                Logs.status to TEXT + DEFAULT("unsync")
        )

        database.createTable(Session.TABLE_NAME, true,
                Session.id to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
                Session.user_id to TEXT,
                Session.user_level to TEXT,
                Session.email to TEXT,
                Session.name to TEXT,
                Session.password to TEXT,
                Session.token to TEXT,
                Session.onsession to TEXT
        )


        database.createTable(Tasks.TABLE_NAME, true,
                Tasks.id to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
                Tasks.user to TEXT,
                Tasks.remarks to TEXT,
                Tasks.long to TEXT,
                Tasks.lat to TEXT,
                Tasks.date to TEXT,
                Tasks.time to TEXT,
                Tasks.starttime to TEXT,
                Tasks.endtime to TEXT,
                Tasks.client to TEXT,
                Tasks.status to TEXT + DEFAULT("unsync")
        )

        Toast.makeText(mContext, "table created",Toast.LENGTH_SHORT).show()
    }

    override fun onUpgrade(database: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        database.dropTable(Logs.TABLE_NAME, true)
        database.dropTable(Session.TABLE_NAME, true)
        database.dropTable(Tasks.TABLE_NAME, true)
    }


    fun createlog(db:ManagedSQLiteOpenHelper) {

        db.use {
            insert(
                    Logs.TABLE_NAME ,
                    Logs.user_id to getSession(db)[0].user_id.toString(),
                    Logs.battery to "${getBattery().toString()}%",
                    Logs.created_date to SimpleDateFormat("dd/MM/yyyy").format(Date()).toString(),
                    Logs.created_time to SimpleDateFormat("hh:mm a").format(Date()).toString()

            )
        }
        mContext!!.toast("log must show and store!");
    }

    fun clearSession(db:ManagedSQLiteOpenHelper){
        db.use {
            delete(Session.TABLE_NAME);
        }
    }

    fun getSession(db:ManagedSQLiteOpenHelper) : List<Session>{
        val loggedin = db.use {
            select(Session.TABLE_NAME).parseList(object :org.jetbrains.anko.db.MapRowParser<Session>{
                override fun parseRow(columns: Map<String, Any>): Session {

                    val id = columns.getValue("id");
                    val name = columns.getValue("name");
                    val user_id = columns.getValue("user_id");
                    val user_level = columns.getValue("user_level");
                    val email = columns.getValue("email");
                    val password = columns.getValue("password");
                    val token = columns.getValue("token");
                    val onsession = columns.getValue("onsession");

                    dbuser = Session(
                            id = id.toString(),
                            user_id = user_id.toString(),
                            email = email.toString(),
                            name = name.toString(),
                            user_level =  user_level.toString(),
                            password =  password.toString(),
                            onsession =  onsession.toString(),
                            token =  token.toString()
                    );
                    return dbuser!!
                }

            })
        }
        return loggedin
    }

    fun getLogs(db:ManagedSQLiteOpenHelper) : List<Logs>{
        val loggedin = db.use {
            select(Logs.TABLE_NAME).parseList(object :org.jetbrains.anko.db.MapRowParser<Logs>{
                override fun parseRow(columns: Map<String, Any>): Logs {

                    val id = columns.getValue("id");
                    val user_id = columns.getValue("user_id");
                    val battery = columns.getValue("battery");
                    val created_date = columns.getValue("created_date");
                    val created_time = columns.getValue("created_time");
                    val status = columns.getValue("status");

                    dblogs = Logs(
                            id = id.toString(),
                            user_id = user_id.toString(),
                            battery = battery.toString(),
                            created_date = created_date.toString(),
                            created_time = created_time.toString(),
                            status = status.toString()
                    );
                    return dblogs!!
                }

            })
        }
        return loggedin
    }

    fun getBattery(): Int {
        val iFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val batteryStatus = mContext!!.registerReceiver(null, iFilter)
        val level = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
        val scale = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
        val batteryPct = level / scale.toFloat()
        return (batteryPct * 100).toInt()
    }

}


