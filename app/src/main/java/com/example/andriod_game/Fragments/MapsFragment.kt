package com.example.andriod_game.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.andriod_game.R
import com.example.andriod_game.models.ListOfPlayers
import com.example.andriod_game.models.Player
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson

class MapsFragment : Fragment()  {
    private lateinit var playersTop10 :ListOfPlayers
    private lateinit var googleMapObj: GoogleMap

    companion object {
        fun init(playersJson: String): MapsFragment {
            val fragment = MapsFragment()
            val args = Bundle()
            args.putString("top10Players", playersJson)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_maps, container, false)
        val top10PlayersFromSp = arguments?.getString("top10Players")
        val gson = Gson()
        playersTop10 = gson.fromJson(top10PlayersFromSp, ListOfPlayers::class.java)
        var list : List<Player> = playersTop10.allPlayers.sortedByDescending {it.score}
        //get the top 10 players
        if (list.size>10)
            list = list.subList(0,9)
        val supportMapFragment =
            (childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?)
        supportMapFragment!!.getMapAsync { googleMap ->
            googleMapObj = googleMap
            for (player in list) {
                googleMap.addMarker(MarkerOptions().position(player.location).title(player.name))
            }

        }
        return view
    }

        fun zoom(location: LatLng) {
            Log.d("zoom" , "zoom in the map has been made")
            moveCameraMap(location)
        }
    private fun moveCameraMap(location: LatLng)
    {
        googleMapObj.clear()
        googleMapObj.addMarker(MarkerOptions().position(location).title("player location"))
        val cameraPosition = CameraPosition.Builder()
            .target(location)
            .zoom(11f)
            .build()
        googleMapObj.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }



}

