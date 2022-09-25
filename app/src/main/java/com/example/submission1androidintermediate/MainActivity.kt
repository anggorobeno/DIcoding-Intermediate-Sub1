package com.example.submission1androidintermediate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.submission1androidintermediate.databinding.ActivityMainBinding
import com.example.submission1androidintermediate.helper.FormType

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.textView.setFormType(FormType.Email())
        binding.textView2.setFormType(FormType.Password())
    }
}