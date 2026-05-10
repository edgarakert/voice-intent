package com.example.voiceintent.feature.note.data.db

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.voiceintent.feature.tag.data.db.NoteTagEntity
import com.example.voiceintent.feature.tag.data.db.TagEntity

data class NoteWithTags(
    @Embedded val note: NoteEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = NoteTagEntity::class,
            parentColumn = "noteId",
            entityColumn = "tagId"
        )
    )
    val tags: List<TagEntity>
)
