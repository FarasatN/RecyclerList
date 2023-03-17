package com.farasatnovruzov.recyclerlist

import android.graphics.*
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.MenuItemCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import java.util.*


class MainActivity : AppCompatActivity(), ItemsRecyclerAdapter.Interaction {

    lateinit var viewModel: MainViewModel
    lateinit var itemsRecyclerAdapter: ItemsRecyclerAdapter
    lateinit var recyclerview : RecyclerView
    var dataList: MutableList<ItemModel>
        = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar  = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.title = "Notifications"
        toolbar.setTitleTextColor(Color.WHITE)
        toolbar.setNavigationOnClickListener(View.OnClickListener { finish() })

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        recyclerview = findViewById<RecyclerView>(R.id.recyclerView)

//        initViewModel()
        dataList.add(ItemModel(1L,1.toLong(),"Farasat card","19-24 Mart Mohtesem Kampaniya","xyz",
            Date().toString(),1,"WithUrl",0,false,2,1))
        for (i in 1..1000) {
            dataList.add(ItemModel(123L+i,i.toLong(),"Novruz Kampaniyasi","19-24 Mart Mohtesem Kampaniya","xyz",
                Date().toString(),1,"WithUrl",0,false,2,1))
        }
        for (i in 1..1000) {
            dataList.add(ItemModel(123L+i,i.toLong(),"Ramazan Kampaniyasi","19-24 Mart Mohtesem Kampaniya","xyz",
                Date().toString(),1,"WithUrl",0,false,2,1))
        }
        dataList.add(ItemModel(123L,1.toLong(),"Fərasət üçün","19-24 Mart Mohtesem Kampaniya","xyz",
            Date().toString(),1,"WithUrl",0,false,2,1))

        initRecyclerView(dataList)


//        enableSwipeToDeleteAndUndo()
        enableSwipeToDelete()

    }

    private fun enableSwipeToDelete() {
        val swipe = object : SwipeHelper(this@MainActivity, recyclerview, 200) {
            override fun instantiateButton(
                viewHolder: RecyclerView.ViewHolder,
                buffer: MutableList<ItemButton>
            ) {
                buffer.add(
                    ItemButton(this@MainActivity,
                        "Delete",
                        14,
                        R.drawable.ic_delete,
//                        0,
                        Color.parseColor("#ff3c30"),
                        object : ButtonClickListener {
                            override fun onClick(pos: Int) {
                                Toast.makeText(
                                    this@MainActivity,
                                    "delete id" + pos,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    ))
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipe)
        itemTouchHelper.attachToRecyclerView(recyclerview)

    }

    private fun enableSwipeToDeleteAndUndo() {
        val callback: SwipeToDeleteCallback = object :
            SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.getAdapterPosition()
                val item = itemsRecyclerAdapter.getData().get(position)
                itemsRecyclerAdapter.removeItem(position)
                val snackbar = Snackbar
                    .make(
                        recyclerview,
//                        "Item was removed from the list.",
                        item.title,
                        Snackbar.LENGTH_LONG
                    )
                snackbar.setAction("UNDO") {
                    itemsRecyclerAdapter.restoreItem(item, position)
                    recyclerview.scrollToPosition(position)
                }
                snackbar.setActionTextColor(Color.GREEN);
                snackbar.show();
            }
        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(recyclerview)
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.list_menu, menu)

        val searchItem: MenuItem = menu!!.findItem(R.id.action_search)
        val checkItem: MenuItem = menu!!.findItem(R.id.action_check)
        val deleteItem: MenuItem = menu!!.findItem(R.id.action_delete)

        val searchView: SearchView = searchItem.getActionView() as SearchView
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE)

        searchView.setOnClickListener {
            checkItem.isVisible = false
            deleteItem.isVisible = false
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
//                initViewModel()
                itemsRecyclerAdapter.getFilter().filter(query)
                Log.v("TAGGGG", "submit query:${query}")
//                recyclerview.layoutManager?.scrollToPosition(0)

                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
//                initViewModel()
                itemsRecyclerAdapter.getFilter().filter(newText)
                Log.v("TAGGGG", "change query:${newText}")
//                recyclerview.layoutManager?.scrollToPosition(0)

                return false
            }
        })


        checkItem.isVisible = true
        deleteItem.isVisible = true
        val currentapiVersion = Build.VERSION.SDK_INT
        if (currentapiVersion >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            searchItem.setOnActionExpandListener(object : MenuItemCompat.OnActionExpandListener,
                MenuItem.OnActionExpandListener {
                override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                    // Do something when collapsed
                    Log.i("TAG", "onMenuItemActionCollapse " + item.itemId)
//                    recyclerview.smoothScrollToPosition(0)
                    recyclerview.scrollToPosition(0)
                    return true // Return true to collapse action view
                }

                override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                    // TODO Auto-generated method stub
                    Log.i("TAG"
                        , "onMenuItemActionExpand " + item.itemId)
                    return true
                }
            })
        } else {
            // do something for phones running an SDK before froyo
            searchView.setOnCloseListener(object : SearchView.OnCloseListener {
                override fun onClose(): Boolean {
                    Log.i("TAG", "mSearchView on close ")
                    // TODO Auto-generated method stub
//                    recyclerview.smoothScrollToPosition(0)
                    recyclerview.scrollToPosition(0)
                    return false
                }
            })
        }

        return true
    }
