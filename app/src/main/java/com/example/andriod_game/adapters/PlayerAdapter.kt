package com.example.andriod_game.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.andriod_game.Interfaces.CallBackButtonHighScoreClick
import com.example.andriod_game.databinding.PlayerItemBinding
import com.example.andriod_game.models.Player

class PlayerAdapter(private val players: List<Player>): RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder>()
{
    var locationCallBack: CallBackButtonHighScoreClick?  = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlayerViewHolder {
        val binding = PlayerItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PlayerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        with(holder)
        {
            with(players.get(position))
            {
                binding.playerItemFragmentMTVScore.text = score.toString()
                binding.playerItemFragmentMTVName.text  = name


            }
        }
    }

    override fun getItemCount(): Int  = players.size

    fun getPlayer (position: Int) = players[position]

    inner class PlayerViewHolder(val binding: PlayerItemBinding):
            RecyclerView.ViewHolder(binding.root)
    {
        init {
            binding.playerItemFragmentCVData.setOnClickListener{
                Log.d("locationCallBack"  , "locationCallBack has been click!!!")
                if (locationCallBack!=null)
                {
                    locationCallBack!!.zoom(getPlayer(absoluteAdapterPosition).location)
                }
            }
        }
    }

}