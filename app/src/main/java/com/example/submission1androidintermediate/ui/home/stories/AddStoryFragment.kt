package com.example.submission1androidintermediate.ui.home.stories

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.core.di.CoroutinesQualifier
import com.example.domain.utils.NetworkResult
import com.example.submission1androidintermediate.R
import com.example.submission1androidintermediate.base.BaseFragment
import com.example.submission1androidintermediate.databinding.FragmentAddStoryBinding
import com.example.submission1androidintermediate.helper.AppUtils.getGlideRequestOption
import com.example.submission1androidintermediate.helper.AppUtils.navigateToDestination
import com.example.submission1androidintermediate.helper.AppUtils.showToast
import com.example.submission1androidintermediate.helper.ImageUtils
import com.example.submission1androidintermediate.helper.StoriesEvent
import com.google.android.material.transition.MaterialContainerTransform
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class AddStoryFragment : BaseFragment<FragmentAddStoryBinding>() {
    private val sharedViewModel: SharedStoriesViewModel by hiltNavGraphViewModels(R.id.story_nav_graph)
    private val requiredPermission = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    @Inject
    @CoroutinesQualifier.MainDispatcher
    lateinit var mainCoroutinesDispatcher: CoroutineDispatcher

    @Inject
    @CoroutinesQualifier.IoDispatcher
    lateinit var ioDispatcher: CoroutineDispatcher
    private lateinit var imageFile: File
    override val setLayout: (LayoutInflater) -> FragmentAddStoryBinding
        get() = {
            FragmentAddStoryBinding.inflate(layoutInflater)
        }

    private val launcherIntentGallery =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            lifecycleScope.launch(ioDispatcher) {
                if (uri == null) return@launch
                val myFile = uri.let {
                    ImageUtils.reduceFileImage(
                        ImageUtils.uriToFile(
                            it,
                            requireActivity()
                        )
                    )
                }
                imageFile = myFile
                withContext(mainCoroutinesDispatcher) {
                    Glide.with(requireContext())
                        .load(uri)
                        .apply(getGlideRequestOption(requireContext()))
                        .into(binding.imageView)
                }
            }
        }

    private fun allPermissionsGranted(): Boolean {
        return requiredPermission.all {
            ContextCompat.checkSelfPermission(
                requireContext(),
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postponeEnterTransition()
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
            drawingViewId = R.id.nav_host_fragment
            scrimColor = Color.TRANSPARENT
            setAllContainerColors(ResourcesCompat.getColor(resources,R.color.white,null))
        }
    }

    override fun onStart() {
        super.onStart()
        (requireView().parent as ViewGroup).doOnPreDraw {
            startPostponedEnterTransition()
        }
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
                }
            }
        }
        sharedViewModel.imageBitmap.observe(viewLifecycleOwner) {
            viewLifecycleOwner.lifecycleScope.launch(ioDispatcher) {
                ImageUtils.bitmapToFile(it, requireActivity())?.let { file ->
                    imageFile = file
                }
                withContext(mainCoroutinesDispatcher) {
                    Glide.with(requireContext())
                        .load(it)
                        .apply(getGlideRequestOption(requireContext()))
                        .into(binding.imageView)
                }
            }
        }
    }

    private fun showLoadingState(isLoading: Boolean) {
        binding.layoutProgressBar.progressCircular.isVisible = isLoading
    }

    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permission ->
            var isAllGranted = true
            permission.entries.forEach { (_, value) ->
                if (!value) {
                    isAllGranted = false
                    return@registerForActivityResult
                }
            }
            if (isAllGranted) launcherIntentGallery.launch("image/*")


        }


    override fun init() {
        binding.fabCamera.setOnClickListener {
            navigateToDestination(R.id.action_addStoryFragment_to_cameraFragment)
        }
        binding.fabGallery.setOnClickListener {
            if (allPermissionsGranted()) {
                launcherIntentGallery.launch("image/*")
            } else {
                requestPermission.launch(requiredPermission)
            }
        }

        binding.btnUpload.setOnClickListener {
            if (binding.etDescription.text.toString().isNotEmpty() && ::imageFile.isInitialized
            ) {
                sharedViewModel.uploadImage(binding.etDescription.text.toString(), imageFile)
            }
        }
    }
}
