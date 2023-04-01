package com.example.application.data.network

import retrofit2.Call
import retrofit2.http.GET

interface ImageService {

    companion object {
        //const val BASE_URL = "https://countryapi.io/api/all?apikey=w2QXzafYgRur9WlZVm7vPfIFjm0pprUY5oLIOa4g"
        const val BASE_URL = "https://picsum.photos/v2/"
//        const val API_KEY = "w2QXzafYgRur9WlZVm7vPfIFjm0pprUY5oLIOa4g"
    }

    @GET("list")
    fun getAllImage(): Call<List<ImageResponse>>
}