package com.example.submission1androidintermediate.ui.home.stories

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.fragment.navArgs
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
            drawingViewId = R.id.nav_host_fragment
            scrimColor = Color.TRANSPARENT
            setAllContainerColors(ResourcesCompat.getColor(resources,R.color.white,null))
        }
    }

    override fun initView() {
        postponeEnterTransition()
        val extraParcelable = args.storyItem as StoriesModel.StoriesModelItem

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