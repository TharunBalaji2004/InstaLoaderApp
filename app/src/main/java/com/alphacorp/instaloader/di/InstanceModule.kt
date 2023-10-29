package com.alphacorp.instaloader.di

import android.content.Context
import com.alphacorp.instaloader.loader.InstaLoader
import com.alphacorp.instaloader.loader.InstaLoaderImpl
import com.chaquo.python.PyObject
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
    fun providesPythonInstance(@ApplicationContext context: Context): PyObject {
        if (!Python.isStarted()) {
            Python.start(AndroidPlatform(context));
        }
        val python = Python.getInstance()
        return python.getModule("script")
    }

    @Singleton
    @Provides
    fun providesInstaLoader(script: PyObject): InstaLoader {
        return InstaLoaderImpl(script)
    }
}