package com.example.tickets.RoomDB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TicketDao {

    @Insert
    fun insertTicket(ticket: Ticket)

    @Update
    fun updateTicket(ticket: Ticket)

    @Query("SELECT * FROM tickets")
    fun getAll():List<Ticket>

    @Query("DELETE FROM tickets WHERE id = :tid")
    fun delete(tid:Int)
}