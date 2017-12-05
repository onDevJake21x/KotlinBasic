package com.example.jake21x.kotlinbasic.model

/**
 * Created by Jake21x on 11/22/2017.
 */

data class Logs(
        val id: Int,
        val user_id: String,
        val battery: String,
        val created_date: String,
        val created_time: String,
        val status: String
) {
    companion object {
        val TABLE_NAME = "logs"
        val id = "id"
        val user_id = "user_id"
        val battery = "battery"
        val created_date = "created_date"
        val created_time = "created_time"
        val status = "status"
    }
}
