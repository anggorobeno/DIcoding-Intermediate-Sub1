package com.example.submission1androidintermediate.ui.home.stories

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.doOnPreDraw
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.domain.model.stories.StoriesModel
import com.example.submission1androidintermediate.R
import com.example.submission1androidintermediate.base.BaseFragment
import com.example.submission1androidintermediate.databinding.FragmentDetailStoryBinding
import com.example.submission1androidintermediate.helper.AppUtils
import com.example.submission1androidintermediate.helper.AppUtils.getGlideRequestOption
import com.example.submission1androidintermediate.helper.AppUtils.showToast
import com.google.android.material.transition.MaterialContainerTransform


class DetailStoryFragment : BaseFragment<FragmentDetailStoryBinding>() {
    private val args: DetailStoryFragmentArgs by navArgs()
    override val setLayout: (LayoutInflater) -> FragmentDetailStoryBinding
        get() = {
            FragmentDetailStoryBinding.inflate(it)
        }

    override fun observeViewModel() {
        // Do nothing
    }

    override fun init() {
        postponeEnterTransition()
        val extraParcelable = args.storyItem as StoriesModel.StoriesModelItem
        ViewCompat.setTransitionName(binding.ivStoryImage, extraParcelable.id)
        enterTransition = MaterialContainerTransform().apply {
            startView = requireActivity().findViewById(R.id.iv_story_image)
            endView = binding.ivStoryImage
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
            scrimColor = Color.TRANSPARENT
        }
        Glide.with(requireContext())
            .load(extraParcelable.photoUrl)
            .apply(getGlideRequestOption(requireContext()))
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    startPostponedEnterTransition()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    startPostponedEnterTransition()
                    return false
                }
            })
            .into(binding.ivStoryImage)
        binding.tvUsername.text = extraParcelable.name
        binding.tvDesc.text = extraParcelable.description
        binding.tvCreatedAt.text = AppUtils.getDate(extraParcelable.createdAt)

    }
}