//    fun scrollToPosition(position: Int) {
//        recyclerview.scrollToPosition(position)
//    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_search) {
           return true
        }else if (id == R.id.action_delete){
            return true
        }else if (id == R.id.action_check){
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private fun initRecyclerView(list: MutableList<ItemModel>){
        recyclerview.setHasFixedSize(true)
        recyclerview.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
//            object:
//            {
//                override fun canScrollVertically(): Boolean {
//                    return false
//                }
//                override fun canScrollHorizontally(): Boolean {
//                    return false
//                }
//
//                override fun setSmoothScrollbarEnabled(enabled: Boolean) {
//                    super.setSmoothScrollbarEnabled(false)
//                }
//            }
            (layoutManager as LinearLayoutManager).setSmoothScrollbarEnabled(true)
            recyclerview.setLayoutManager(layoutManager)
            itemsRecyclerAdapter = ItemsRecyclerAdapter(this@MainActivity,this@MainActivity, list)
            adapter = itemsRecyclerAdapter
        }
    }






    fun selectAllVisible(value: Boolean) {
        val selectAll = findViewById<ConstraintLayout>(R.id.selectAll)
        if (value == true) {
            selectAll.visibility = View.VISIBLE
        } else {
//            authOpCheckBox.isChecked = false
            selectAll.visibility = View.GONE
        }
    }



    override fun onItemSelected(position: Int, item: ItemModel) {
        println("DEBUG: CLICKED position : $position")
        println("DEBUG: CLICKED item: $item")
    }




