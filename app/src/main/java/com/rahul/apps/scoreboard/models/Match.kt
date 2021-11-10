package com.rahul.apps.scoreboard.models

import android.util.Log

enum class BowlResult{
    DOT_BOWL,
    SINGLE_RUN, DOUBLE_RUN, TRIPLE_RUN, FOUR, SIX,
    WIDE_OR_NO_BOWL,
    LBW_BOLD_STUMPS,CATCH_OUT,
    NOTHING
}

class Match {
    var battingTeam = Team()
    var bowlingTeam = Team()
    var batsman = Player()
        set(value){
            field.isBatting = false
            field = value
            field.isBatting = true
        }
    var nonStrikeBatsman = Player()

    var nextBatsmanInQueue: Player? = Player()
    var bowler = Player()

    fun nextBowl(bowlResult: BowlResult, bowlResultExtra: BowlResult = BowlResult.NOTHING){
        processBowlResult(bowlResult)
        processBowlResult(bowlResultExtra)
    }
    fun switchTeams(){
        battingTeam = bowlingTeam.also { bowlingTeam = battingTeam }
        batsman = battingTeam.players.first()
        bowler = bowlingTeam.players.last()
        nextBatsmanInQueue =
            if(1 < battingTeam.players.size) battingTeam.players[1]
            else null
    }
    private fun processBowlResult(bowlResult: BowlResult){
        if(bowlResult != BowlResult.WIDE_OR_NO_BOWL && bowlResult != BowlResult.NOTHING){
            oversBowlPlus()
        }
        when(bowlResult){
            BowlResult.SINGLE_RUN -> singleRun()
            BowlResult.DOUBLE_RUN -> doubleRun()
            BowlResult.TRIPLE_RUN -> tripleRun()
            BowlResult.FOUR -> four()
            BowlResult.SIX -> six()
            BowlResult.WIDE_OR_NO_BOWL -> wideOrNoBowl()
            BowlResult.LBW_BOLD_STUMPS -> wicket()
            BowlResult.CATCH_OUT -> wicket()
            else -> {}
        }
    }

    private fun oversBowlPlus(){
        batsman.battingRecord.overs++
        bowler.bowlingRecord.overs++
        battingTeam.overs++
        if(battingTeam.overs.bowls == 0)
            batsman = nonStrikeBatsman.also { nonStrikeBatsman = batsman }
    }
    private fun singleRun(){
        batsman.battingRecord.score.singleRun()
        battingTeam.score.singleRun()
        bowler.bowlingRecord.score.singleRun()
        batsman = nonStrikeBatsman.also { nonStrikeBatsman = batsman }
    }
    private fun doubleRun(){
        batsman.battingRecord.score.doubleRun()
        battingTeam.score.doubleRun()
        bowler.bowlingRecord.score.doubleRun()
    }
    private fun tripleRun(){
        batsman.battingRecord.score.tripleRun()
        battingTeam.score.tripleRun()
        bowler.bowlingRecord.score.tripleRun()
        batsman = nonStrikeBatsman.also { nonStrikeBatsman = batsman }
    }
    private fun four(){
        batsman.battingRecord.score.four()
        battingTeam.score.four()
        bowler.bowlingRecord.score.four()
    }
    private fun six(){
        batsman.battingRecord.score.six()
        battingTeam.score.six()
        bowler.bowlingRecord.score.six()
    }
    private fun wideOrNoBowl(){
        bowler.bowlingRecord.score.singleRun()
        battingTeam.score.singleRun()
        bowler.bowlingRecord.widesOrNoBowls++
    }
    private fun wicket(){
        bowler.bowlingRecord.wickets++
        battingTeam.wickets++
        if(nextBatsmanInQueue != null){
            batsman = nextBatsmanInQueue!!
        }
        else switchTeams()
        // have to update nextBatsmanInQueue after it
    }
}