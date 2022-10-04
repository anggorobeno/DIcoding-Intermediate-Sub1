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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class WelcomeFragment : BaseFragment<FragmentWelcomeBinding>() {
    override val setLayout: (LayoutInflater) -> FragmentWelcomeBinding
        get() = {
            FragmentWelcomeBinding.inflate(layoutInflater)
        }

    override fun observeViewModel() {


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun init() {
        viewLifecycleOwner.lifecycleScope.launch {
            Timber.d(preferencesDataStore.getLoginStatus().toString())
            preferencesDataStore.getLoginStatus()?.let { isLogin ->
                if (isLogin) {
                    val graph = findNavController().graph
                    val navOptions = NavOptions.Builder()
                        .setPopUpTo(graph.id, true)
                        .build()
                    findNavController().navigate(
                        R.id.action_welcomeFragment_to_homeFragment,
                        null,
                        navOptions
                    )
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

