package com.example.sunnyweather.ui.place

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.sunnyweather.logic.Repository
import com.example.sunnyweather.logic.model.Place

/**
 * 每当 searchPlaces()函数被调用时，switchMap()方法所对应的转换函数就会执行，将仓库层返回的LiveData对象转换成
 * 一个可供Activity观察的LiveData对象
 */
class PlaceViewModel:ViewModel(){
    private val searchLiveData = MutableLiveData<String>()

    val placeList = ArrayList<Place>()

    val placeLiveData =Transformations.switchMap(searchLiveData){ q ->
        Log.d("PlaceViewModel", "输入字符：$q")
        Repository.searchPlaces(q)
    }

    fun searchPlaces(q:String){
        searchLiveData.value=q
    }
}