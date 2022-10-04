package com.example.submission1androidintermediate.ui.home

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.domain.utils.NetworkResult
import com.example.submission1androidintermediate.R
import com.example.submission1androidintermediate.base.BaseFragment
import com.example.submission1androidintermediate.databinding.FragmentHomeBinding
import com.example.submission1androidintermediate.ui.camera.CameraFragment
import com.example.submission1androidintermediate.ui.login.LoginFragment
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    private val viewModel: HomeViewModel by viewModels()

    override val setLayout: (LayoutInflater) -> FragmentHomeBinding
        get() = {
            FragmentHomeBinding.inflate(layoutInflater)
        }


    override fun observeViewModel() {
        viewModel.storiesResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResult.Success -> {
                    Timber.d(result.data?.data.toString())
                }
                is NetworkResult.Error -> {
                    Timber.d(result.message.toString())
                }
                is NetworkResult.Loading -> {}
            }


        }


    }

    override fun init() {
        viewModel.getStories()
        binding.btnCamera.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_cameraFragment)
        }

    }

}

