package com.rahul.apps.scoreboard.models

data class Team(
    var name: String = "",
    val score: Score = Score(),
    var overs: Overs = Overs(),
    var wickets: Int = 0,
    val players: MutableList<Player> = mutableListOf()
){
    companion object{
        fun create() = Team()
    }
}