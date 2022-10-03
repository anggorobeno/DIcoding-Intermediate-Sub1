package com.example.submission1androidintermediate.ui.login

import android.view.LayoutInflater
import androidx.fragment.app.viewModels
import com.example.domain.utils.NetworkResult
import com.example.domain.model.user.login.LoginRequest
import com.example.domain.model.validator.EmailValidator
import com.example.domain.model.validator.PasswordValidator
import com.example.submission1androidintermediate.base.BaseFragment
import com.example.submission1androidintermediate.databinding.FragmentLoginBinding
import com.example.submission1androidintermediate.helper.AppUtils.showToast
import com.example.submission1androidintermediate.helper.FormType
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>() {
    private val viewModel: LoginViewModel by viewModels()


    private fun setEnableButton(isEnable: Boolean) {
        binding.btnLogin.isEnabled = false
        binding.btnLogin.isEnabled = isEnable
    }

    override val setLayout: (LayoutInflater) -> FragmentLoginBinding = { layoutInflater ->
        FragmentLoginBinding.inflate(layoutInflater)
    }

    override fun observeViewModel() {
        viewModel.loginResult.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Error -> it.message?.getContentIfNotHandled()?.let { message ->
                    showToast(message)
                }
                is NetworkResult.Loading -> {}
                is NetworkResult.Success -> {
                    Timber.d(it.data.toString())
                }
            }
        }
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
            viewModel.doLoginUser(
                LoginRequest(
                    binding.etFormEmail.getText(),
                    binding.etFormPassword.getText()
                )
            )
        }
    }
}
