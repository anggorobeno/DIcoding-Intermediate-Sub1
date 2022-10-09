package com.example.submission1androidintermediate.ui.home.stories

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
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


class DetailStoryFragment : BaseFragment<FragmentDetailStoryBinding>() {
    private val args: DetailStoryFragmentArgs by navArgs()
    override val setLayout: (LayoutInflater) -> FragmentDetailStoryBinding
        get() = {
            FragmentDetailStoryBinding.inflate(it)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition =
            TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
    }

    override fun observeViewModel() {

    }

    override fun init() {
        postponeEnterTransition()
        val extraParcelable = args.storyItem as StoriesModel.StoriesModelItem
        binding.ivStoryImage.transitionName = extraParcelable.photoUrl
        binding.tvUsername.transitionName = extraParcelable.id
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