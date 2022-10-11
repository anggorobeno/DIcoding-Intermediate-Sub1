package com.example.submission1androidintermediate.ui.home

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.core.di.CoroutinesQualifier
import com.example.domain.utils.NetworkResult
import com.example.submission1androidintermediate.R
import com.example.submission1androidintermediate.base.BaseFragment
import com.example.submission1androidintermediate.databinding.FragmentHomeBinding
import com.example.submission1androidintermediate.helper.AppUtils.navigateToDestination
import com.example.submission1androidintermediate.helper.AppUtils.showToast
import com.example.submission1androidintermediate.helper.StoriesEvent
import com.example.submission1androidintermediate.ui.home.adapter.HomeStoryAdapter
import com.google.android.material.transition.MaterialElevationScale
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    private val viewModel: HomeViewModel by viewModels()
    private val adapter: HomeStoryAdapter = HomeStoryAdapter()

    @Inject
    @CoroutinesQualifier.MainDispatcher
    lateinit var mainDispatcher: CoroutineDispatcher

    @Inject
    @CoroutinesQualifier.IoDispatcher
    lateinit var ioDispatcher: CoroutineDispatcher

    override val setLayout: (LayoutInflater) -> FragmentHomeBinding
        get() = {
            FragmentHomeBinding.inflate(layoutInflater)
        }

    private fun showLoadingState(isLoading: Boolean) {
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }
        super.onViewCreated(view, savedInstanceState)

    }


    override fun init() {
        setupMenu()
        setupAdapter()
        setupView()
        exitTransition = MaterialElevationScale(false).apply {
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
        }
        reenterTransition = MaterialElevationScale(true).apply {
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
        }

    }

    private fun setupMenu() {

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {

                if (menuItem.itemId == R.id.menu_logout) {
                    viewLifecycleOwner.lifecycleScope.launch(ioDispatcher) {
                        preferencesDataStore.clear()
                        withContext(mainDispatcher) {
                            val navOption = NavOptions.Builder()
                                .setPopUpTo(R.id.homeFragment, true)
                                .setEnterAnim(R.anim.fade_in_left)
                                .setExitAnim(R.anim.slide_out_right)
                                .build()
                            navigateToDestination(
                                dest = R.id.welcomeFragment,
                                navOptions = navOption
                            )
                        }
                    }

                }
                return true
            }
        }, viewLifecycleOwner)
    }

    private fun setupView() {
        binding.mainFab.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToAddStoryFragment()
            val extras = FragmentNavigatorExtras(
                binding.mainFab to "fab_to_add",
            )
            findNavController().navigate(
                action,
                extras
            )
        }
        binding.fabCamera.setOnClickListener {
            navigateToDestination(R.id.action_homeFragment_to_addStoryFragment)
        }
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.getStories()
            binding.swipeRefreshLayout.isRefreshing = true
        }
    }

    private fun setupAdapter() {
        binding.rvStory.apply {
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            adapter = this@HomeFragment.adapter
        }
        adapter.onClickCallback = { item, binding ->
            val action = HomeFragmentDirections.actionHomeFragmentToDetailStoryFragment(item)
            val extras = FragmentNavigatorExtras(
                binding.cardView to "card_view_to_detail",
            )
            findNavController().navigate(action,extras)
        }
    }

}

