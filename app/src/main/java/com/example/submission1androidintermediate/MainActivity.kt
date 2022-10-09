package com.example.submission1androidintermediate

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.core.data.local.PreferencesDataStore
import com.example.submission1androidintermediate.databinding.ActivityMainBinding
import com.github.ajalt.timberkt.Timber
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var preferencesDataStore: PreferencesDataStore
    var isLoggedIn = false
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Timber.d {
            "Activity onCreate called"
        }
        val navHost =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
        val navController = navHost!!.navController
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.homeFragment))
        binding.toolbar.setupWithNavController(
            navController,
            appBarConfiguration
        )
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val noToolbarDestination =
                setOf(
                    R.id.welcomeFragment,
                    R.id.loginFragment,
                    R.id.registerFragment,
                    R.id.cameraFragment
                )
            binding.toolbar.isVisible = !noToolbarDestination.contains(destination.id)
        }

        val navInflater = navController.navInflater
        val graph = navInflater.inflate(R.navigation.nav_graph)


    }


}