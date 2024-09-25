package com.example.tickets.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.tickets.RoomDB.Ticket
import com.example.tickets.model.MainRepository
import kotlinx.coroutines.launch

class MainViewModel(application: Application): AndroidViewModel(application) {

    private val repository = MainRepository(application)
     val tickets: MutableLiveData<List<Ticket>> = MutableLiveData()

    init {
      // fetch()
    }

    fun fetch(){
        viewModelScope.launch {
            tickets.value = repository.fetchticket()
        }
    }


}