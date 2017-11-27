package com.example.jake21x.kotlinbasic.realm

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

/**
 * Created by Jake21x on 11/22/2017.
 */
@RealmClass
 open class Logs(
        @PrimaryKey var pk:Long? = 0,
        var id:String? = null,
        var user:String? = null,
        var date:String? = null,
        var time:String? = null,
        var battery:String? = null,
        var status:String? = null
    ):RealmObject() {

    }
