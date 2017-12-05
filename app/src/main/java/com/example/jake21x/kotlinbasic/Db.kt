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


/**
 * Created by Jake21x on 11/28/2017.
 */
class Db(ctx: Context) : ManagedSQLiteOpenHelper(ctx, "marca-native-app.db", null, 1) {


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

        Toast.makeText(mContext, "table created",Toast.LENGTH_SHORT).show()
    }

    override fun onUpgrade(database: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        database.dropTable(Logs.TABLE_NAME, true)
    }


    fun createlog(db:ManagedSQLiteOpenHelper) {
        db.use {
            insert(
                    Logs.TABLE_NAME ,
                    Logs.user_id to "1",
                    Logs.battery to "34",
                    Logs.created_date to SimpleDateFormat("dd/MM/yyyy").format(Date()).toString(),
                    Logs.created_time to SimpleDateFormat("hh:mm a").format(Date()).toString()

            )
        }
        mContext!!.toast("log must show and store!");
    }
}


//
//class  Db{
//
//
// val dbName="marca-native-app.db"
//val dbVersion = 1 ;
//
//val TABLE_NAME_logs = "logs";
//val TABLE_NAME_session = "session";
//val TABLE_NAME_itinerary = "itinerary";
//val TABLE_NAME_users = "users";

//
//
//    val exe2 = ("CREATE TABLE IF NOT EXISTS " + TABLE_NAME_users + "("
//            + "id INTEGER PRIMARY KEY,"
//            + "username" + " TEXT,"
//            + "birthday" + " TEXT,"
//            + "contact" + " TEXT,"
//            + "email" + " TEXT,"
//            + "address" + " TEXT,"
//            + "photo" + " TEXT,"
//            + "status " + "TEXT" + " DEFAULT 'unsync')")
//
//    val exe3 = ("CREATE TABLE IF NOT EXISTS " + TABLE_NAME_itinerary + "("
//            + "id INTEGER PRIMARY KEY,"
//            + "user_id" + " TEXT,"
//            + "remarks" + " TEXT,"
//            + "long" + " TEXT,"
//            + "lat" + " TEXT,"
//            + "date" + " TEXT,"
//            + "time" + " TEXT,"
//            + "tasktype" + " TEXT,"
//            + "starttime" + " TEXT,"
//            + "endtime" + " TEXT,"
//            + "client" + " TEXT,"
//            + "status " + "TEXT" + " DEFAULT 'unsync')")
//
//    val exe4 = ("CREATE TABLE IF NOT EXISTS " + TABLE_NAME_session + "("
//            + "id INTEGER PRIMARY KEY,"
//            + "user_id" + " TEXT,"
//            + "name" + " TEXT,"
//            + "email" + " TEXT,"
//            + "user_level" + " TEXT,"
//            + "password" + " TEXT,"
//            + "token" + "TEXT)")
//
//
//    var sqlDB:SQLiteDatabase?=null
//
//    constructor(context:Context){
//        var db=DatabaseHelperNotes(context)
//        sqlDB=db.writableDatabase
//
//    }
//
//
//    inner class  DatabaseHelperNotes:SQLiteOpenHelper{
//        var context:Context?=null
//        constructor(context:Context):super(context,dbName,null,dbVersion){
//            this.context=context
//        }
//
//        override fun onCreate(p0: SQLiteDatabase?) {
//            p0!!.execSQL(exe1)
//            p0!!.execSQL(exe2)
//            p0!!.execSQL(exe3)
//            p0!!.execSQL(exe4)
//            Toast.makeText(this.context," database is created", Toast.LENGTH_LONG).show()
//        }
//
//        override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
//            p0!!.execSQL("Drop table IF EXISTS " + TABLE_NAME_users)
//            p0!!.execSQL("Drop table IF EXISTS " + TABLE_NAME_logs)
//            p0!!.execSQL("Drop table IF EXISTS " + TABLE_NAME_itinerary)
//            p0!!.execSQL("Drop table IF EXISTS " + TABLE_NAME_session)
//        }
//
//    }
//
//
//    fun store(table:String , values:ContentValues):Long{
//        val ID= sqlDB!!.insert(table,"",values)
//        return ID
//    }
//
//    fun  executeQuery(table:String ,projection:Array<String>,selection:String,selectionArgs:Array<String>,sorOrder:String):Cursor{
//        val qb= SQLiteQueryBuilder()
//        qb.tables = table
//        val cursor=qb.query(sqlDB,projection,selection,selectionArgs,null,null,sorOrder)
//        return cursor
//    }
