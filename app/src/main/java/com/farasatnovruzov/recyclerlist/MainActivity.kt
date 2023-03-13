package com.farasatnovruzov.recyclerlist

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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

    private fun initRecyclerView(list: List<ItemModel>){
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

}


