package com.example.tickets.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.tickets.RoomDB.AppDatabase
import com.example.tickets.RoomDB.TicketDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewViewModel(application: Application): AndroidViewModel(application) {

    private val ticketDao: TicketDao = AppDatabase.getDatabase(application).ticketDao()

    val isdeleted: MutableLiveData<Boolean> = MutableLiveData()

    fun deleteTicket(tid: Int){
        viewModelScope.launch (Dispatchers.IO){
            ticketDao.delete(tid)

            isdeleted.postValue(true)

        }
    }
}