package com.example.sunnyweather.ui.weather

import android.content.Context
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.WindowCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.sunnyweather.R
import com.example.sunnyweather.databinding.ActivityWeatherBinding
import com.example.sunnyweather.logic.model.Weather
import com.example.sunnyweather.logic.model.getSky
import com.example.sunnyweather.ui.place.WeatherViewModel
import java.util.Locale

class WeatherActivity:AppCompatActivity() {
    val viewModel by lazy { ViewModelProvider(this).get(WeatherViewModel::class.java)}
    lateinit var binding: ActivityWeatherBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //沉浸状态栏
        WindowCompat.setDecorFitsSystemWindows(window, false)
        //设置状态栏透明
        window.statusBarColor = Color.TRANSPARENT

        //setContentView(R.layout.activity_weather)
        binding= ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(viewModel.lng.isEmpty()){
            viewModel.lng=intent.getStringExtra("location_lng")?:""
        }
        if(viewModel.lat.isEmpty()){
            viewModel.lat=intent.getStringExtra("location_lat")?:""
        }
        if (viewModel.placeName.isEmpty()){
            viewModel.placeName=intent.getStringExtra("place_name")?:""
        }
        viewModel.weatherLiveData.observe(this, Observer { result->
            val weather=result.getOrNull()
            if(weather!=null){
                showWeatherInfo(weather)
            }else{
                Toast.makeText(this,"无法获取天气信息",Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
            binding.swipeRefresh.isRefreshing = false
        })

        //下拉刷新
        binding.swipeRefresh.setColorSchemeResources(R.color.colorPrimary)
        refreshWeather()
        binding.swipeRefresh.setOnRefreshListener {
            refreshWeather()
        }

        //滑动菜单
        binding.now.nav.setOnClickListener{
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
        binding.drawerLayout.addDrawerListener(object :DrawerLayout.DrawerListener{
            override fun onDrawerStateChanged(newState: Int) { }
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) { }
            override fun onDrawerOpened(drawerView: View) { }
            override fun onDrawerClosed(drawerView: View) {
                val manager=getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                manager.hideSoftInputFromWindow(drawerView.windowToken,InputMethodManager.HIDE_NOT_ALWAYS)
            }
        })
    }

    fun refreshWeather(){
        viewModel.refreshWeather(viewModel.lng,viewModel.lat)
        binding.swipeRefresh.isRefreshing=true
    }

    private fun showWeatherInfo(weather: Weather) {
        binding.now.placeName.text=viewModel.placeName
        val realtime=weather.realtime
        val daily=weather.daily

        //填充now.xml
        val text1=realtime.temperature.toString() + "°c"
        binding.now.currentTemp.text = text1
        binding.now.currentSky.text= getSky(realtime.skycon).info
        val text2="空气指数${realtime.airQuality.aqi.chn.toInt()}"
        binding.now.currentAQI.text = text2
        binding.now.nowLayout.setBackgroundResource(getSky(realtime.skycon).bg)

        //填充forecast.xml
        binding.forecast.forecastLayout.removeAllViews()
        val days=daily.skycon.size
        for (i in 0 until days) {
            val skycon = daily.skycon[i]
            println(skycon)
            val temperature = daily.temperature[i]
            val view = LayoutInflater.from(this).inflate(
                R.layout.forecast_item,
                binding.forecast.forecastLayout, false)
            val dateInfo = view.findViewById(R.id.dateInfo) as TextView
            val skyIcon = view.findViewById(R.id.skyIcon) as ImageView
            val skyInfo = view.findViewById(R.id.skyInfo) as TextView
            val temperatureInfo = view.findViewById(R.id.temperatureInfo) as TextView
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            dateInfo.text = simpleDateFormat.format(skycon.date)
            val sky = getSky(skycon.value)
            skyIcon.setImageResource(sky.icon)
            skyInfo.text = sky.info
            val tempText = "${temperature.min.toInt()} ~ ${temperature.max.toInt()} ℃"
            temperatureInfo.text = tempText
            binding.forecast.forecastLayout.addView(view)
        }

        //填充life_index.xml布局中的数据
        val lifeIndex = daily.lifeIndex
        binding.lifeIndexLayout.coldRiskText.text = lifeIndex.coldRisk[0].desc
        binding.lifeIndexLayout.dressingText.text = lifeIndex.dressing[0].desc
        binding.lifeIndexLayout.ultravioletText.text = lifeIndex.ultraviolet[0].desc
        binding.lifeIndexLayout.carWashingText.text = lifeIndex.carWashing[0].desc

        binding.weatherLayout.visibility = View.VISIBLE
    }
}