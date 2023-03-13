package com.farasatnovruzov.recyclerlist

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil

class ItemsRecyclerAdapter(
    private val interaction: Interaction? = null,
    private val dataList: List<ItemModel>
                           ) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ItemModel>() {

        override fun areItemsTheSame(oldItem: ItemModel, newItem: ItemModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ItemModel, newItem: ItemModel): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return ItemModelViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item,
                parent,
                false
            ),
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ItemModelViewHolder -> {
                holder.bind(differ.currentList.get(position))
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<ItemModel>) {
        differ.submitList(list)
    }

    class ItemModelViewHolder
    constructor(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: ItemModel) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }

            itemView.findViewById<TextView>(R.id.notificationTitle).text = item.title
            itemView.findViewById<TextView>(R.id.notificationBody).text = item.content
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: ItemModel)
    }
}
