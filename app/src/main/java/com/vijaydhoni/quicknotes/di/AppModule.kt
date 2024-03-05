package com.vijaydhoni.quicknotes.di

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.room.Room
import androidx.room.RoomDatabase
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.vijaydhoni.quicknotes.data.NoteDB
import com.vijaydhoni.quicknotes.data.NoteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun providesNotesDb(app: Application): NoteDB {
        return Room.databaseBuilder(app, NoteDB::class.java, "Notes_Database")
            .build()
    }

    @Singleton
    @Provides
    fun providesNotesDao(noteDB: NoteDB): NoteDao {
        return noteDB.notesDao
    }

    @Singleton
    @Provides
    fun providesFirebaseAuth(): FirebaseAuth {
        return Firebase.auth
    }

    @Provides
    @Singleton
    fun providesIntroductionSharedPrefernce(
        app: Application
    ): SharedPreferences = app.getSharedPreferences("UserPrefs", MODE_PRIVATE)

}