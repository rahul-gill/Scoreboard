package com.rahul.apps.scoreboard.models

import androidx.room.Entity

@Entity
data class Team(
    var name: String = "",
    val score: Score = Score(),
    var overs: Overs = Overs(),
    var wickets: Int = 0,
    val players: MutableList<Player> = mutableListOf()
)