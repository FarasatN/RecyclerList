package com.farasatnovruzov.recyclerlist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*
import kotlin.collections.ArrayList

class MainViewModel: ViewModel() {

    lateinit var liveDataList: MutableLiveData<MutableList<ItemModel>>

    init {
        liveDataList = MutableLiveData()
    }

    fun getLiveDataObserver(): MutableLiveData<MutableList<ItemModel>>{
        return liveDataList
    }

    fun getData(){
        val dataList = ArrayList<ItemModel>().toMutableList()
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
        liveDataList.value = dataList
    }
}
