package com.example.submission1androidintermediate

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.core.data.local.IDataStore
import com.example.submission1androidintermediate.databinding.ActivityMainBinding
import com.example.submission1androidintermediate.helper.AppUtils.marginInDp
import com.github.ajalt.timberkt.Timber
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    private val noToolbarDestination =
        setOf(
            R.id.welcomeFragment,
            R.id.loginFragment,
            R.id.registerFragment,
            R.id.cameraFragment,
            R.id.mapsFragment
        )
    private val authDestination =
        setOf(
            R.id.welcomeFragment,
            R.id.loginFragment,
            R.id.registerFragment,
        )
    private val noBottomBarDestination =
        setOf(
            R.id.cameraFragment,
        )
    private val noFabDestination =
        setOf(
            R.id.mapsFragment,
            R.id.addStoryFragment,
            R.id.detailStoryFragment
        )

    @Inject
    lateinit var preferencesDataStore: IDataStore
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
        navController = navHost!!.navController
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.homeFragment))
        /*
            set up toolbar so navigation back icon is automatically visible
         */
        binding.toolbar.setupWithNavController(
            navController,
            appBarConfiguration
        )
        setSupportActionBar(binding.toolbar)
        /*
            fix for navigation back button not reacting to user click when activity restart
         */
        binding.toolbar.setNavigationOnClickListener { _ ->
            NavigationUI.navigateUp(
                navController,
                appBarConfiguration
            )
        }
        binding.bottomNav.setupWithNavController(navController)
        binding.bottomNav.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.mapsFragment -> {
                    navController.navigate(R.id.mapsFragment)
                    true
                }
                R.id.homeFragment -> {
                    navController.navigate(R.id.homeFragment)
                    true
                }
                else -> {
                    false
                }
            }
        }
        binding.bottomNav.setOnItemReselectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.mapsFragment -> {
                    /* No op to prevent fragment recreate on reselected */

                }
                R.id.homeFragment -> {
                    /* No op to prevent fragment recreate on reselected */

                }
            }

        }
        binding.fabAddStory.apply {
            setShowMotionSpecResource(R.animator.fab_show)
            setHideMotionSpecResource(R.animator.fab_hide)
            setOnClickListener {
                navController.navigate(
                    R.id.action_homeFragment_to_addStoryFragment
                )
            }
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            updateMargin(destination)
            binding.toolbar.isVisible = !noToolbarDestination.contains(destination.id)

            if (noBottomBarDestination.contains(destination.id)) {
                showFab(false)
                hideBottomAppBar()
            } else if (noFabDestination.contains(destination.id)) {
                showFab(false)
                showBottomBar()
            } else if (authDestination.contains(destination.id)) {
                showFab(false)
                hideBottomAppBar()
            } else {
                showFab(true)
                showBottomBar()
            }

        }
    }

    private fun updateMargin(destination: NavDestination) {
        /*
        update margin to hide empty space after hiding bottom app bar / toolbar
         */
        val marginLayoutParams =
            binding.navHostFragment.layoutParams as ViewGroup.MarginLayoutParams
        if (!noToolbarDestination.contains(destination.id)) {
            marginLayoutParams.setMargins(0, 56f.marginInDp(this), 0, 0)
        } else marginLayoutParams.setMargins(0, 0, 0, 56f.marginInDp(this))
        binding.navHostFragment.layoutParams = marginLayoutParams
    }



    private fun hideBottomAppBar() {
        binding.run {
            bottomAppBar.performHide()
            // Get a handle on the animator that hides the bottom app bar so we can wait to hide
            // the fab and bottom app bar until after it's exit animation finishes.
            bottomAppBar.animate().setListener(object : AnimatorListenerAdapter() {
                var isCanceled = false
                override fun onAnimationEnd(animation: Animator?) {
                    if (isCanceled) return

                    bottomAppBar.visibility = View.GONE
                    fabAddStory.visibility = View.INVISIBLE
                }

                override fun onAnimationCancel(animation: Animator?) {
                    isCanceled = true
                }
            })
        }
    }

    private fun showBottomBar() {
        binding.bottomAppBar.apply {
            isVisible = true
            performShow()
        }
    }

    private fun showFab(isShown: Boolean) {
        binding.fabAddStory.apply {
            if (isShown) {
                show()
            } else {
                hide()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}