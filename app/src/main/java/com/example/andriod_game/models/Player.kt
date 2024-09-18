package com.example.andriod_game.models

import com.google.android.gms.maps.model.LatLng

data class Player private constructor(
    val name :String,
    val score: Int,
    val location : LatLng
)
{
    class Builder(
        var name :String = "",
        var score: Int = 0,
        var location : LatLng =LatLng(0.0, 0.0)
    )
    {
        fun name (name: String) = apply {this.name = name  }
        fun score (score:Int) = apply {this.score = score  }
        fun location (location:LatLng ) = apply {this.location = location  }
        fun build() = Player(name , score , location)
    }
}
