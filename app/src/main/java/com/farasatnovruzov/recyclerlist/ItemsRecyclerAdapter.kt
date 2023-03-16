package com.farasatnovruzov.recyclerlist

import android.R.attr.data
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView


private var isMultiSelectModeActive: Boolean = false

class ItemsRecyclerAdapter(
    private val context: Context,
    private val interaction: Interaction? = null,
    private val dataList: MutableList<ItemModel>,
//    private val searchedListFull: MutableList<ItemModel>
//    = ArrayList(dataList),
 ) :   RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {

    private val searchedListFull: MutableList<ItemModel> = mutableListOf()

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
//        return dataList.size
    }

    init {
        differ.submitList(dataList)
        searchedListFull.addAll(differ.currentList)
    }

//    fun submitList(list: MutableList<ItemModel>) {
//        differ.submitList(list)
//    }

    class ItemModelViewHolder(
        itemView: View,
        private val interaction: Interaction?
            ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: ItemModel) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
                deactivateMultiSelection(context)
            }
            itemView.setOnLongClickListener {
                interaction?.onItemSelected(adapterPosition, item)
                isMultiSelectModeActive = true
                activateMultiSelection(context)
                true
            }

            itemView.findViewById<TextView>(R.id.notificationTitle).text = item.title
            itemView.findViewById<TextView>(R.id.notificationBody).text = item.content
        }
    }


    interface Interaction {
        fun onItemSelected(position: Int, item: ItemModel)
    }

    override fun getFilter(): Filter {
        return exampleFilter
    }

    private val exampleFilter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val filteredList: MutableList<ItemModel> = ArrayList<ItemModel>().toMutableList()
            if (constraint == null || constraint.length == 0) {
                filteredList.addAll(searchedListFull)
            }
            else {
                val filterPattern = constraint.toString().toLowerCase().trim { it <= ' ' }
                for (item in searchedListFull) {
                    if (item.title.toLowerCase().contains(filterPattern)) {
                        Log.v("TAGGGG", "filterPattern:${filterPattern}")
                        filteredList.add(item)
                    }
                    else{
//                        filteredList.clear()
//                        differ.submitList(dataList)
//                        val activity: MainActivity = context as MainActivity
//                        activity.scrollToPosition(0)

                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults?) {
//            differ.currentList.toMutableList().clear()
//            differ.currentList.toMutableList().addAll(results!!.values as MutableList<ItemModel>)
            differ.submitList(results!!.values as MutableList<ItemModel>)


//            notifyDataSetChanged()
//            Log.v("TAGGGG", "results.values:${(results?.values as MutableList<ItemModel>).size}")


            Log.v("TAGGGG", "searchedListFull:${searchedListFull.size}")
            Log.v("TAGGGG", "differList:${differ.currentList.size}")

//            dataList.clear()
//            dataList.addAll(results!!.values as MutableList<ItemModel>)
//            differ.submitList(dataList)
        }
    }




    fun removeItem(position: Int) {
        dataList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun restoreItem(item: ItemModel, position: Int) {
        dataList.add(position, item)
        notifyItemInserted(position)
    }

    fun getData(): MutableList<ItemModel> {
        return dataList
    }
}

private fun activateMultiSelection(context: Context) {
    isMultiSelectModeActive = true
    val activity = context as MainActivity
    activity.selectAllVisible(isMultiSelectModeActive)
}

private fun deactivateMultiSelection(context: Context) {
    isMultiSelectModeActive = false
    val activity = context as MainActivity
    activity.selectAllVisible(isMultiSelectModeActive)
}