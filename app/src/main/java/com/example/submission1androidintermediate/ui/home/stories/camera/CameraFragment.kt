package com.example.submission1androidintermediate.ui.home.stories.camera

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.view.LayoutInflater
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.submission1androidintermediate.R
import com.example.submission1androidintermediate.base.BaseFragment
import com.example.submission1androidintermediate.databinding.FragmentCameraBinding
import com.example.submission1androidintermediate.helper.AppUtils.showToast
import com.example.submission1androidintermediate.helper.ImageUtils
import com.example.submission1androidintermediate.ui.home.stories.SharedStoriesViewModel
import com.github.ajalt.timberkt.Timber
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@AndroidEntryPoint
class CameraFragment : BaseFragment<FragmentCameraBinding>() {
    private var preview: Preview? = null
    val sharedStoryViewModel: SharedStoriesViewModel by hiltNavGraphViewModels(R.id.story_nav_graph)
    private var imageCapture: ImageCapture? = null
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private var camera: Camera? = null
    private lateinit var safeContext: Context
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    override val setLayout: (LayoutInflater) -> FragmentCameraBinding
        get() = {
            FragmentCameraBinding.inflate(layoutInflater)
        }

    override fun observeViewModel() {
        // Do nothing
    }

    override fun init() {
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            Timber.d {
                getString(R.string.label_request_camera_permission)
            }
            requestPermission.launch(Manifest.permission.CAMERA)
        }
        binding.captureImage.setOnClickListener { takePhoto() }
        binding.switchCamera.setOnClickListener {
            cameraSelector =
                if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) CameraSelector.DEFAULT_FRONT_CAMERA
                else CameraSelector.DEFAULT_BACK_CAMERA

            startCamera()
        }
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO){
            outputDirectory = ImageUtils.getOutputDirectory(requireActivity())
        }

        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    private fun allPermissionsGranted(): Boolean {
        return REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(safeContext, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        val photoFile = File(
            outputDirectory,
            SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis()) + ".jpg"
        )
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(safeContext),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Timber.d {
                        getString(R.string.label_failed_camera_capture)
                    }
                    showToast(getString(R.string.label_failed_camera_capture))
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    val msg = getString(R.string.label_success_capture_camera)
                    sharedStoryViewModel.saveImageResult(
                        photoFile,
                        cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA
                    )
                    findNavController().popBackStack()
                    Timber.d {
                        "$msg $savedUri.toString()"
                    }
                }
            }
        )
    }


    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                Timber.d { getString(R.string.label_camera_granted) }
                startCamera()
            } else {
                Timber.d { getString(R.string.label_camera_denied) }
            }
        }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        safeContext = context
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(safeContext)

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            preview = Preview.Builder().build()

            imageCapture = ImageCapture.Builder().build()

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                camera = cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture
                )
                preview?.setSurfaceProvider(binding.viewFinder.surfaceProvider)
            } catch (exc: Exception) {
                Timber.d {
                    getString(R.string.label_error_use_case_binding)
                }
            }

        }, ContextCompat.getMainExecutor(safeContext))

    }

    companion object {
        const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}



