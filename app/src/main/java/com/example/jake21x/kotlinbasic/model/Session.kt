package com.example.jake21x.kotlinbasic.model

/**
 * Created by Jake21x on 11/22/2017.
 */
 data class Session(
        var id:String,
        var user_id:String ,
        var token:String ,
        var email:String ,
        var name:String ,
        var password:String ,
        var onsession:String ,
        var user_level:String
    ){

   companion object {
       val TABLE_NAME = "session"
       var id  = "id"
       var user_id = "user_id"
       var token = "token"
       var email  = "email"
       var name  = "name"
       var password  = "password"
       var onsession = "onsession"
       var user_level  = "user_level"
   }
}