//    private fun itemTouch() {
//        var deletedItem: ItemModel? = null
//        val archivedItems: MutableList<ItemModel?> = ArrayList()
//
//        val callback: ItemTouchHelper.SimpleCallback = object :
//            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
//
//            var mContext: Context? = null
//            private var mClearPaint: Paint? = null
//            private var mBackground: ColorDrawable? = null
//            private var backgroundColor = 0
//            private var deleteDrawable: Drawable? = null
//            private var intrinsicWidth = 0
//            private var intrinsicHeight = 0
//
//
//            fun SwipeToDeleteCallback(context: Context) {
//                mContext = context
//                mBackground = ColorDrawable()
//                backgroundColor = Color.parseColor("#b80f0a")
//                mClearPaint = Paint()
//                mClearPaint?.setXfermode(PorterDuffXfermode(PorterDuff.Mode.CLEAR))
//                deleteDrawable = ContextCompat.getDrawable(mContext!!, R.drawable.ic_delete)
//                intrinsicWidth = deleteDrawable!!.intrinsicWidth
//                intrinsicHeight = deleteDrawable!!.intrinsicHeight
//            }
//
//            override fun onChildDraw(
//                c: Canvas,
//                recyclerView: RecyclerView,
//                viewHolder: RecyclerView.ViewHolder,
//                dX: Float,
//                dY: Float,
//                actionState: Int,
//                isCurrentlyActive: Boolean
//            ) {
//                val itemView = viewHolder.itemView
//                val itemHeight = itemView.height
//
//                val isCancelled: Boolean = dX.equals(0) && !isCurrentlyActive
//
//                if (isCancelled) {
//                    clearCanvas(
//                        c,
//                        itemView.right + dX,
//                        itemView.top.toFloat(),
//                        itemView.right.toFloat(),
//                        itemView.bottom.toFloat(),
//
//                        )
//                    super.onChildDraw(
//                        c,
//                        recyclerView,
//                        viewHolder,
//                        dX,
//                        dY,
//                        actionState,
//                        isCurrentlyActive
//                    )
//                    return
//                }
//
//                mBackground?.setColor(backgroundColor)
//                mBackground?.setBounds(
//                    itemView.right + dX.toInt(),
//                    itemView.top,
//                    itemView.right,
//                    itemView.bottom
//                )
//                mBackground?.draw(c)
//
//                val deleteIconTop: Int = itemView.top + (itemHeight - intrinsicHeight) / 2
//                val deleteIconMargin: Int = (itemHeight - intrinsicHeight) / 2
//                val deleteIconLeft: Int = itemView.right - deleteIconMargin - intrinsicWidth
//                val deleteIconRight = itemView.right - deleteIconMargin
//                val deleteIconBottom: Int = deleteIconTop + intrinsicHeight
//
//
//                deleteDrawable?.setBounds(
//                    deleteIconLeft,
//                    deleteIconTop,
//                    deleteIconRight,
//                    deleteIconBottom
//                )
//                deleteDrawable?.draw(c)
//
//
//                super.onChildDraw(
//                    c,
//                    recyclerView,
//                    viewHolder,
//                    dX,
//                    dY,
//                    actionState,
//                    isCurrentlyActive
//                )
//            }
//
//            override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
//                return 0.7f
//            }
//
//            private fun clearCanvas(
//                c: Canvas,
//                left: Float,
//                top: Float,
//                right: Float,
//                bottom: Float
//            ) {
//                c.drawRect(left, top, right, bottom, mClearPaint!!)
//            }
//
//            override fun onMove(
//                recyclerView: RecyclerView,
//                viewHolder: RecyclerView.ViewHolder,
//                target: RecyclerView.ViewHolder
//            ): Boolean {
//                return false
//            }
//
//            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//                val position = viewHolder.getAdapterPosition()
//                when (direction) {
//                    ItemTouchHelper.LEFT -> {
//                        deletedItem = dataList.get(position)
//                        //                        viewModel.liveDataList.value!!.removeAt(position)
//                        //                        viewModel.liveDataList.postValue()
//                        //                        removeItem(position)
//                        //                        viewModel.dataList.removeAt(position)
//                        //                        viewModel.dataList.clear()
//                        //                        viewModel.liveDataList.observe(this@MainActivity, androidx.lifecycle.Observer{
//                        //                            it.removeAt(position)
//                        //                        })
//
//                        //                        viewModel.removeItem(position)
//
//                        dataList.removeAt(position)
//                        itemsRecyclerAdapter.notifyItemRemoved(position)
//
//                        //                        Snackbar.make(recyclerview, deletedItem!!.title, Snackbar.LENGTH_LONG)
//                        //                            .setAction("Undo", View.OnClickListener {
//                        ////                                viewModel.addItem(position, deletedItem!!)
//                        //                                viewModel.liveDataList.value!!.add(position,deletedItem!!)
//                        //
//                        //                                itemsRecyclerAdapter.notifyItemInserted(position)
//                        //                            })
//                        //                            .show()
//                    }
//
//                    //                    ItemTouchHelper.RIGHT -> {
//                    //                        val item = getItem(position)
//                    //                        archivedItems.add(item)
//                    //                        removeItem(position)
//                    //                        itemsRecyclerAdapter.notifyItemRemoved(position)
//                    //                        Snackbar.make(recyclerview, item!!.title+", archived.", Snackbar.LENGTH_LONG)
//                    //                            .setAction("Undo", View.OnClickListener {
//                    //                                viewModel.addItem(position,item)
//                    ////                                archivedItems.removeAt(archivedItems.lastIndexOf(item))
//                    ////                                archivedItems.add(position,item)
//                    //                                itemsRecyclerAdapter.notifyItemInserted(position)
//                    //                            }).show()
//                    //                    }
//                }
//            }
//        }
//
//        val itemTouchHelper = ItemTouchHelper(callback)
//        itemTouchHelper.attachToRecyclerView(recyclerview)
//    }



