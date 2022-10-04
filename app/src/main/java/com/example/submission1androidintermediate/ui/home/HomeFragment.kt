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
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.submission1androidintermediate.R
import com.example.submission1androidintermediate.base.BaseFragment
import com.example.submission1androidintermediate.databinding.FragmentHomeBinding
import com.example.submission1androidintermediate.ui.camera.CameraFragment
import com.example.submission1androidintermediate.ui.login.LoginFragment
import timber.log.Timber

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    override val setLayout: (LayoutInflater) -> FragmentHomeBinding
        get() = {
            FragmentHomeBinding.inflate(layoutInflater)
        }



    override fun observeViewModel() {

    }

    override fun init() {
        binding.btnCamera.setOnClickListener {
             findNavController().navigate(R.id.action_homeFragment_to_cameraFragment)
        }

    }

}

