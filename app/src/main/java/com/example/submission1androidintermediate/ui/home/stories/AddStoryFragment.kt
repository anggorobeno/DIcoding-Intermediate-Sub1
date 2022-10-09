package com.example.submission1androidintermediate.ui.home.stories

import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.view.isVisible
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.domain.utils.NetworkResult
import com.example.submission1androidintermediate.R
import com.example.submission1androidintermediate.base.BaseFragment
import com.example.submission1androidintermediate.databinding.FragmentAddStoryBinding
import com.example.submission1androidintermediate.helper.AppUtils
import com.example.submission1androidintermediate.helper.AppUtils.navigateToDestination
import com.example.submission1androidintermediate.helper.AppUtils.showToast
import com.example.submission1androidintermediate.helper.ImageUtils
import com.example.submission1androidintermediate.helper.StoriesEvent
import com.example.submission1androidintermediate.ui.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.io.File

@AndroidEntryPoint
class AddStoryFragment : BaseFragment<FragmentAddStoryBinding>() {
    private val sharedViewModel: SharedStoriesViewModel by hiltNavGraphViewModels(R.id.story_nav_graph)
    private lateinit var imageFile: File
    override val setLayout: (LayoutInflater) -> FragmentAddStoryBinding
        get() = {
            FragmentAddStoryBinding.inflate(layoutInflater)
        }

    override fun observeViewModel() {
        sharedViewModel.storiesUploadResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResult.Loading -> {
                    showLoadingState(true)
                }
                is NetworkResult.Error -> {
                    showLoadingState(false)
                    result.message?.let {
                        it.getContentIfNotHandled()?.let { message ->
                            showToast(message)
                        }
                    }
                }
                is NetworkResult.Success -> {
                    showLoadingState(false)
                    EventBus.getDefault().post(StoriesEvent("onStoriesUploadSuccess"))
                    findNavController().popBackStack()
                    showToast(result.data?.message.toString())
                }
            }
        }
        sharedViewModel.imageBitmap.observe(viewLifecycleOwner) {
            ImageUtils.bitmapToFile(it, getOutputDirectory())?.let { file ->
                imageFile = file
            }
            Glide.with(requireContext())
                .load(it)
                .into(binding.imageView)
        }
    }

    private fun showLoadingState(isLoading: Boolean) {
        binding.layoutProgressBar.progressCircular.isVisible = isLoading
    }

    private fun getOutputDirectory(): File {
        val mediaDir = activity?.externalMediaDirs?.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists()) mediaDir else activity?.filesDir!!
    }

    override fun init() {
        binding.btncamera.setOnClickListener {
            navigateToDestination(R.id.action_addStoryFragment_to_cameraFragment)
        }
        binding.btnUpload.setOnClickListener {
            if (!binding.etDescription.text.toString()
                    .isNullOrEmpty() && ::imageFile.isInitialized
            ) {
                sharedViewModel.uploadImage(binding.etDescription.text.toString(), imageFile)

            }
        }
    }
}
