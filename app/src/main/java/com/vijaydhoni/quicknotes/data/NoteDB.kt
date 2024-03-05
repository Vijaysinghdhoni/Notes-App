package com.vijaydhoni.quicknotes.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [(Note::class)], version = 1)
abstract class NoteDB : RoomDatabase() {

    abstract val notesDao: NoteDao

}