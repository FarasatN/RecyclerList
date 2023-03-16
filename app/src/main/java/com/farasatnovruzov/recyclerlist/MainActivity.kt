package com.farasatnovruzov.recyclerlist

import android.graphics.Color
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
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), ItemsRecyclerAdapter.Interaction {

    lateinit var viewModel: MainViewModel
    lateinit var itemsRecyclerAdapter: ItemsRecyclerAdapter
    lateinit var recyclerview : RecyclerView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar  = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.title = "Notifications"
        toolbar.setTitleTextColor(Color.WHITE)

        toolbar.setNavigationOnClickListener(View.OnClickListener { finish() })


        recyclerview = findViewById<RecyclerView>(R.id.recyclerView)

//        viewModel = this.run {
//            ViewModelProvider(this@MainActivity).get(MainViewModel::class.java)
//        }?: throw Exception("Invalid Activity")

        // this creates a vertical layout Manager
//        recyclerview.layoutManager = LinearLayoutManager(this)
        // ArrayList of class ItemsViewModel

//        val dataList = ArrayList<ItemModel>()
//        for (i in 1..10) {
//            dataList.add(ItemModel(12312L+i,i.toLong(),"Novruz Kampaniyasi","19-24 Mart Mohtesem Kampaniya","xyz",Date().toString(),1,"WithUrl",0,false,2,1))
//        }
//        for (i in 1..10) {
//            dataList.add(ItemModel(12312L+i,i.toLong(),"Ramazan Kampaniyasi","19-24 Mart Mohtesem Kampaniya","xyz",Date().toString(),1,"WithUrl",0,false,2,1))
//        }

//        val adapter = ItemsRecyclerAdapter(dataList)

        // Setting the Adapter with the recyclerview
//        recyclerview.adapter = adapter

//        initRecyclerView(dataList)
//        itemsRecyclerAdapter.submitList(dataList)



        initViewModel()

        isHuaweiDevice()




        var deletedItem:ItemModel? = null
        val archivedItems : MutableList<ItemModel?> = ArrayList()

        val callback: ItemTouchHelper.SimpleCallback = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.getAdapterPosition()
                when (direction) {
                    ItemTouchHelper.LEFT -> {
//                        deletedItem = getItem(position)
//                        viewModel.liveDataList.value!!.removeAt(position)
//                        viewModel.liveDataList.postValue()
//                        removeItem(position)

                        viewModel.liveDataList.value!!.removeAt(position)

                        itemsRecyclerAdapter.notifyItemRemoved(position)
//                        Snackbar.make(recyclerview, deletedItem!!.title, Snackbar.LENGTH_LONG)
//                            .setAction("Undo", View.OnClickListener {
////                                viewModel.addItem(position, deletedItem!!)
//                                viewModel.liveDataList.value!!.add(position,deletedItem!!)
//
//                                itemsRecyclerAdapter.notifyItemInserted(position)
//                            })
//                            .show()
                    }

//                    ItemTouchHelper.RIGHT -> {
//                        val item = getItem(position)
//                        archivedItems.add(item)
//                        removeItem(position)
//                        itemsRecyclerAdapter.notifyItemRemoved(position)
//                        Snackbar.make(recyclerview, item!!.title+", archived.", Snackbar.LENGTH_LONG)
//                            .setAction("Undo", View.OnClickListener {
//                                viewModel.addItem(position,item)
////                                archivedItems.removeAt(archivedItems.lastIndexOf(item))
////                                archivedItems.add(position,item)
//                                itemsRecyclerAdapter.notifyItemInserted(position)
//                            }).show()
//                    }
                }
            }
        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(recyclerview)


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.list_menu, menu)

        val searchItem: MenuItem = menu!!.findItem(R.id.action_search)
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
        return if (id == R.id.action_search) {
            true
        } else super.onOptionsItemSelected(item)
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

    private fun initViewModel(){
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.getLiveDataObserver().observe(this, androidx.lifecycle.Observer {
            if (it != null){
                initRecyclerView(it)
                itemsRecyclerAdapter
//                itemsRecyclerAdapter.notifyDataSetChanged()
            }else{
                Toast.makeText(this,"Error in getting list", Toast.LENGTH_SHORT).show()
            }
        })
        viewModel.getData()
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




    override fun onItemSelected(position: Int, item: ItemModel) {
        println("DEBUG: CLICKED position : $position")
        println("DEBUG: CLICKED item: $item")
    }











    fun isHuaweiDevice():Boolean{
        val manufacturer = Build.MANUFACTURER
        val brand = Build.BRAND
        Log.v("TAGGGG", "isHuaweiDevice:Build.MANUFACTURER:${Build.MANUFACTURER},Build.BRAND:${Build.BRAND} ")
        return manufacturer.toUpperCase(Locale.getDefault()).contains("HUAWEI") || brand.toUpperCase(Locale.getDefault()).contains("HUAWEI")
    }

}


