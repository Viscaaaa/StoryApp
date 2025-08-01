package com.visca.storyaplication.View.Ui.List

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.visca.storyaplication.Data.Response.ListStoryItem
import com.visca.storyaplication.Utils.dateFormat
import com.visca.storyaplication.databinding.ListStoryBinding

class StoryAdapter: PagingDataAdapter<ListStoryItem, StoryAdapter.MyViewHolder>(DIFF_CALLBACK){

    private lateinit var onItemClickCallBack: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(data: ListStoryItem)
    }

    fun setOnItemClickCallback(onItemClickCallback: Any) {
        this.onItemClickCallBack = onItemClickCallback as OnItemClickCallback
    }

    class MyViewHolder(private val binding: ListStoryBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ListStoryItem) {
            Glide.with(itemView.context)
                .load(item.photoUrl)
                .into(binding.ivStory)
            binding.tvNameStory.text = item.name
            binding.tvDescStory.text = item.description
            binding.tvCratedatStory.text = item.createdAt.dateFormat()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup , viewType: Int): MyViewHolder {
        val binding = ListStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder , position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }

        holder.itemView.setOnClickListener {
            getItem(holder.adapterPosition)?.let { it1 -> onItemClickCallBack.onItemClicked(it1) }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}