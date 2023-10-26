package com.alphacorp.instaloader.di

import android.content.Context
import com.alphacorp.instaloader.loader.InstaLoader
import com.alphacorp.instaloader.loader.InstaLoaderImpl
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class InstanceModule {
    @Singleton
    @Provides
    fun providesPythonInstance(@ApplicationContext context: Context): Python {
        if (!Python.isStarted()) {
            Python.start(AndroidPlatform(context));
        }
        return Python.getInstance()
    }

    @Singleton
    @Provides
    fun providesInstaLoader(): InstaLoader {
        return InstaLoaderImpl()
    }
}