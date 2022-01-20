package com.rahul.apps.scoreboard.models

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