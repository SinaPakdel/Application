package com.example.application.data.repository

import com.example.application.data.LocalDataSource
import com.example.application.data.RemoteDataSource
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class Repository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) {
    val remote = remoteDataSource
    val local = localDataSource
}