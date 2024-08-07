package com.example.hapzassign.eventmanager.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.hapzassign.eventmanager.model.Converters
import com.example.hapzassign.eventmanager.model.Event

@Database(entities = [Event::class], version = 1)
@TypeConverters(Converters::class)
abstract class EventDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao

    companion object {
        @Volatile
        private var instance: EventDatabase? = null

        fun getDatabase(context: Context): EventDatabase =
            instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext, EventDatabase::class.java, "event_database")
                .fallbackToDestructiveMigration()
                .build()
    }
}
