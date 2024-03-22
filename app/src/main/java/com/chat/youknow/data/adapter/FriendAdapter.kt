package com.chat.youknow.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chat.youknow.data.model.Friend
import com.chat.youknow.databinding.RvFriendAdapterBinding

class FriendAdapter(
    private val friendList: ArrayList<Friend>,
) : RecyclerView.Adapter<FriendAdapter.DataViewHolder>() {

    private var onItemClickListener: ((Friend) -> Unit)? = null

    inner class DataViewHolder(val binding: RvFriendAdapterBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(friend: Friend) {
            binding.tvFriendName.text = friend.name
//            Glide.with(binding.ivFlag.context)
//                .load(country.url)
//                .into(binding.ivFlag)


//            binding.ivSave.apply {
//                setOnClickListener {
//                    clickListener.onSaveItemClick(country)
//                }
//            }

            itemView.setOnClickListener {
                onItemClickListener?.let {
                    it(friend)
                }
            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataViewHolder(
            RvFriendAdapterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun getItemCount(): Int = friendList.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val friend = friendList[position]
        holder.bind(friend)
    }

    fun addData(list: List<Friend>) {
        friendList.addAll(list)
    }

    fun setOnItemClickListener(listener: (Friend) -> Unit) {
        onItemClickListener = listener
    }

}