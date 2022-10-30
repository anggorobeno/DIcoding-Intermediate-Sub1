package com.example.submission1androidintermediate.ui.welcome

import android.view.LayoutInflater
import androidx.navigation.fragment.findNavController
import com.example.submission1androidintermediate.R
import com.example.submission1androidintermediate.base.BaseFragment
import com.example.submission1androidintermediate.databinding.FragmentWelcomeBinding
import com.example.submission1androidintermediate.helper.AppUtils.navigateToDestination
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import timber.log.Timber


@AndroidEntryPoint
class WelcomeFragment : BaseFragment<FragmentWelcomeBinding>() {
    override val setLayout: (LayoutInflater) -> FragmentWelcomeBinding
        get() = {
            FragmentWelcomeBinding.inflate(it)
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

