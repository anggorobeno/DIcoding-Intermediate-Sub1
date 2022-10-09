package com.example.submission1androidintermediate.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.domain.model.stories.StoriesModel
import com.example.submission1androidintermediate.databinding.ItemStoryBinding
import com.example.submission1androidintermediate.helper.AppUtils
import com.example.submission1androidintermediate.helper.AppUtils.navigateToDestination
import com.example.submission1androidintermediate.ui.home.HomeFragmentDirections

class HomeStoryAdapter : RecyclerView.Adapter<HomeStoryAdapter.StoryViewHolder>() {
    var onClickCallback: ((StoriesModel.StoriesModelItem, View, View, View) -> Unit)? = null
    private val differ: AsyncListDiffer<StoriesModel.StoriesModelItem> =
        AsyncListDiffer(this, DiffCallback())

    inner class StoryViewHolder(private val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: StoriesModel.StoriesModelItem) {
            binding.root.setOnClickListener {
                onClickCallback?.invoke(
                    item,
                    binding.ivStoryImage,
                    binding.tvStoryName,
                    binding.ivUserProfile
                )
            }
            binding.tvStoryName.text = item.name
            Glide.with(binding.ivStoryImage.context)
                .load(item.photoUrl)
                .apply(AppUtils.getGlideRequestOption(binding.ivStoryImage.context))
                .into(binding.ivStoryImage)
        }
    }

    fun setList(data: StoriesModel) {
        differ.submitList(data.data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private class DiffCallback : DiffUtil.ItemCallback<StoriesModel.StoriesModelItem>() {

        override fun areItemsTheSame(
            oldItem: StoriesModel.StoriesModelItem,
            newItem: StoriesModel.StoriesModelItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: StoriesModel.StoriesModelItem,
            newItem: StoriesModel.StoriesModelItem
        ): Boolean {
            return oldItem == newItem
        }
    }

}