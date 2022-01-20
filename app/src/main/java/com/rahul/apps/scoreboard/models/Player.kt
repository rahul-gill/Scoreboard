package com.rahul.apps.scoreboard.models


data class Player(
    var name: String = "",
    var battingRecord: BattingRecord = BattingRecord(),
    var bowlingRecord: BowlingRecord = BowlingRecord(),
    var isBowling: Boolean = false,
    var isBatting: Boolean = false
)

data class BattingRecord(
    var overs: Overs = Overs(),
    var score: Score = Score(),
)
data class BowlingRecord(
    var overs: Overs = Overs(),
    var score: Score = Score(),
    var wickets: Int = 0,
    var widesOrNoBowls: Int = 0,
)