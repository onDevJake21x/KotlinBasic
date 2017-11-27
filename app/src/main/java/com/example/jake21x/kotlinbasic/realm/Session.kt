package com.example.jake21x.kotlinbasic.realm

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

/**
 * Created by Jake21x on 11/22/2017.
 */
@RealmClass
 open class Session(
        @PrimaryKey var pk:Long? = 0,
        var id:String? = null,
        var token:String? = null,
        var email:String? = null,
        var name:String? = null,
        var password:String? = null,
        var onsession:String? = null,
        var user_level:String? = null
    ):RealmObject() {

    }
