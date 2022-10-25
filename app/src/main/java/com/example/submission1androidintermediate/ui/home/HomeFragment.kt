package com.example.submission1androidintermediate.ui.home

import android.os.Bundle
import android.view.*
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.map
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.core.di.CoroutinesQualifier
import com.example.domain.model.stories.StoriesModel
import com.example.submission1androidintermediate.R
import com.example.submission1androidintermediate.base.BaseFragment
import com.example.submission1androidintermediate.databinding.FragmentHomeBinding
import com.example.submission1androidintermediate.helper.AppUtils.navigateToDestination
import com.example.submission1androidintermediate.helper.AppUtils.showToast
import com.example.submission1androidintermediate.helper.StoriesEvent
import com.example.submission1androidintermediate.ui.home.adapter.HomeStoryPagingAdapter
import com.example.submission1androidintermediate.ui.home.adapter.LoadingStateAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.transition.MaterialElevationScale
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    private val viewModel: HomeViewModel by viewModels()
    private var homeStoryPagingAdapter: HomeStoryPagingAdapter? = null

    @Inject
    @CoroutinesQualifier.MainDispatcher
    lateinit var mainDispatcher: CoroutineDispatcher

    @Inject
    @CoroutinesQualifier.IoDispatcher
    lateinit var ioDispatcher: CoroutineDispatcher

    override val setLayout: (LayoutInflater) -> FragmentHomeBinding
        get() = {
            FragmentHomeBinding.inflate(it)
        }

    @Subscribe
    fun onEventReceived(event: StoriesEvent) {
        if (event.message == "onStoriesUploadSuccess") {
            refreshContent()
            scrollToTop(0)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true).apply {
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
        }
        exitTransition = MaterialElevationScale(false).apply {
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
        }
        reenterTransition = MaterialElevationScale(true).apply {
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
        }
        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    override fun onDestroyView() {
        homeStoryPagingAdapter = null
        super.onDestroyView()
    }

    override fun observeViewModel() {
        viewModel.storiesPagingResult.observe(viewLifecycleOwner) {
            homeStoryPagingAdapter?.submitData(viewLifecycleOwner.lifecycle, it)
        }
    }

    override fun onStart() {
        super.onStart()
        (view?.parent as ViewGroup).doOnPreDraw { startPostponedEnterTransition() }
    }


    override fun init() {
        postponeEnterTransition()
        setupMenu()
        setupAdapter()
        setupView()
    }

    private fun setupMenu() {

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.home_action_bar_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {

                if (menuItem.itemId == R.id.menu_logout) {
                    viewLifecycleOwner.lifecycleScope.launch(ioDispatcher) {
                        preferencesDataStore.clear()
                        withContext(mainDispatcher) {
                            navigateToDestination(
                                dest = R.id.action_homeFragment_to_welcomeFragment,
                            )
                        }
                    }

                }
                return true
            }
        }, viewLifecycleOwner)
    }

    private fun setupView() {
        val nav = activity?.findViewById<BottomNavigationView>(R.id.bottom_nav)
        nav?.setOnItemReselectedListener { item ->
            if (item.itemId == R.id.homeFragment) {
                scrollToTop(0)
            }
        }
        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.swipeRefreshLayout.isRefreshing = false
            homeStoryPagingAdapter?.refresh()
        }

    }

    private fun scrollToTop(position: Int) {
        binding.rvStory.smoothScrollToPosition(position)
    }

    private fun refreshContent() {
        viewModel.getStoriesPaging()
    }

    private fun setupAdapter() {
        homeStoryPagingAdapter = HomeStoryPagingAdapter()
        binding.layoutEmptyState.btnRetry.setOnClickListener {
            refreshContent()
        }
        homeStoryPagingAdapter?.onClickCallback = { item, binding ->
            val action = HomeFragmentDirections.actionHomeFragmentToDetailStoryFragment(item)
            val extras = FragmentNavigatorExtras(
                binding.cardView to "card_view_to_detail",
            )
            findNavController().navigate(action, extras)
        }
        homeStoryPagingAdapter?.addLoadStateListener(::updateUi)
        binding.rvStory.apply {
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            adapter = homeStoryPagingAdapter?.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    homeStoryPagingAdapter?.retry()
                }
            )
        }

    }

    private fun updateUi(combinedLoadStates: CombinedLoadStates) {
        binding.apply {
            when (combinedLoadStates.source.refresh) {
                is LoadState.Loading -> {
                    layoutProgressBar.progressCircular.isVisible = true
                }
                is LoadState.Error -> {
                    // Show Error State
                    layoutProgressBar.progressCircular.isVisible = false
                    llEmptyState.isVisible = true
                    rvStory.isVisible = false
                }
                is LoadState.NotLoading -> {
                    // Show Success State
                    layoutProgressBar.progressCircular.isVisible = false
                    rvStory.isVisible = true
                    llEmptyState.isVisible = false

                    // Show Empty State
                    if (combinedLoadStates.append.endOfPaginationReached && homeStoryPagingAdapter!!.itemCount < 1) {
                        llEmptyState.isVisible = true
                        rvStory.isVisible = false
                        layoutEmptyState.apply {
                            ivErrorImage.setImageDrawable(
                                ResourcesCompat.getDrawable(
                                    resources,
                                    R.drawable.error_empty,
                                    null
                                )
                            )
                            tvTitle.text = getString(R.string.label_empty_state_title)
                            tvSubtitle.text = getString(R.string.label_empty_state_subtitle)
                        }
                    }
                }

            }
        }
    }

}

