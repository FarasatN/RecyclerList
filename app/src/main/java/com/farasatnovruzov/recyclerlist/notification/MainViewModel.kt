package com.farasatnovruzov.recyclerlist.notification

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class MainViewModel: ViewModel() {

    var liveDataList: MutableLiveData<MutableList<ItemModel>>
    var dataList: MutableList<ItemModel>

    init {
        liveDataList = MutableLiveData()
        dataList = mutableListOf()
        dataList.add(
            ItemModel(1L,1.toLong(),"Farasat card","19-24 Mart Mohtesem Kampaniya","xyz",
            Date().toString(),1,"WithUrl",0,false,2,1)
        )
        for (i in 1..1000) {
            dataList.add(
                ItemModel(123L+i,i.toLong(),"Novruz Kampaniyasi","19-24 Mart Mohtesem Kampaniya","xyz",
                Date().toString(),1,"WithUrl",0,false,2,1)
            )
        }
        for (i in 1..1000) {
            dataList.add(
                ItemModel(123L+i,i.toLong(),"Ramazan Kampaniyasi","19-24 Mart Mohtesem Kampaniya","xyz",
                Date().toString(),1,"WithUrl",0,false,2,1)
            )
        }
        dataList.add(
            ItemModel(123L,1.toLong(),"Fərasət üçün","19-24 Mart Mohtesem Kampaniya","xyz",
            Date().toString(),1,"WithUrl",0,false,2,1)
        )

//        liveDataList.value = dataList
    }






    fun getData(){
        liveDataList.value = dataList
    }

    fun clearData(){
        dataList.clear()
        liveDataList.value = dataList
    }


    fun removeItem(position: Int){
        dataList.removeAt(position)
        liveDataList.value = dataList
    }

    fun getItem(position: Int): ItemModel {
        dataList.get(position)
        liveDataList.value = dataList

        return liveDataList.value!!.get(position)
    }
    fun addItem(position: Int,item: ItemModel){
        dataList.add(position,item)
        liveDataList.value = dataList
    }



//    fun getLiveDataObserver(): MutableLiveData<MutableList<ItemModel>>{
//        return liveDataList
//    }
}
