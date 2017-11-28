package com.example.jake21x.kotlinbasic


import android.os.BatteryManager
import android.content.Intent
import android.content.IntentFilter
import android.database.sqlite.SQLiteDatabase
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*
import android.database.MatrixCursor
import android.database.sqlite.SQLiteQueryBuilder
import com.example.jake21x.kotlinbasic.model.Session
import java.sql.SQLException
import kotlin.collections.ArrayList


/**
 * Created by Jake21x on 11/28/2017.
 */






class  Db{

    val dbName="marca-native-app.db"
    val dbVersion = 1 ;

    val TABLE_NAME_logs = "logs";
    val TABLE_NAME_session = "session";
    val TABLE_NAME_itinerary = "itinerary";
    val TABLE_NAME_users = "users";

    val exe1 = ("CREATE TABLE IF NOT EXISTS " + TABLE_NAME_logs + "("
            + "id INTEGER PRIMARY KEY,"
            + "user_id" + " TEXT,"
            + "battery" + " TEXT,"
            + "created_date" + " TEXT,"
            + "created_time" + " TEXT,"
            + "status " + "TEXT" + " DEFAULT 'unsync')")

    val exe2 = ("CREATE TABLE IF NOT EXISTS " + TABLE_NAME_users + "("
            + "id INTEGER PRIMARY KEY,"
            + "username" + " TEXT,"
            + "birthday" + " TEXT,"
            + "contact" + " TEXT,"
            + "email" + " TEXT,"
            + "address" + " TEXT,"
            + "photo" + " TEXT,"
            + "status " + "TEXT" + " DEFAULT 'unsync')")

    val exe3 = ("CREATE TABLE IF NOT EXISTS " + TABLE_NAME_itinerary + "("
            + "id INTEGER PRIMARY KEY,"
            + "user_id" + " TEXT,"
            + "remarks" + " TEXT,"
            + "long" + " TEXT,"
            + "lat" + " TEXT,"
            + "date" + " TEXT,"
            + "time" + " TEXT,"
            + "tasktype" + " TEXT,"
            + "starttime" + " TEXT,"
            + "endtime" + " TEXT,"
            + "client" + " TEXT,"
            + "status " + "TEXT" + " DEFAULT 'unsync')")

    val exe4 = ("CREATE TABLE IF NOT EXISTS " + TABLE_NAME_session + "("
            + "id INTEGER PRIMARY KEY,"
            + "user_id" + " TEXT,"
            + "name" + " TEXT,"
            + "email" + " TEXT,"
            + "user_level" + " TEXT,"
            + "password" + " TEXT,"
            + "token" + "TEXT)")


    var sqlDB:SQLiteDatabase?=null

    constructor(context:Context){
        var db=DatabaseHelperNotes(context)
        sqlDB=db.writableDatabase

    }


    inner class  DatabaseHelperNotes:SQLiteOpenHelper{
        var context:Context?=null
        constructor(context:Context):super(context,dbName,null,dbVersion){
            this.context=context
        }

        override fun onCreate(p0: SQLiteDatabase?) {
            p0!!.execSQL(exe1)
            p0!!.execSQL(exe2)
            p0!!.execSQL(exe3)
            p0!!.execSQL(exe4)
            Toast.makeText(this.context," database is created", Toast.LENGTH_LONG).show()
        }

        override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
            p0!!.execSQL("Drop table IF EXISTS " + TABLE_NAME_users)
            p0!!.execSQL("Drop table IF EXISTS " + TABLE_NAME_logs)
            p0!!.execSQL("Drop table IF EXISTS " + TABLE_NAME_itinerary)
            p0!!.execSQL("Drop table IF EXISTS " + TABLE_NAME_session)
        }

    }


    fun store(table:String , values:ContentValues):Long{
        val ID= sqlDB!!.insert(table,"",values)
        return ID
    }

    fun  executeQuery(table:String ,projection:Array<String>,selection:String,selectionArgs:Array<String>,sorOrder:String):Cursor{
        val qb= SQLiteQueryBuilder()
        qb.tables = table
        val cursor=qb.query(sqlDB,projection,selection,selectionArgs,null,null,sorOrder)
        return cursor
    }

}