package com.example.hapzassign.eventmanager.repository

import androidx.lifecycle.LiveData
import com.example.hapzassign.eventmanager.database.EventDao
import com.example.hapzassign.eventmanager.model.Event

class EventRepository(private val eventDao: EventDao) {

    val allEvents: LiveData<List<Event>> = eventDao.getAllEvents()

    fun insert(event: Event) {
        eventDao.insert(event)
    }

    fun update(event: Event) {
        eventDao.update(event)
    }

    fun getEventById(id: Int): LiveData<Event?> {
        return eventDao.getEventById(id)
    }
}
