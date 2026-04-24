package com.example.voiceintent.di.note_analysis

import com.example.voiceintent.feature.note_analysis.data.repository.NoteAnalysisRepositoryImpl
import com.example.voiceintent.feature.note_analysis.domain.repository.NoteAnalysisRepository
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
    abstract fun bindNoteNoteAnalysisRepository(
        impl: NoteAnalysisRepositoryImpl
    ): NoteAnalysisRepository
}