package com.example.submission1androidintermediate.ui.login

import android.view.LayoutInflater
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.domain.model.user.login.LoginRequest
import com.example.domain.model.validator.EmailValidator
import com.example.domain.model.validator.PasswordValidator
import com.example.domain.utils.NetworkResult
import com.example.domain.utils.SingleEvent
import com.example.submission1androidintermediate.R
import com.example.submission1androidintermediate.base.BaseFragment
import com.example.submission1androidintermediate.databinding.FragmentLoginBinding
import com.example.submission1androidintermediate.helper.AppUtils.navigateToDestination
import com.example.submission1androidintermediate.helper.AppUtils.showToast
import com.example.submission1androidintermediate.helper.FormType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>() {
    private val viewModel: LoginViewModel by viewModels()
    private var saveTokenJob: Job = Job()
    private var setLoginJob: Job = Job()
    private var loginJob: Job = Job()


    private fun setEnableButton(isEnable: Boolean) {
        binding.btnLogin.isEnabled = false
        binding.btnLogin.isEnabled = isEnable
    }

    override val setLayout: (LayoutInflater) -> FragmentLoginBinding = { layoutInflater ->
        FragmentLoginBinding.inflate(layoutInflater)
    }

    override fun observeViewModel() {
        viewModel.loginResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResult.Error -> result.message?.getContentIfNotHandled()?.let { message ->
                    showLoadingState(false)
                    showToast(message)
                }
                is NetworkResult.Loading -> {
                    showLoadingState(true)
                }
                is NetworkResult.Success -> {
                    showLoadingState(false)
                    result.data?.let {
                        it.data?.token?.let { token ->
                            saveTokenJob = viewModel.saveUserToken(token)
                        }
                    }
                    setLoginJob = viewModel.setLoginStatus(true)
                    viewLifecycleOwner.lifecycleScope.launch {
                        SingleEvent(result.data?.message).getContentIfNotHandled()
                            ?.let { showToast(it) }
                        saveTokenJob.join()
                        setLoginJob.join()
                        navigateToDestination(R.id.action_loginFragment_to_homeFragment)
                    }
                }
            }
        }
    }

    private fun showLoadingState(isLoading: Boolean) {
        binding.layoutProgressBar.progressCircular.isVisible = isLoading
    }

    override fun init() {
        binding.etFormEmail.setFormType(FormType.Email(EmailValidator()))
        binding.etFormPassword.setFormType(FormType.Password(PasswordValidator()))
        binding.etFormPassword.errorTextListener = { isError ->
            setEnableButton(false)
            if (!isError) setEnableButton(true)
        }
        binding.etFormEmail.errorTextListener = { isError ->
            setEnableButton(false)
            if (!isError) setEnableButton(true)
        }
        binding.btnLogin.setOnClickListener {
            if (loginJob.isActive) loginJob.cancel()
            loginJob = viewModel.doLoginUser(
                LoginRequest(
                    binding.etFormEmail.getText(),
                    binding.etFormPassword.getText()
                )
            )

        }
        binding.tvRegisterNow.setOnClickListener {
            navigateToDestination(R.id.action_loginFragment_to_registerFragment)
        }
    }
}
