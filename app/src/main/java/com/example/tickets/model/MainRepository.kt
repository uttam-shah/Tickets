package com.example.tickets.model

import android.content.Context
import com.example.tickets.RoomDB.AppDatabase
import com.example.tickets.RoomDB.Ticket
import com.example.tickets.RoomDB.TicketDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

class MainRepository(context: Context) {

   private val ticketDao: TicketDao = AppDatabase.getDatabase(context).ticketDao()

     suspend fun fetchticket(): List<Ticket> {
        return withContext(Dispatchers.IO) {
            ticketDao.getAll() // Directly return the result from the DAO
        }
    }

}