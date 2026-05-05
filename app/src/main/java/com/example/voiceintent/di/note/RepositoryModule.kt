package com.example.voiceintent.di.note

import com.example.voiceintent.feature.note.data.repository.NoteRepositoryImpl
import com.example.voiceintent.feature.note.domain.repository.NoteRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun provideNoteRepository(
        impl: NoteRepositoryImpl
    ): NoteRepository
}