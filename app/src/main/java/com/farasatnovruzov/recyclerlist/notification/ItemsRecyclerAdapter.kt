package com.farasatnovruzov.recyclerlist.notification

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.asynclayoutinflater.view.AsyncLayoutInflater
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.farasatnovruzov.recyclerlist.R


private var isMultiSelectModeActive: Boolean = false

class ItemsRecyclerAdapter(
    private val context: Context,
    private val interaction: Interaction? = null,
    private val dataList: MutableList<ItemModel>,
//    private val searchedListFull: MutableList<ItemModel>
//    = ArrayList(dataList),
 ) :   RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {
//    val inflater = AsyncLayoutInflater(this)
//    inflater.inflate(R.layout.main, null, callback)
    //declare AsyncLayoutInflater
    private lateinit var asyncLayoutInflater: AsyncLayoutInflater
//    init asyncLayoutInflater
//    fun initAsyncInflater() {
//        context?.let{asyncLayoutInflater=AsyncLayoutInflater(it)}
//    }
//    //usage
//    asyncLayoutInflater.inflate(R.layout.custom_marker_layout, null){
//        customMarkerView, _, _ ->
//    }

//    val content = findViewById(android.R.id.content)
//    AsyncLayoutInflater(this)
//    .inflate(R.layout.activity_main, content) { view, _, _ ->
//        binding = ActivityMainBinding.bind(view)
//        setContentView(view)
//    }

    private val searchedListFull: MutableList<ItemModel> = mutableListOf()

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ItemModel>() {
        override fun areItemsTheSame(oldItem: ItemModel, newItem: ItemModel): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: ItemModel, newItem: ItemModel): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, DIFF_CALLBACK)


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
                holder.bind(
                    differ.currentList.get(position)
//                            dataList.get(position)
                ,differ.currentList
                )
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
//        return dataList.size
    }

    init {
        differ.submitList(dataList)
        searchedListFull.addAll(
            differ.currentList
//            dataList
        )
    }

    fun submitList(list: MutableList<ItemModel>) {
        differ.submitList(list)
    }

    class ItemModelViewHolder(
        itemView: View,
        private val interaction: Interaction?
            ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: ItemModel, list: MutableList<ItemModel>) = with(itemView) {
            val checkMark = itemView.findViewById<ImageView>(R.id.notificationCheckImage)

            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
//                deactivateMultiSelection()

                val activity = context as MainActivity
//                val activityDetail = DetailActivity(activity.itemsRecyclerAdapter,activity)

                val intent = Intent(activity, DetailActivity::class.java)
                intent.putExtra("title",item.title)
                intent.putExtra("content",item.content)
                intent.putExtra("position",adapterPosition)
                activity.startActivity(intent)
            }
            itemView.setOnLongClickListener {
                interaction?.onItemSelected(adapterPosition, item)

                itemView.setBackgroundColor(Color.parseColor("#E4E4E4"))
                checkMark.visibility = View.VISIBLE
                val activity = context as MainActivity
                isMultiSelectModeActive = true
                activity.selectAllVisible(isMultiSelectModeActive)

//                activateMultiSelection(context)

//                if (!isMultiSelectModeActive) {
////                    authOperation.isSelected = true
////                    notifyDataSetChanged()
//                    val activity = context as MainActivity
//                    activity.selectAllVisible(isMultiSelectModeActive)
//                } else {
////                    deactivateMultiSelection()
////                    deselectAll()
//                }
                true
            }

            itemView.findViewById<TextView>(R.id.notificationTitle).text = item.title
            itemView.findViewById<TextView>(R.id.notificationBody).text = item.content
        }

    }



//    fun setAllSelected(active: Boolean) {
//        if (active) {
//            for (op in differ.currentList) {
//                op.isSelected = true
//            }
//            for (op in differ.currentList) {
//                op.isSelected = true
//            }
////            activateMultiSelection()
//            notifyDataSetChanged()
//        } else {
//            for (op in differ.currentList) {
//                op.isSelected = false
//            }
//            for (op in differ.currentList) {
//                op.isSelected = false
//            }
////            activateMultiSelection()
//            notifyDataSetChanged()
//        }
////        val activity = context as NotificationActivity
////        activity.selectAllVisible(isMultiSelectModeActive)
//    }

//    fun deselectAll() {
//        //  notificationFilterList.clear()
//        for (op in differ.currentList) {
//            op.isSelected = false
//        }
////        clickListener.invoke(0)
//        isMultiSelectModeActive = false
//        notifyDataSetChanged()
////        val activity = context as NotificationActivity
////        activity.selectAllVisible(isMultiSelectModeActive)
//    }

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
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults?) {
            differ.submitList(results!!.values as MutableList<ItemModel>)
            //dataList.addAll(results!!.values as MutableList<ItemModel>)
//            notifyDataSetChanged()

            Log.v("TAGGGG", "searchedListFull:${searchedListFull.size}")
            Log.v("TAGGGG", "differList:${differ.currentList.size}")
//            Log.v("TAGGGG", "differList:${dataList.size}")
        }
    }


    private fun activateMultiSelection(context: Context) {
        isMultiSelectModeActive = true
        val activity = context as MainActivity
        activity.selectAllVisible(isMultiSelectModeActive)
    }

    private fun deactivateMultiSelection(context: Context) {
        isMultiSelectModeActive = true
        val activity = context as MainActivity
        activity.selectAllVisible(isMultiSelectModeActive)
    }





    fun removeItem(position: Int) {
        dataList.removeAt(position)
        differ.submitList(dataList)
        notifyItemRemoved(position)
    }

    fun restoreItem(item: ItemModel, position: Int) {
        dataList.add(position, item)
        differ.submitList(dataList)
        notifyItemInserted(position)
    }

    fun getData(): MutableList<ItemModel> {
        differ.submitList(dataList)
        return differ.currentList
    }
}
