package com.example.andriod_game.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.andriod_game.Interfaces.HighScoreItemClicked
import com.example.andriod_game.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText


class GetNameFragment : Fragment() {
    private lateinit var highScores_ET_location: TextInputEditText
    private lateinit var highScores_BTN_send: MaterialButton
    var calbackHighScoreItemClicked: HighScoreItemClicked? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_get_name, container, false)
        findViews(v)
        initViews(v)
        return v
    }

    private fun initViews(v: View) {
        highScores_BTN_send.setOnClickListener { v: View ->
            var name  = highScores_ET_location.text
            itemClicked(name.toString())
        }

    }

    private fun itemClicked(name:String ) {
        calbackHighScoreItemClicked?.highScoreItemClicked(name)
    }

    private fun findViews(v: View) {
        highScores_ET_location = v.findViewById(R.id.highScores_ET_location)
        highScores_BTN_send = v.findViewById(R.id.highScores_BTN_send)
    }


}