package com.tam.gojual.ui.adapter // Sesuaikan package

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tam.gojual.data.model.Post
import com.tam.gojual.databinding.ItemPostCardBinding

class PostAdapter(private val onItemClick: (Post) -> Unit) :
    ListAdapter<Post, PostAdapter.PostViewHolder>(POST_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemPostCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val currentPost = getItem(position)
        if (currentPost != null) {
            holder.bind(currentPost)
        }
    }

    inner class PostViewHolder(private val binding: ItemPostCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val post = getItem(position)
                    if (post != null) {
                        onItemClick(post)
                    }
                }
            }
        }

        fun bind(post: Post) {
            binding.apply {
                tvPostTitle.text = post.title
                tvPostPrice.text = post.price
                tvPostLocation.text = post.location
                // Nanti kita akan tambahkan loading gambar di sini
            }
        }
    }

    companion object {
        private val POST_COMPARATOR = object : DiffUtil.ItemCallback<Post>() {
            override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean =
                oldItem == newItem
        }
    }
}