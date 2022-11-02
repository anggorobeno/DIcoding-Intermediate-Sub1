package com.example.submission1androidintermediate.ui.home.map

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Resources
import android.view.LayoutInflater
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.example.domain.model.stories.MapModel
import com.example.domain.utils.NetworkResult
import com.example.submission1androidintermediate.R
import com.example.submission1androidintermediate.base.BaseFragment
import com.example.submission1androidintermediate.databinding.FragmentMapsBinding
import com.example.submission1androidintermediate.helper.AppUtils.showToast
import com.github.ajalt.timberkt.Timber
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.ktx.addMarker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapsFragment : BaseFragment<FragmentMapsBinding>() {
    private lateinit var map: GoogleMap
    private val viewModel: MapViewModel by viewModels()

    private val requiredPermission = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private fun allPermissionsGranted(): Boolean {
        return requiredPermission.all {
            ContextCompat.checkSelfPermission(
                requireContext(),
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    private val requestLocationPermission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permission ->
            when {
                permission[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    getDeviceLocation()

                }
                permission[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    getDeviceLocation()
                }
                else -> {
                    Timber.d {
                        "Location Permission denied"
                    }
                    shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)
                }
            }


        }

    private fun getDeviceLocation() {
        if (allPermissionsGranted()) {
            map.isMyLocationEnabled = true
        } else requestLocationPermission.launch(requiredPermission)
    }

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        map = googleMap
        map.uiSettings.apply {
            isZoomControlsEnabled = true
            isMapToolbarEnabled = true
            isMyLocationButtonEnabled = true
        }
        getDeviceLocation()
        setMapStyle()
    }

    private fun addMarkers(listMap: List<MapModel?>) {
        if (listMap.isEmpty()) return
        val boundsBuilder = LatLngBounds.Builder()
        listMap.forEach { data ->
            val lat = data?.lat ?: return
            val lon = data.lon ?: return
            val latLng = LatLng(lat, lon)
            boundsBuilder.include(latLng)
            map.addMarker {
                position(latLng)
                title(data.name)
            }
        }
        val bounds: LatLngBounds = boundsBuilder.build()
        map.animateCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds,
                resources.displayMetrics.widthPixels,
                resources.displayMetrics.heightPixels,
                300
            )
        )
    }

    private fun setMapStyle() {
        try {
            val success =
                map.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                        requireActivity(),
                        R.raw.map_style
                    )
                )
            if (!success) {
                Timber.d { "Style parsing failed." }
            }
        } catch (exception: Resources.NotFoundException) {
            Timber.d { "Can't find style. Error: $exception " }
        }
    }

    override val setLayout: (LayoutInflater) -> FragmentMapsBinding
        get() = {
            FragmentMapsBinding.inflate(it)
        }

    override fun observeViewModel() {
        viewModel.getStoriesLocation().observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResult.Success -> {
                    addMarkers(viewModel.getMapModel(result.data?.data ?: emptyList()))
                }
                is NetworkResult.Error -> {
                    showToast("Error while getting markers")
                }
                is NetworkResult.Loading -> {
                    // Do nothing
                }
            }
        }
    }

    override fun initView() {
        val mapFragment: SupportMapFragment? =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}