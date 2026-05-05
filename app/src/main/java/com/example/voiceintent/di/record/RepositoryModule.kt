package com.example.voiceintent.di.record

import com.example.voiceintent.feature.record.data.data_source.AudioRecordInMemoryDataSource
import com.example.voiceintent.feature.record.domain.repository.AudioRecordRepository
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
    abstract fun provideAudioRecordRepository(
        impl: AudioRecordInMemoryDataSource
    ): AudioRecordRepository
}