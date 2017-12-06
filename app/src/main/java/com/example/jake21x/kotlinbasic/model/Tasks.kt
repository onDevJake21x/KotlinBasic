package com.example.jake21x.kotlinbasic.model

/**
 * Created by Jake21x on 11/22/2017.
 */
open class Tasks(
        var id:String,
        var user:String,
        var remarks:String,
        var long:String,
        var lat:String,
        var date:String,
        var time:String,
        var starttime:String,
        var endtime:String,
        var client:String,
        var status:String
    ){

    companion object {
        val TABLE_NAME = "task"
        var id = "id"
        var user  = "user"
        var remarks = "remarks"
        var long = "long"
        var lat  = "lat"
        var date  = "date"
        var time  = "time"
        var starttime  = "starttime"
        var endtime  = "endtime"
        var client  = "client"
        var status  = "status"
    }
}