//    private fun initViewModel(){
//        viewModel.liveDataList.observe(this, androidx.lifecycle.Observer {
//            if (it != null){
//                initRecyclerView(it)
//                itemsRecyclerAdapter.notifyDataSetChanged()
//            }else{
//                Toast.makeText(this,"Error in getting list", Toast.LENGTH_SHORT).show()
//            }
//        })
//    }



//    private fun removeItem(position: Int){
//        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
//        viewModel.getLiveDataObserver().observe(this, androidx.lifecycle.Observer {
//            if (it != null){
//                initRecyclerView(it)
////                itemsRecyclerAdapter.submitList(it)
////                itemsRecyclerAdapter.notifyDataSetChanged()
//            }else{
//                Toast.makeText(this,"Error in getting list", Toast.LENGTH_SHORT).show()
//            }
//        })
////        viewModel.dataList.removeItem(position)
//        return  viewModel.removeItem(position)
//    }
//
//    private fun getItem(position: Int): ItemModel {
//        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
//        viewModel.getLiveDataObserver().observe(this, androidx.lifecycle.Observer {
//            if (it != null){
//                initRecyclerView(it)
////                itemsRecyclerAdapter.submitList(it)
////                itemsRecyclerAdapter.notifyDataSetChanged()
//            }else{
//                Toast.makeText(this,"Error in getting list", Toast.LENGTH_SHORT).show()
//            }
//        })
////        viewModel.dataList.get(position)
//
//        return  viewModel.getItem(position)
//
//    }
//
//    private fun addItem(position: Int,item: ItemModel) {
//        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
//        viewModel.getLiveDataObserver().observe(this, androidx.lifecycle.Observer {
//            if (it != null){
//                initRecyclerView(it)
////                itemsRecyclerAdapter.submitList(it)
////                itemsRecyclerAdapter.notifyDataSetChanged()
//            }else{
//                Toast.makeText(this,"Error in getting list", Toast.LENGTH_SHORT).show()
//            }
//        })
////        viewModel.dataList.add(position,item)
//        viewModel.addItem(position,item)
//    }


//    fun isHuaweiDevice():Boolean{
//        val manufacturer = Build.MANUFACTURER
//        val brand = Build.BRAND
//        Log.v("TAGGGG", "isHuaweiDevice:Build.MANUFACTURER:${Build.MANUFACTURER},Build.BRAND:${Build.BRAND} ")
//        return manufacturer.toUpperCase(Locale.getDefault()).contains("HUAWEI") || brand.toUpperCase(Locale.getDefault()).contains("HUAWEI")
//    }



}

//https://www.youtube.com/watch?v=rcSNkSJ624U
//https://github.com/trulymittal/RecyclerView/blob/swipe_gestures/app/src/main/java/com/example/recyclerview/MainActivity.java
//https://github.com/xabaras/RecyclerViewSwipeDecorator



//https://www.youtube.com/watch?v=uvzP8KTz4Fg
//https://www.youtube.com/watch?v=Hz_EeuZp6Ak