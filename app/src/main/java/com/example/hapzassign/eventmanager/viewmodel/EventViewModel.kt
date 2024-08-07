package com.example.hapzassign.eventmanager.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.hapzassign.eventmanager.database.EventDatabase
import com.example.hapzassign.eventmanager.model.Event
import com.example.hapzassign.eventmanager.repository.EventRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EventViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: EventRepository
    val allEvents: LiveData<List<Event>>

    init {
        val eventDao = EventDatabase.getDatabase(application).eventDao()
        repository = EventRepository(eventDao)
        allEvents = repository.allEvents
    }

    fun insert(event: Event) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(event)
    }

    fun update(event: Event) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(event)
    }

    fun getEventById(id: Int): LiveData<Event?> {
        return repository.getEventById(id)
    }
}
