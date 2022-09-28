package com.example.submission1androidintermediate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.get
import com.example.core.data.networkutils.NetworkResult
import com.example.core.data.remote.request.LoginRequest
import com.example.submission1androidintermediate.databinding.ActivityMainBinding
import com.example.submission1androidintermediate.helper.AppUtils.showToast
import com.example.submission1androidintermediate.helper.FormType
import com.example.submission1androidintermediate.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: UserViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.etFormEmail.setFormType(FormType.Email())
        binding.etFormPassword.setFormType(FormType.Password())
        viewModel.result.observe(this) {
            when (it) {
                is NetworkResult.Error -> it.message?.getContentIfNotHandled()?.let { message ->
                    showToast(message)
                }
                is NetworkResult.Loading -> {}
                is NetworkResult.Success -> {}
            }
        }
        binding.etFormPassword.errorTextListener = { isError ->
            setEnableButton(false)
            if (!isError) setEnableButton(true)
        }
        binding.etFormEmail.errorTextListener = { isError ->
            setEnableButton(false)
            if (!isError) setEnableButton(true)
        }
        binding.btnLogin.setOnClickListener {
            viewModel.loginUser(
                LoginRequest(
                    binding.etFormEmail.getText(),
                    binding.etFormPassword.getText()
                )
            )
        }
    }

    private fun setEnableButton(isEnable: Boolean){
        binding.btnLogin.isEnabled = false
        binding.btnLogin.isEnabled = isEnable
    }
}