package com.example.hapzassign.eventmanager.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "events")
@TypeConverters(Converters::class)
data class Event(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val dateTime: Long,
    val location: String,
    val description: String,
    val participants: List<String> // Use your custom converter for this field
)
