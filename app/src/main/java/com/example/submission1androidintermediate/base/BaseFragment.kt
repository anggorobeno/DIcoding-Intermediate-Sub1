package com.example.submission1androidintermediate.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.example.core.data.local.PreferencesDataStore
import javax.inject.Inject

abstract class BaseFragment<VB : ViewBinding> : Fragment() {
    @Inject
    lateinit var preferencesDataStore: PreferencesDataStore
    private lateinit var safeContext: Context

    protected lateinit var binding: VB
    protected abstract val setLayout: (LayoutInflater) -> VB

    override fun onAttach(context: Context) {
        super.onAttach(context)
        safeContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = setLayout(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        observeViewModel()
    }



    abstract fun observeViewModel()

    abstract fun init()

}