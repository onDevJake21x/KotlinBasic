package com.example.jake21x.kotlinbasic.model

/**
 * Created by Jake21x on 11/22/2017.
 */

data class Images(
        val id: String,
        val user_id:String,
        val fullpath: String,
        val tag: String,
        val date:String
) {
    companion object {
        val TABLE_NAME = "images"
        val id = "id"
        val user_id = "user_id"
        val fullpath = "fullpath"
        val tag = "tag"
        val date = "date"
    }
}
