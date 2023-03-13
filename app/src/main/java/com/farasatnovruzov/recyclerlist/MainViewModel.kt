package com.farasatnovruzov.recyclerlist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class MainViewModel: ViewModel() {

    lateinit var liveDataList: MutableLiveData<List<ItemModel>>

    init {
        liveDataList = MutableLiveData()
    }

    fun getLiveDataObserver(): MutableLiveData<List<ItemModel>>{
        return liveDataList
    }

    fun getData(){
        val dataList = ArrayList<ItemModel>()
        for (i in 1..100) {
            dataList.add(ItemModel(12312L+i,i.toLong(),"Novruz Kampaniyasi","19-24 Mart Mohtesem Kampaniya","xyz",
                Date().toString(),1,"WithUrl",0,false,2,1))
        }
        for (i in 1..100) {
            dataList.add(ItemModel(12312L+i,i.toLong(),"Ramazan Kampaniyasi","19-24 Mart Mohtesem Kampaniya","xyz",
                Date().toString(),1,"WithUrl",0,false,2,1))
        }
        liveDataList.value = dataList
    }
}
