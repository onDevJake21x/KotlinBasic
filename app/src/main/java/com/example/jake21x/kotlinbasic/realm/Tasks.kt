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
        var note:String? = null,
        var location:String? = null,
        var datetime:String? = null,
        var tasktype:String? = null,
        var client:String? = null
    ):RealmObject(){

    }