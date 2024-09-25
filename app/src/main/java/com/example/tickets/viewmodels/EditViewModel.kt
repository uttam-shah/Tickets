package com.example.tickets.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.tickets.RoomDB.AppDatabase
import com.example.tickets.RoomDB.Ticket
import com.example.tickets.RoomDB.TicketDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditViewModel(application: Application): AndroidViewModel(application) {

    private val ticketDao: TicketDao = AppDatabase.getDatabase(application).ticketDao()

    val name: MutableLiveData<String> = MutableLiveData()
    val description: MutableLiveData<String> = MutableLiveData()
    val dueDate: MutableLiveData<String> = MutableLiveData()
    val dueTime: MutableLiveData<String> = MutableLiveData()
    val priority: MutableLiveData<String> = MutableLiveData()
    val taskUpdated: MutableLiveData<Boolean> = MutableLiveData()

    fun updateData(id: Int){
        viewModelScope.launch(Dispatchers.IO ) {
            val ticket = Ticket( id, name.value.toString(), description.value.toString(), priority.value.toString(),dueDate.value.toString(),dueTime.value.toString())
            ticketDao.updateTicket(ticket)

            launch(Dispatchers.Main) {
                taskUpdated.value = true
            }
        }


    }
}