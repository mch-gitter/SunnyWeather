package com.example.sunnyweather.ui.place

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sunnyweather.R
import com.example.sunnyweather.logic.model.Place
import com.example.sunnyweather.ui.weather.WeatherActivity

class PlaceAdapter(private val fragment: PlaceFragment,private val placelist:List<Place>):
    RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {
    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val placename:TextView=view.findViewById(R.id.placeName)
        val placeaddress:TextView=view.findViewById(R.id.placeAddress)
    }

    override fun getItemCount()=placelist.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.place_item,parent,false)
        val holder=ViewHolder(view)
        holder.itemView.setOnClickListener{
            val position=holder.adapterPosition
            val place=placelist[position]
            val intent=Intent(parent.context,WeatherActivity::class.java).apply {
                putExtra("location_lng",place.location.lng)
                putExtra("location_lat", place.location.lat)
                putExtra("place_name", place.name)
            }
            fragment.viewModel.savePlace(place)
            fragment.startActivity(intent)
            fragment.activity?.finish()
        }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place=placelist[position]
        holder.placename.text=place.name
        holder.placeaddress.text=place.address
    }
}