package com.example.jake21x.kotlinbasic.realm

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

/**
 * Created by Jake21x on 11/22/2017.
 */
@RealmClass
open class Tasks(
        @PrimaryKey var pk:Long? = 0,
        var id:String? = null,
        var user:String? = null,
        var remarks:String? = null,
        var long:String? = null,
        var lat:String? = null,
        var date:String? = null,
        var time:String? = null,
        var tasktype:String? = null,
        var starttime:String? = null,
        var endtime:String? = null,
        var client:String? = null
    ):RealmObject(){

    }