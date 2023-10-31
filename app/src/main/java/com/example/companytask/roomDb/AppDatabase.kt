package com.example.companytask.roomDb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [NoteEntity::class], version = 1)
abstract class AppDatabase:RoomDatabase() {
    abstract fun noteDao(): NoteDao
    companion object{
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context):AppDatabase{
            if(INSTANCE == null){
                synchronized(this){
                    INSTANCE = Room.databaseBuilder(context,AppDatabase::class.java,"AppDatabase").build()
                }
            }
            return INSTANCE!!
        }
    }
}