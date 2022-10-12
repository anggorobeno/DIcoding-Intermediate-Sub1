package com.example.submission1androidintermediate

import android.os.Bundle
import android.os.StrictMode
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.core.data.local.PreferencesDataStore
import com.example.submission1androidintermediate.databinding.ActivityMainBinding
import com.github.ajalt.timberkt.Timber
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController

    @Inject
    lateinit var preferencesDataStore: PreferencesDataStore
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        StrictMode.enableDefaults()
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Timber.d {
            "Activity onCreate called"
        }
        val navHost =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
        navController = navHost!!.navController
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.homeFragment))
        binding.toolbar.setupWithNavController(
            navController,
            appBarConfiguration
        )
        setSupportActionBar(binding.toolbar)

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
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}