package com.farasatnovruzov.recyclerlist

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*


class MainActivity : AppCompatActivity(), ItemsRecyclerAdapter.Interaction {

    lateinit var viewModel: MainViewModel
    lateinit var itemsRecyclerAdapter: ItemsRecyclerAdapter
    lateinit var recyclerview : RecyclerView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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





    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        return super.onCreateOptionsMenu(menu)

        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.list_menu, menu)

        val searchItem: MenuItem = menu!!.findItem(R.id.action_search)
        val searchView: SearchView = searchItem.getActionView() as SearchView

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                itemsRecyclerAdapter.getFilter().filter(newText)
                return false
            }
        })
        return true
    }

    private fun initRecyclerView(list: List<ItemModel>){
        recyclerview.setHasFixedSize(true);
        recyclerview.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            itemsRecyclerAdapter = ItemsRecyclerAdapter(this@MainActivity, list)
            adapter = itemsRecyclerAdapter
        }
    }

    private fun initViewModel(){
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.getLiveDataObserver().observe(this, androidx.lifecycle.Observer {
            if (it != null){
                initRecyclerView(it)
                itemsRecyclerAdapter.submitList(it)
                itemsRecyclerAdapter.notifyDataSetChanged()
            }else{
                Toast.makeText(this,"Error in getting list", Toast.LENGTH_SHORT).show()
            }
        })
        viewModel.getData()
    }

    override fun onItemSelected(position: Int, item: ItemModel) {
        println("DEBUG: CLICKED position : $position")
        println("DEBUG: CLICKED item: $item")
    }







    fun isHuaweiDevice():Boolean{
        val manufacturer = Build.MANUFACTURER
        val brand = Build.BRAND
        Log.i("TAGGGG", "isHuaweiDevice:Build.MANUFACTURER:${Build.MANUFACTURER},Build.BRAND:${Build.BRAND} ")
        return manufacturer.toUpperCase(Locale.getDefault()).contains("HUAWEI") || brand.toUpperCase(Locale.getDefault()).contains("HUAWEI")
    }

}


