package com.example.jake21x.kotlinbasic.model

/**
 * Created by Jake21x on 11/22/2017.
 */
 open class Logs(
        var id:String? = null,
        var user_id:String? = null,
        var created_date:String? = null,
        var created_time:String? = null,
        var battery:String? = null,
        var status:String? = "unsync"
    )