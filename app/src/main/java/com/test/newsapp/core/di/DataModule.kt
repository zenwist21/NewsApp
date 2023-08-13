package com.test.newsapp.core.di

import com.test.newsapp.core.data.source.repository.NewsRepository
import com.test.newsapp.core.domain.repository.INewsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun provideNewsRepo(newsRepository: NewsRepository): INewsRepository

}