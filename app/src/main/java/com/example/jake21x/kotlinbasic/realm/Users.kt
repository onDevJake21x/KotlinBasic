package com.example.jake21x.kotlinbasic.realm

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

/**
 * Created by Jake21x on 11/22/2017.
 */
@RealmClass
 open class Users(
        @PrimaryKey var pk:Long? = 0,
        var id:String? = null,
        var username:String? = null,
        var birthday:String? = null,
        var contact:String? = null,
        var email:String? = null,
        var address:String? = null,
        var position:String? = null,
        var photo:String? = null
    ):RealmObject() {

    }
