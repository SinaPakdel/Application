package com.example.application.di.modules

import android.app.Application
import com.example.application.data.RemoteDataSource
import com.example.application.data.remote.FoodService
import com.example.application.di.qualifier.ApiKey
import com.example.application.di.qualifier.BaseUrl
import com.example.application.utils.api.provideApi
import com.example.application.utils.consts.Constants.Companion.API_KEY
import com.example.application.utils.consts.Constants.Companion.API_KEY_VALUE
import com.example.application.utils.consts.Constants.Companion.BASE_URL
import com.example.application.utils.view.ImageLoader
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideImageLoader(application: Application) = ImageLoader(application)

    @Provides
    @Singleton
    @ApiKey
    fun provideApiKey(): String = API_KEY

    @Provides
    @Singleton
    @BaseUrl
    fun provideBaseUrl(): String = BASE_URL


    @Provides
    @Singleton
    fun provideInterceptor(@ApiKey apiKey: String): Interceptor = Interceptor { chain ->
        val url = chain.request()
            .url
            .newBuilder()
            .addQueryParameter(API_KEY_VALUE, apiKey)
            .build()
        val request = chain.request()
            .newBuilder()
            .url(url)
            .build()
        chain.proceed(request)
    }

    @Provides
    @Singleton
    fun provideOkHttpLog(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }


    @Provides
    @Singleton
    fun provideOkHttpClient(
        interceptor: Interceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .readTimeout(15, TimeUnit.SECONDS)
        .connectTimeout(15, TimeUnit.SECONDS)
        .addInterceptor(httpLoggingInterceptor)
        .addInterceptor(interceptor)
        .build()

    @Provides
    @Singleton
    fun provideGsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Provides
    @Singleton
    fun provideRetrofit(
        client: OkHttpClient,
        @BaseUrl baseUrl: String,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(gsonConverterFactory)
            .build()

    @Singleton
    @Provides
    fun provideMovieService(retrofit: Retrofit): FoodService = provideApi<FoodService>(retrofit)

    @Singleton
    @Provides
    fun provideRemoteDataSource(movieService: FoodService): RemoteDataSource =
        RemoteDataSource(movieService)
}