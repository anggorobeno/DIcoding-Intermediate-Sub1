package com.example.submission1androidintermediate.ui.home

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.utils.NetworkResult
import com.example.domain.utils.SingleEvent
import com.example.submission1androidintermediate.R
import com.example.submission1androidintermediate.base.BaseFragment
import com.example.submission1androidintermediate.databinding.FragmentHomeBinding
import com.example.submission1androidintermediate.helper.AppUtils.showToast
import com.example.submission1androidintermediate.ui.adapter.HomeStoryAdapter
import com.example.submission1androidintermediate.ui.camera.CameraFragment
import com.example.submission1androidintermediate.ui.login.LoginFragment
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    private val viewModel: HomeViewModel by viewModels()
    private val adapter: HomeStoryAdapter = HomeStoryAdapter()
    override val setLayout: (LayoutInflater) -> FragmentHomeBinding
        get() = {
            FragmentHomeBinding.inflate(layoutInflater)
        }

    fun showLoadingState(isLoading: Boolean) {
        binding.layoutProgressBar.progressCircular.isVisible = isLoading
    }

    override fun observeViewModel() {
        viewModel.storiesResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResult.Success -> {
                    SingleEvent(result.data?.message).getContentIfNotHandled()?.let {
                        showToast(it)
                    }
                    showLoadingState(false)
                    result.data?.let { adapter.setList(it) }
                    Timber.d(result.data?.data.toString())
                }
                is NetworkResult.Error -> {
                    result.message?.getContentIfNotHandled()?.let { message ->
                        showToast(message)
                    }
                    showLoadingState(false)
                    Timber.d(result.message.toString())
                }
                is NetworkResult.Loading -> {
                    showLoadingState(true)
                }
            }


        }


    }


    override fun init() {
        viewModel.getStories()
        setupAdapter()
        setupView()
//        binding.btnCamera.setOnClickListener {
//            findNavController().navigate(R.id.action_homeFragment_to_cameraFragment)
//        }

    }

    private fun setupView() {
        binding.fabAddStory.setOnClickListener {
            Timber.d("Fab Clicked")
        }
    }

    private fun setupAdapter() {
        binding.rvStory.apply {
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            adapter = this@HomeFragment.adapter
        }
        adapter.onClickCallback = {

        }
    }

}

