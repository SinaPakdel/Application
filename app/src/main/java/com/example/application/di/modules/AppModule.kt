package com.example.application.di.modules

import android.content.Context
import com.example.application.utils.network.NetworkListener
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(FragmentComponent::class)
object AppModule {
    @Provides
    fun provideNetworkListener(@ApplicationContext context: Context) = NetworkListener(context)
}