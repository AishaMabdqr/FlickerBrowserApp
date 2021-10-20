package com.example.flickrbrowserapp

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.card_view.view.*
import kotlinx.android.synthetic.main.item_row.view.*
import kotlinx.android.synthetic.main.item_row.view.imageView
import kotlinx.android.synthetic.main.item_row.view.tvTitle

class RVAdapter (val activity : MainActivity, val itemList : ArrayList<PhotoList>) : RecyclerView.Adapter<RVAdapter.ItemViewHolder>() {
    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_row,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val items = itemList[position]

        holder.itemView.apply {
                tvTitle.text = items.title
                Glide.with(this).load(items.link).into(imageView)
                rvItems.setOnClickListener{
                    activity.showImage(items.link)
                }


        }
    }

    override fun getItemCount() = itemList.size
}
