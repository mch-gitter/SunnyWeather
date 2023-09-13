package com.example.sunnyweather.logic.model

import com.google.gson.annotations.SerializedName
import java.util.Date

class DailyResponse(val status:String,val result:Result) {
    data class Description(val desc:String)

    data class LifeIndex(val coldRisk:List<Description>,val carWashing:List<Description>,
                         val ultraviolet: List<Description>, val dressing:List<Description>)

    data class Skycon(val value:String,val date:Date)

    data class Temperature(val max:Float,val min:Float)

    data class Daily(val temperature: List<Temperature>,val skycon:List<Skycon>,
                     @SerializedName("life_index") val lifeIndex:LifeIndex)
    data class Result(val daily:Daily)
}