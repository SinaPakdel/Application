package com.example.application.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.application.data.network.ImageResponse
import com.example.application.data.network.ImageService
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class ImageViewModel @Inject constructor(
    private val imageService: ImageService
) : ViewModel() {

    private val _images = MutableLiveData<List<ImageResponse>>()
    val images: LiveData<List<ImageResponse>> = _images


    init {
        getImage()
    }

    private fun getImage() = imageService.getAllImage().enqueue(object : Callback<List<ImageResponse>> {
        override fun onResponse(call: Call<List<ImageResponse>>, response: Response<List<ImageResponse>>) {
            _images.postValue(response.body())
        }

        override fun onFailure(call: Call<List<ImageResponse>>, t: Throwable) {

        }
    })

}