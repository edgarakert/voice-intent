package com.example.voiceintent.di.note

import android.content.Context
import androidx.room.Room
import com.example.voiceintent.feature.note.data.db.NoteDao
import com.example.voiceintent.feature.note.data.db.NoteDatabase
import com.example.voiceintent.feature.tag.data.db.TagDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideNoteDatabase(@ApplicationContext context: Context): NoteDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = NoteDatabase::class.java,
            name = "voice_intent.db"
        ).fallbackToDestructiveMigration(false).build()
    }

    @Provides
    @Singleton
    fun provideNoteDao(database: NoteDatabase): NoteDao {
        return database.noteDao()
    }

    @Provides
    @Singleton
    fun provideTagDao(database: NoteDatabase): TagDao {
        return database.tagDao()
    }
}