package com.rahul.apps.scoreboard.models

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