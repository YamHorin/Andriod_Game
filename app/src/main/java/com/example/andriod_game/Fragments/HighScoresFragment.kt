package com.example.andriod_game.Fragments
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.andriod_game.Interfaces.CallBackButtonHighScoreClick
import com.example.andriod_game.Interfaces.CallBackGetBackToMainMenu
import com.example.andriod_game.adapters.PlayerAdapter
import com.example.andriod_game.databinding.FragmentHighScoresBinding
import com.example.andriod_game.models.ListOfPlayers
import com.example.andriod_game.models.Player
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson


class HighScoresFragment : Fragment() {
    private lateinit var binding: FragmentHighScoresBinding
    private lateinit var players :ListOfPlayers

    var callBackGetBackToMainMenu: CallBackGetBackToMainMenu? = null
    var callBackButtonHighScoreClick: CallBackButtonHighScoreClick? = null
    companion object {
        fun newInstance(playersJson: String): HighScoresFragment {
            val fragment = HighScoresFragment()
            val args = Bundle()
            args.putString("Players", playersJson)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHighScoresBinding.inflate(inflater, container, false)
        val top10PlayersFromSp = arguments?.getString("Players" )
        val gson = Gson()
        players =gson.fromJson(top10PlayersFromSp ,ListOfPlayers::class.java )
        var list : List<Player> = players.allPlayers.sortedByDescending {it.score}
        //get the top 10 players
        if (list.size>10)
            list = list.subList(0,9)
        binding.HighScoresFragmentFABGoBack.setOnClickListener { goBackToMainMenu() }
        val playerAdapter = PlayerAdapter(list)
        playerAdapter.locationCallBack = callBackButtonHighScoreClick
        binding.HighScoresFragmentRVList.adapter = playerAdapter
        binding.HighScoresFragmentRVList.layoutManager = LinearLayoutManager(requireContext())

        return binding.root
    }

    private fun goBackToMainMenu() {
        callBackGetBackToMainMenu?.GetBackMainMenu()
    }


}