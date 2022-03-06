package com.rahul.apps.scoreboard.models

import androidx.room.Entity

@Entity
data class Player(
    var name: String = "",
    var battingRecord: BattingRecord = BattingRecord(),
    var bowlingRecord: BowlingRecord = BowlingRecord(),
    var isBowling: Boolean = false,
    var isBatting: Boolean = false,
    var isOut: Boolean = false
){
    val strikeRate
        get() = battingRecord.overs.totalBowls.let { bowls ->
            if(bowls == 0) "0.0"
            else "%.2f".format(battingRecord.score.runs.toDouble() / bowls)
        }
}

@Entity
data class BattingRecord(
    var overs: Overs = Overs(),
    var score: Score = Score(),
)
@Entity
data class BowlingRecord(
    var overs: Overs = Overs(),
    var score: Score = Score(),
    var wickets: Int = 0,
    var widesOrNoBowls: Int = 0,
)