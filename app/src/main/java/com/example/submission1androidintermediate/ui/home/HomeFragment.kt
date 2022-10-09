package com.example.submission1androidintermediate.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.utils.NetworkResult
import com.example.submission1androidintermediate.R
import com.example.submission1androidintermediate.base.BaseFragment
import com.example.submission1androidintermediate.databinding.FragmentHomeBinding
import com.example.submission1androidintermediate.helper.AppUtils.navigateToDestination
import com.example.submission1androidintermediate.helper.AppUtils.showToast
import com.example.submission1androidintermediate.helper.AppUtils.smoothSnapToPosition
import com.example.submission1androidintermediate.helper.StoriesEvent
import com.example.submission1androidintermediate.ui.adapter.HomeStoryAdapter
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
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

    @Subscribe
    fun onEventReceived(event: StoriesEvent) {
        if (event.message == "onStoriesUploadSuccess") {
            viewModel.getStories()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    override fun observeViewModel() {
        viewModel.storiesResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResult.Success -> {
                    showLoadingState(false)
                    result.data?.let { adapter.setList(it) }
                    Timber.d(result.data?.data.toString())
                    (view?.parent as ViewGroup).doOnPreDraw {
                        startPostponedEnterTransition()
                    }
                    binding.swipeRefreshLayout.isRefreshing = false
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
        viewModel.toastText.observe(viewLifecycleOwner) { message ->
            message?.getContentIfNotHandled()?.let {
                showToast(it)
            }
        }


    }


    override fun init() {
        postponeEnterTransition()
        setupAdapter()
        setupView()
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.getStories()
            binding.swipeRefreshLayout.isRefreshing = true
        }
    }

    private fun setupView() {
        binding.fabCamera.setOnClickListener {
            navigateToDestination(R.id.action_homeFragment_to_addStoryFragment)
        }


    }

    private fun setupAdapter() {
        binding.rvStory.apply {
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            adapter = this@HomeFragment.adapter
        }
        adapter.onClickCallback = { item, imageViewStory, textView, imageViewProfile ->
            val action = HomeFragmentDirections.actionHomeFragmentToDetailStoryFragment(item)
            imageViewStory.transitionName = item.photoUrl
            imageViewProfile.transitionName = item.id
            textView.transitionName = item.id
            val extras = FragmentNavigatorExtras(
                imageViewStory to item.photoUrl.toString(),
                textView to item.id.toString(),
            )
            findNavController().navigate(directions = action, navigatorExtras = extras)
        }
    }

}

