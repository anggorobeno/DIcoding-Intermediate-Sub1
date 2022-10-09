package com.example.submission1androidintermediate.ui.welcome

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.core.data.local.PreferencesDataStore
import com.example.submission1androidintermediate.R
import com.example.submission1androidintermediate.base.BaseFragment
import com.example.submission1androidintermediate.databinding.FragmentWelcomeBinding
import com.example.submission1androidintermediate.helper.AppUtils.getNavGraph
import com.example.submission1androidintermediate.helper.AppUtils.navigateToDestination
import com.github.ajalt.timberkt.d
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class WelcomeFragment : BaseFragment<FragmentWelcomeBinding>() {
    override val setLayout: (LayoutInflater) -> FragmentWelcomeBinding
        get() = {
            FragmentWelcomeBinding.inflate(layoutInflater)
        }

    override fun observeViewModel() {
    // Do nothing
    }

    override fun init() {
        runBlocking {
            Timber.d(preferencesDataStore.getLoginStatus().toString())
            preferencesDataStore.getLoginStatus()?.let { isLogin ->
                if (isLogin) {
                    navigateToDestination(R.id.action_welcomeFragment_to_homeFragment)
                }
            }
        }
        binding.btnLogin.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeFragment_to_loginFragment)
        }
        binding.btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeFragment_to_registerFragment)
        }
    }
}

