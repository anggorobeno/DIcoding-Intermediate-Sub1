package com.example.submission1androidintermediate.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.domain.model.stories.StoriesModel
import com.example.submission1androidintermediate.databinding.ItemStoryBinding

class HomeStoryAdapter : RecyclerView.Adapter<HomeStoryAdapter.StoryViewHolder>() {
    val list = arrayListOf<StoriesModel.StoriesModelItem>()
    var onClickCallback: ((StoriesModel.StoriesModelItem) -> Unit)? = null

    inner class StoryViewHolder(private val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: StoriesModel.StoriesModelItem) {
            binding.root.setOnClickListener {
                onClickCallback?.invoke(item)
            }
            binding.tvStoryName.text = item.name
            Glide.with(binding.ivStoryImage.context)
                .load(item.photoUrl)
                .into(binding.ivStoryImage)
        }
    }

    fun setList(data: StoriesModel) {
        this.list.addAll(data.data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}