package com.example.andriod_game.models

data class ListOfPlayers(
    val allPlayers: List<Player>
)
{
    fun addPlayer(playerCurrent: Player) {
        (this.allPlayers as MutableList).add(playerCurrent)
    }

    class Builder(
        var allPlayers: List<Player> =  mutableListOf()
    )
    {
    fun allPlayer (allPlayers: List<Player>) = apply {this.allPlayers = allPlayers }
    fun addPlayers (player:Player) = apply { (this.allPlayers as MutableList).add(player) }
    fun build() = ListOfPlayers(allPlayers)
    }

}
