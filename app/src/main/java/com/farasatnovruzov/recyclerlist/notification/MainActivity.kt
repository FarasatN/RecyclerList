package com.farasatnovruzov.recyclerlist.notification

import android.content.Context
import android.graphics.Color
import android.graphics.PointF
import android.os.Build
import android.os.Bundle
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.MenuItemCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.*
import com.farasatnovruzov.recyclerlist.R
import java.util.*


class MainActivity : AppCompatActivity(), ItemsRecyclerAdapter.Interaction {

    lateinit var viewModel: MainViewModel
    lateinit var itemsRecyclerAdapter: ItemsRecyclerAdapter
    lateinit var recyclerview : RecyclerView
    var dataList: MutableList<ItemModel>
        = mutableListOf()
    lateinit var selectAll: ConstraintLayout

    init {
        dataList.add(
            ItemModel(1L,1.toLong(),"Farasat card","19-24 Mart Mohtesem Kampaniya","xyz",
            Date().toString(),1,"WithUrl",0,false,2,1)
        )
        for (i in 1..1000) {
            dataList.add(
                ItemModel(2L+i,i.toLong(),"Novruz Kampaniyasi","19-24 Mart Mohtesem Kampaniya","xyz",
                Date().toString(),1,"WithUrl",0,false,2,1)
            )
        }
        for (i in 1..1000) {
            dataList.add(
                ItemModel(333L+i,i.toLong(),"Ramazan Kampaniyasi","19-24 Mart Mohtesem Kampaniya","xyz",
                Date().toString(),1,"WithUrl",0,false,2,1)
            )
        }
        dataList.add(
            ItemModel(444L,1.toLong(),"Fərasət üçün","19-24 Mart Mohtesem Kampaniya","xyz",
            Date().toString(),1,"WithUrl",0,false,2,1)
        )

        itemsRecyclerAdapter = ItemsRecyclerAdapter(this@MainActivity,this@MainActivity, dataList)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar  = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.title = "Notifications"
        toolbar.setTitleTextColor(Color.WHITE)
        toolbar.setNavigationOnClickListener(View.OnClickListener { finish() })
        selectAll = findViewById<ConstraintLayout>(R.id.selectAll)


        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        recyclerview = findViewById<RecyclerView>(R.id.recyclerView)

//        initViewModel()

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
                        36,
                        R.drawable.ic_delete,
//                        0,
                        Color.parseColor("#ff3c30"),
                        object : ButtonClickListener {
                            override fun onClick(pos: Int) {
                                val alertDialogBuilder = AlertDialog.Builder(this@MainActivity,
                                    androidx.appcompat.R.style.Theme_AppCompat_Light_Dialog_Alert)
                                alertDialogBuilder.apply {
                                    setTitle("Delete notification")
                                    setMessage("Are you sure you want to delete the notification?")
                                    setPositiveButton("Yes") { dialog, which ->
                                        itemsRecyclerAdapter.removeItem(pos)
                                        Toast.makeText(applicationContext,"Notification deleted", Toast.LENGTH_SHORT).show()
                                    }
                                    setNegativeButton("No") { dialog, which ->
                                        dialog.dismiss()
                                    }
                                    create().setCancelable(false)
                                }.show()
                            }
                        }
                    ))
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipe)
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

        val currentapiVersion = Build.VERSION.SDK_INT
        if (currentapiVersion >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            searchItem.setOnActionExpandListener(object : MenuItemCompat.OnActionExpandListener,
                MenuItem.OnActionExpandListener {
                override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                    recyclerview.scrollToPosition(0)
                    return true // Return true to collapse action view
                }
                override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                    return true
                }
            })
        } else {
            // do something for phones running an SDK before froyo
            searchView.setOnCloseListener(object : SearchView.OnCloseListener {
                override fun onClose(): Boolean {
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
        when(item.itemId){
            R.id.action_search -> return true
            R.id.action_delete ->  return true
            R.id.action_check -> return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun initRecyclerView(list: MutableList<ItemModel>){
        recyclerview.setHasFixedSize(true)

//        recyclerview.isNestedScrollingEnabled = false
        recyclerview.layoutManager = LinearLayoutManager(this)
//        recyclerview.apply {
//            layoutManager = LinearLayoutManager(this@MainActivity)
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
//            (layoutManager as LinearLayoutManager).setSmoothScrollbarEnabled(true)
//            recyclerview.setLayoutManager(layoutManager)
//            recyclerview.layoutManager = SpeedyLinearLayoutManager(context)
//        }

//        val smoothScroller = object : LinearSmoothScroller(this) {
//            private val MILLISECONDS_PER_INCH = 150f // Change this value to modify scroll speed
//
//            override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
//                return MILLISECONDS_PER_INCH / displayMetrics.densityDpi
//            }
//        }
//        recyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                super.onScrollStateChanged(recyclerView, newState)
//                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    smoothScroller.targetPosition = 0
////                        recyclerView.layoutManager?.findFirstVisibleItemPosition() ?: 0
//                    recyclerView.layoutManager?.startSmoothScroll(smoothScroller)
//                }
//            }
//        })

//        recyclerview.layoutManager = object : LinearLayoutManager(this) {

//            private val MILLISECONDS_PER_INCH = 3000f // Adjust this value to modify the speed
//
//            override fun computeScrollVectorForPosition(targetPosition: Int): PointF? {
//                return super.computeScrollVectorForPosition(targetPosition)?.apply {
//                    x *= MILLISECONDS_PER_INCH / 1000f
//                    y *= MILLISECONDS_PER_INCH / 1000f
//                }
//            }

//            override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
//                return super.calculateSpeedPerPixel(displayMetrics) * 2 // Change this value to modify scroll speed
//            }
//            override fun calculateTimeForDeceleration(dx: Int): Int {
//                return super.calculateTimeForDeceleration(dx) * 2 // Change this value to modify scroll speed
//            }
//        }


//        recyclerview.layoutManager = layoutManager


        recyclerview.setItemAnimator(null)
        recyclerview.getItemAnimator()?.endAnimations()

//        val animator: ItemAnimator = recyclerview.getItemAnimator()!!
//        if (animator is SimpleItemAnimator) {
//            (animator as SimpleItemAnimator).supportsChangeAnimations = false
//        }
        itemsRecyclerAdapter = ItemsRecyclerAdapter(this@MainActivity,this@MainActivity, list)
        recyclerview.adapter = itemsRecyclerAdapter
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
        println("DEBUG: CLICKED item id: ${item.id}")
    }

class SpeedyLinearLayoutManager : LinearLayoutManager {
        constructor(context: Context?) : super(context) {}
        constructor(context: Context?, orientation: Int, reverseLayout: Boolean) : super(
            context,
            orientation,
            reverseLayout
        ) {
        }
        constructor(
            context: Context?,
            attrs: AttributeSet?,
            defStyleAttr: Int,
            defStyleRes: Int
        ) : super(context, attrs, defStyleAttr, defStyleRes) {
        }

        companion object {
            private const val MILISECONDS_PER_INCH = 100f //default is 25f (bigger = slower)
        }

    override fun smoothScrollToPosition(
        recyclerView: RecyclerView,
        state: RecyclerView.State?,
        position: Int
    ) {
//        super.smoothScrollToPosition(recyclerView, state, position);
        val linearSmoothScroller: LinearSmoothScroller =
            object : LinearSmoothScroller(recyclerView.context) {
                override fun computeScrollVectorForPosition(targetPosition: Int): PointF? {
                    return super.computeScrollVectorForPosition(targetPosition)
                }

                override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
//                    return super.calculateSpeedPerPixel(displayMetrics)
                    return MILISECONDS_PER_INCH /displayMetrics.densityDpi
                }
            }
        linearSmoothScroller.targetPosition = position
        startSmoothScroll(linearSmoothScroller)
    }
}




//-------------------------------------------------

//    products_recycler_view.apply {
//        setHasFixedSize(true)
//        adapter = vm.viewAdapter.value
//        val preCachingLinearLayoutManager = PreCachingLinearLayoutManager(context)
//        val dm = DisplayMetrics()
//        activity?.windowManager?.defaultDisplay?.getMetrics(dm)
//        val extraPixels = dm.heightPixels * 2
//        preCachingLinearLayoutManager.extraLayoutSpace = extraPixels
//        layoutManager = preCachingLinearLayoutManager
//        val layoutManagerReference = WeakReference(preCachingLinearLayoutManager)
//        products_recycler_view.postDelayed({
//            layoutManagerReference.get()?.extraLayoutSpace = dm.heightPixels / 2
//        }, 2000L)
//    }


    class PreCachingLinearLayoutManager : LinearLayoutManager {
        var extraLayoutSpace = 0

        constructor(context: Context?) : super(context)
        constructor(context: Context?, orientation: Int, reverseLayout: Boolean) : super(
            context,
            orientation,
            reverseLayout
        )

        constructor(
            context: Context?,
            attrs: AttributeSet?,
            defStyleAttr: Int,
            defStyleRes: Int
        ) : super(context, attrs, defStyleAttr, defStyleRes)


        override fun getExtraLayoutSpace(state: RecyclerView.State): Int {
            return if (extraLayoutSpace > 0) {
                extraLayoutSpace
            } else super.getExtraLayoutSpace(state)
        }
    }
//---------------------------------------------------

//    private fun enableSwipeToDeleteAndUndo() {
//        val callback: SwipeToDeleteCallback = object :
//            SwipeToDeleteCallback(this) {
//            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//                val position = viewHolder.getAdapterPosition()
//                val item = itemsRecyclerAdapter.getData().get(position)
//                itemsRecyclerAdapter.removeItem(position)
//                val snackbar = Snackbar
//                    .make(
//                        recyclerview,
////                        "Item was removed from the list.",
//                        item.title,
//                        Snackbar.LENGTH_LONG
//                    )
//                snackbar.setAction("UNDO") {
//                    itemsRecyclerAdapter.restoreItem(item, position)
//                    recyclerview.scrollToPosition(position)
//                }
//                snackbar.setActionTextColor(Color.GREEN);
//                snackbar.show();
//            }
//        }
//        val itemTouchHelper = ItemTouchHelper(callback)
//        itemTouchHelper.attachToRecyclerView(recyclerview)
//    }






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