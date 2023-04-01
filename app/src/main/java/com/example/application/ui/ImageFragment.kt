package com.example.application.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.application.R
import com.example.application.databinding.FragmentImageBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ImageFragment : Fragment(R.layout.fragment_image) {
    private lateinit var binding: FragmentImageBinding
    private val viewModel: ImageViewModel by viewModels()
    private lateinit var imageAdapter: ImageAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentImageBinding.bind(view)


        viewModel.images.observe(viewLifecycleOwner) { list ->
            Log.e("ImageFragment", "onViewCreated: $list")
            imageAdapter = ImageAdapter()
            imageAdapter.submitList(list)

        }
    }
}