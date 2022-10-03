package com.example.submission1androidintermediate.ui.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.fragment.app.viewModels
import com.example.domain.model.user.register.RegisterRequest
import com.example.domain.model.validator.EmailValidator
import com.example.domain.model.validator.PasswordValidator
import com.example.domain.model.validator.UsernameValidator
import com.example.domain.utils.NetworkResult
import com.example.submission1androidintermediate.R
import com.example.submission1androidintermediate.base.BaseFragment
import com.example.submission1androidintermediate.databinding.FragmentRegisterBinding
import com.example.submission1androidintermediate.helper.AppUtils.showToast
import com.example.submission1androidintermediate.helper.FormType
import com.example.submission1androidintermediate.ui.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding>() {
    private val viewModel: RegisterViewModel by viewModels()
    override val setLayout: (LayoutInflater) -> FragmentRegisterBinding = {
        FragmentRegisterBinding.inflate(layoutInflater)
    }

    override fun observeViewModel() {
        viewModel.registerResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResult.Loading -> {

                }
                is NetworkResult.Error -> {
                    result.message?.getContentIfNotHandled()?.let {
                        showToast(it)
                    }
                }
                is NetworkResult.Success -> {
                    Timber.d(result.data.toString())
                    result.message?.getContentIfNotHandled()?.let {
                        showToast(it)
                    }
                    result.data?.let {
                        it.message?.let { message ->
                            showToast(message)
                        }
                    }
                }
            }
        }

    }

    override fun init() {
        binding.etFormEmail.setFormType(FormType.Email(EmailValidator()))
        binding.etFormPassword.setFormType(FormType.Password(PasswordValidator()))
        binding.etFormUsername.setFormType(FormType.Username(UsernameValidator()))
        binding.etFormPassword.errorTextListener = { isError ->
            setEnableButton(false)
            if (!isError) setEnableButton(true)
        }
        binding.etFormEmail.errorTextListener = { isError ->
            setEnableButton(false)
            if (!isError) setEnableButton(true)
        }
        binding.btnRegister.setOnClickListener {
            viewModel.doRegisterUser(
                RegisterRequest(
                    binding.etFormUsername.getText(),
                    binding.etFormEmail.getText(),
                    binding.etFormPassword.getText()
                )
            )
        }
    }

    private fun setEnableButton(isEnable: Boolean) {
        binding.btnRegister.isEnabled = false
        binding.btnRegister.isEnabled = isEnable
    }
}