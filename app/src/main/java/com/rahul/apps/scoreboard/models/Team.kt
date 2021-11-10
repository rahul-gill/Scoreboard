package com.rahul.apps.scoreboard.models

import android.util.Log

data class Score(
    var runs: Int = 0,
    var sixes: Int = 0,
    var fours: Int = 0,
    var singleRuns: Int = 0,
    var doubleRuns: Int = 0,
    var tripleRuns: Int = 0,
    var dotBowls: Int = 0,
){
    fun singleRun(){ runs++; singleRuns++ }
    fun doubleRun(){ runs+=2; doubleRuns++ }
    fun tripleRun(){ runs+=3; tripleRuns++ }
    fun six(){ runs+=6; sixes++ }
    fun four(){ runs+=4; fours++ }
    fun dotBowl(){ dotBowls++ }

}

data class Overs(
    var overs: Int = 0,
    var bowls: Int = 0,
){
    operator fun inc(): Overs{
        if (bowls == 5){
            overs++
            bowls = 0
        }
        else bowls++
        return this
    }
    operator fun dec(): Overs{
        if (bowls == 1){
            overs--
            bowls = 0
        }
        else bowls--
        return this
    }
    val totalBowls: Int
        get() = overs*6 + bowls

}

data class Team(
    var name: String = "",
    val score: Score = Score(),
    var overs: Overs = Overs(),
    var wickets: Int = 0,
    val players: MutableList<Player> = mutableListOf()
)