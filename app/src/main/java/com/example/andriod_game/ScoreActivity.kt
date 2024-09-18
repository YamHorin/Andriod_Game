package com.example.andriod_game

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.andriod_game.Fragments.GetNameFragment
import com.example.andriod_game.Fragments.HighScoresFragment
import com.example.andriod_game.Fragments.MapsFragment
import com.example.andriod_game.Interfaces.HighScoreItemClicked
import com.example.andriod_game.Interfaces.CallBackButtonHighScoreClick
import com.example.andriod_game.Interfaces.CallBackGetBackToMainMenu
import com.example.andriod_game.models.ListOfPlayers
import com.example.andriod_game.models.Player
import com.example.andriod_game.utilities.Constants
import com.example.andriod_game.utilities.Context
import com.example.andriod_game.utilities.SharedPreferencesManager
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson

class ScoreActivity : AppCompatActivity() {
    private lateinit var listOfPlayers:ListOfPlayers
    private lateinit var scoreActivityFrameUp: FrameLayout
    private lateinit var scoreActivityFrameMap:FrameLayout
    private lateinit var getNameFragment:GetNameFragment
    private lateinit var highScoresFragment:HighScoresFragment
    private lateinit var mapsFragment:MapsFragment
    private lateinit var locationPermissionRequest: ActivityResultLauncher<Array<String>>
    private var gson:Gson = Gson()
    private lateinit var locationCurrent :LatLng
    var scorePLayer :Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_score)
        findViews()
        initViews()
        }

    private fun findViews() {
        scoreActivityFrameUp = findViewById(R.id.scoreActivity_Frame_up)
        scoreActivityFrameMap = findViewById(R.id.scoreActivity_Frame_map)
    }

    private fun initViews() {
        val bundle: Bundle? = intent.extras

        val score = bundle?.getInt(Context.SCORE_KEY, 0)
        if (score != null) {
            scorePLayer = score
        }
        val previousActivity = bundle?.getString(Context.ACTIVITY_KEY)
        SharedPreferencesManager.init(this)
        listOfPlayers =getListOfPlayerFromSP()
        if (previousActivity=="main")
        {
            //location permission
            locationPermissionRequest = registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) { permissions ->
                when {
                    permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                        requestLocationUpdates()
                    }
                    permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                        requestLocationUpdates()
                    } else -> {
                    Toast.makeText(this, "Access to mobile phone location is necessary", Toast.LENGTH_LONG).show()
                    }
                }
            }
            //get permission  location
            locationPermissionRequest.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
            //getName
            getNameFragment = GetNameFragment()
            getNameFragment.calbackHighScoreItemClicked = object: HighScoreItemClicked
            {
                override fun highScoreItemClicked(name: String) {
                    requestLocationUpdates()
                    Log.d("location player", "got player location $locationCurrent")
                    //update score in the SP
                    val playerCurrent : Player = Player.Builder()
                        .name(name)
                        .score(scorePLayer)
                        .location(locationCurrent)
                        .build()
                    //add player and save
                    listOfPlayers.addPlayer(playerCurrent)
                    SharedPreferencesManager.getInstance().putString(Constants.LIST_KEY ,gson.toJson(listOfPlayers))
                    //check saving
                    val listPLayersFromSP = SharedPreferencesManager.getInstance().getString(Constants.LIST_KEY, "")
                    Log.d("listPLayersFromSP",
                        "***saved a new value in listPLayersFromSP: $listPLayersFromSP"
                    )
                    //move fragments
                    highScoresFragment = HighScoresFragment.newInstance(gson.toJson(listOfPlayers))
                    mapsFragment = MapsFragment.init(gson.toJson(listOfPlayers))
                    //Call Back zoom on camera
                    highScoresFragment.callBackButtonHighScoreClick = object: CallBackButtonHighScoreClick
                    {
                        override fun zoom(locationPlayer: LatLng) {
                            Log.d("zoom" , "zoom in the map has been made")
                            mapsFragment.zoom(locationPlayer)
                        }
                    }
                    // Call back get back to the first screen

                    highScoresFragment.callBackGetBackToMainMenu = object : CallBackGetBackToMainMenu
                    {
                        override fun GetBackMainMenu() {
                            moveActivityMainMenu()
                        }

                    }
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.scoreActivity_Frame_up , highScoresFragment  ,null)
                        .setReorderingAllowed(true).commit()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.scoreActivity_Frame_up, highScoresFragment)
                        .add(R.id.scoreActivity_Frame_map, mapsFragment)
                        .setReorderingAllowed(true)
                        .commit()

                    Log.d("move to high score screen " , "R.id.scoreActivity_Frame_map have been init")
                    }
            }
            supportFragmentManager.beginTransaction().add(R.id.scoreActivity_Frame_up , getNameFragment).commit()
        }
        else
        {
            highScoresFragment = HighScoresFragment.newInstance(gson.toJson(listOfPlayers))
            mapsFragment = MapsFragment.init(gson.toJson(listOfPlayers))

            //Call Back zoom on camera
            highScoresFragment.callBackButtonHighScoreClick = object: CallBackButtonHighScoreClick
            {
                override fun zoom(locationPlayer: LatLng) {
                    Log.d("zoom" , "zoom in the map has been made")
                    mapsFragment.zoom(locationPlayer)
                }
            }
            // Call back get back to the first screen

            highScoresFragment.callBackGetBackToMainMenu = object : CallBackGetBackToMainMenu
            {
                override fun GetBackMainMenu() {
                    moveActivityMainMenu()
                }

            }

            supportFragmentManager.beginTransaction().add(R.id.scoreActivity_Frame_up , highScoresFragment).commit()
            supportFragmentManager.beginTransaction().add(R.id.scoreActivity_Frame_map , mapsFragment).commit()
        }

    }

    private fun requestLocationUpdates() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request permissions if not granted
            locationPermissionRequest.launch(arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION))
        } else {
            // Permissions already granted, get the location
            val currentLocationRequest = CurrentLocationRequest.Builder().build()
            fusedLocationClient.getCurrentLocation(currentLocationRequest, null)
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        locationCurrent = LatLng(location.latitude, location.longitude)
                        Log.d("locationCurrent", "Location: $locationCurrent") // Log the location
                    } else {
                        Log.d("locationCurrent", "****error could get location locationCurrent =null")
                    }
                }
                .addOnFailureListener { exception: Exception ->
                    Log.e("LocationError", "Error getting location", exception)
                }
        }
    }



    private fun getListOfPlayerFromSP(): ListOfPlayers {
        val listPLayersFromSP :String = SharedPreferencesManager.getInstance().getString(Constants.LIST_KEY, "")
        if (listPLayersFromSP =="")
        {
            return ListOfPlayers.Builder().build()
        }
        val listOfPLayersOBjFromSP: ListOfPlayers = gson.fromJson(listPLayersFromSP, ListOfPlayers::class.java)
        Log.d("listPLayersFromSP", "$$ look at listPLayersFromSP: ${listOfPLayersOBjFromSP.allPlayers}")
        //get the top 10 player only
        return listOfPLayersOBjFromSP
    }
    private fun moveActivityMainMenu()
    {
        val intent = Intent(this, StartMenuActivity::class.java)
        val bundle = Bundle()
        intent.putExtras(bundle)
        startActivity(intent)
        finish()
    }

}