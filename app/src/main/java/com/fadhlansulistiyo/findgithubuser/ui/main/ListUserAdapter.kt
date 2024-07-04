package com.fadhlansulistiyo.findgithubuser.ui.main

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fadhlansulistiyo.findgithubuser.data.remote.response.UserItems
import com.fadhlansulistiyo.findgithubuser.databinding.ItemUserBinding
import com.fadhlansulistiyo.findgithubuser.ui.detailuser.DetailUserActivity
import com.fadhlansulistiyo.findgithubuser.ui.follow.FollowFragment

class ListUserAdapter : ListAdapter<UserItems, ListUserAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): MyViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
    }

    class MyViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: UserItems) {
            binding.tvItemName.text = item.login
            binding.tvItemType.text = item.type
            Glide.with(itemView.context)
                .load(item.avatarUrl)
                .into(binding.ivItemAvatar)

            itemView.setOnClickListener {
                Intent(itemView.context, DetailUserActivity::class.java).apply {
                    putExtra(DetailUserActivity.EXTRA_LOGIN, item.login)
                    putExtra(DetailUserActivity.EXTRA_AVATAR_URL, item.avatarUrl)
                    putExtra(DetailUserActivity.EXTRA_TYPE, item.type)
                    putExtra(DetailUserActivity.EXTRA_URL, item.url)
                    putExtra(FollowFragment.ARG_USERNAME, item.login)
                }.run {
                    itemView.context.startActivity(this)
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<UserItems>() {
            override fun areItemsTheSame(oldItem: UserItems, newItem: UserItems): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: UserItems, newItem: UserItems): Boolean {
                return oldItem == newItem
            }
        }
    }
}