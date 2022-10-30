package com.example.submission1androidintermediate.ui.home.stories

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.transition.Slide
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
import com.github.ajalt.timberkt.Timber
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.material.transition.MaterialContainerTransform
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus
import java.io.File
import java.io.IOException
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class AddStoryFragment : BaseFragment<FragmentAddStoryBinding>() {
    private val sharedViewModel: SharedStoriesViewModel by hiltNavGraphViewModels(R.id.story_nav_graph)
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mMap: GoogleMap
    private lateinit var imageFile: File
    private var lat = 0.0
    private var lon = 0.0
    private val requiredPermissionToPostStory = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    private val requiredLocationPermission = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    @Inject
    @CoroutinesQualifier.MainDispatcher
    lateinit var mainCoroutinesDispatcher: CoroutineDispatcher

    @Inject
    @CoroutinesQualifier.IoDispatcher
    lateinit var ioDispatcher: CoroutineDispatcher
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

    private fun allPostStoryPermissionsGranted(): Boolean {
        return requiredPermissionToPostStory.all {
            ContextCompat.checkSelfPermission(
                requireContext(),
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun allLocationPermissionGranted() = requiredLocationPermission.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
                val bitmap = BitmapFactory.decodeFile(it.image.path)
                val rotatedBitmap = ImageUtils.rotateBitmap(bitmap, it.isBackCamera)
                ImageUtils.bitmapToFile(rotatedBitmap, requireActivity())?.let { file ->
                    imageFile = ImageUtils.reduceFileImage(file)
                }
                withContext(mainCoroutinesDispatcher) {
                    Glide.with(requireContext())
                        .load(rotatedBitmap)
                        .apply(getGlideRequestOption(requireContext()))
                        .into(binding.imageView)
                }
            }
        }
    }

    private fun showLoadingState(isLoading: Boolean) {
        binding.layoutProgressBar.progressCircular.isVisible = isLoading
    }

    private val requestPostStoryPermission =
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


    private val requestLocationPermission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    // Precise location access granted.
                    getMyLastLocation()
                }
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    // Only approximate location access granted.
                    getMyLastLocation()
                }
                else -> {
                    // No location access granted.
                }
            }

        }

    private fun getMyLastLocation() {
        if (allLocationPermissionGranted()) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    lat = location.latitude
                    lon = location.longitude
                    setAddressName(location.latitude, location.longitude)
                } else {
                    showToast("Location is not found. Try again")
                }
            }
        } else {
            requestLocationPermission.launch(requiredLocationPermission)
        }
    }

    private fun setAddressName(lat: Double, lon: Double): String? {
        var addressName: String? = null
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        try {
            val list = geocoder.getFromLocation(lat, lon, 1)
            if (list != null && list.size != 0) {
                addressName = list[0].getAddressLine(0)
                binding.tvCurrentLocation.text = addressName
                Timber.d { "getAddressName: $addressName" }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return addressName
    }

    private fun launchGallery() {
        if (allPostStoryPermissionsGranted()) {
            launcherIntentGallery.launch("image/*")
        } else {
            requestPostStoryPermission.launch(requiredPermissionToPostStory)
        }
    }


    private fun updateButtonState() {
        binding.btnUpload.isEnabled = binding.etDescription.text.toString()
            .isNotEmpty() && ::imageFile.isInitialized && lon != 0.0 && lat != 0.0
    }

    private fun setTransition() {
        /*
         * setEnterTransition if start view is view from activity to fragment end view
         * setSharedElementTransition if start view if from fragment to fragment end view
         */
        enterTransition = MaterialContainerTransform().apply {
            startView = activity?.findViewById(R.id.fab_add_story)
            endView = binding.clAddStory
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
            drawingViewId = binding.clAddStory.id
            scrimColor = Color.TRANSPARENT
            setAllContainerColors(ResourcesCompat.getColor(resources, R.color.transparent, null))
        }
        returnTransition = Slide()
    }

    override fun init() {
        setTransition()
        updateButtonState()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        binding.fabCamera.setOnClickListener {
            navigateToDestination(R.id.action_addStoryFragment_to_cameraFragment)
            updateButtonState()
        }
        binding.tvCurrentLocation.setOnClickListener {
            getMyLastLocation()
            updateButtonState()
        }
        binding.fabGallery.setOnClickListener {
            launchGallery()
            updateButtonState()
        }

        binding.etDescription.doOnTextChanged { _, _, _, _ ->
            updateButtonState()
        }
        binding.btnUpload.setOnClickListener {
            if (binding.etDescription.text.toString()
                    .isNotEmpty() && ::imageFile.isInitialized && lon != 0.0 && lat != 0.0
            ) {
                sharedViewModel.uploadImage(
                    binding.etDescription.text.toString(), imageFile,
                    lat.toString(), lon.toString()
                )
            }
        }
    }
}
