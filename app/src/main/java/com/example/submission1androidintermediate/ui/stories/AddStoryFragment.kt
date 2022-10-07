package com.example.submission1androidintermediate.ui.stories

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.submission1androidintermediate.R
import com.example.submission1androidintermediate.base.BaseFragment
import com.example.submission1androidintermediate.databinding.FragmentAddStoryBinding


class AddStoryFragment : BaseFragment<FragmentAddStoryBinding>() {
    override val setLayout: (LayoutInflater) -> FragmentAddStoryBinding
        get() = {
            FragmentAddStoryBinding.inflate(layoutInflater)
        }

    override fun observeViewModel() {

    }

    override fun init() {

    }
}
