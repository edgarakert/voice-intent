package com.example.voiceintent.feature.note.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.voiceintent.feature.note.data.db.converter.StringListConverter
import com.example.voiceintent.feature.tag.data.db.NoteTagEntity
import com.example.voiceintent.feature.tag.data.db.TagDao
import com.example.voiceintent.feature.tag.data.db.TagEntity

@Database(
    entities = [NoteEntity::class, TagEntity::class, NoteTagEntity::class],
    version = 3,
    exportSchema = false
)
@TypeConverters(StringListConverter::class)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao

    abstract fun tagDao(): TagDao
}