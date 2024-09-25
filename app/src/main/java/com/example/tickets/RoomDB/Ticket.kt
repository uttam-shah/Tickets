package com.example.tickets.RoomDB

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "tickets")
data class Ticket (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val description: String,
    val priority: String,
    val dueDate: String,
    val dueTime: String
):Serializable