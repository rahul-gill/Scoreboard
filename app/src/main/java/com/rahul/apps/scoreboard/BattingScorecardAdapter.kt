package com.rahul.apps.scoreboard


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rahul.apps.scoreboard.databinding.BattingScorecardItemBinding
import com.rahul.apps.scoreboard.models.Player

class BattingScorecardAdapter: RecyclerView.Adapter<BattingScorecardAdapter.ViewHolder>(){
    var data = mutableListOf<BattingScorecardItem>(BattingScorecardItem.Header)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }
    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(private val binding: BattingScorecardItemBinding): RecyclerView.ViewHolder(binding.root){
        @SuppressLint("SetTextI18n")
        fun bind(player: BattingScorecardItem){
            if(player is BattingScorecardItem.Header){
                binding.playerName.text = "P"
                binding.playerRunsBowls.text = "R(B)"
                binding.playerFours.text = "4"
                binding.playerSixes.text = "6"
                binding.playerStrikeRate.text ="SR"
            }
            else {
                val pl = (player as BattingScorecardItem.PlayerData)
                binding.playerName.text = pl.name + if(pl.onStrike) "*" else ""
                binding.playerRunsBowls.text = pl.runsBowls
                binding.playerFours.text = pl.fours.toString()
                binding.playerSixes.text = pl.sixes.toString()
                binding.playerStrikeRate.text = "%.2f".format(pl.strikeRate)
            }
        }
        companion object{
            fun from(parent: ViewGroup): ViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = BattingScorecardItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

sealed class BattingScorecardItem{
    object Header: BattingScorecardItem()
    class PlayerData(
        var name: String,
        var runsBowls: String,
        var fours: Int,
        var sixes: Int,
        var strikeRate: Double,
        var onStrike: Boolean
    ): BattingScorecardItem()
}
fun playerToBattingScorecardItem(player: Player): BattingScorecardItem.PlayerData{
    val scoreObj = player.battingRecord.score
    val bowls = player.battingRecord.overs.totalBowls
    return BattingScorecardItem.PlayerData(
        name = player.name,
        runsBowls = "${scoreObj.runs}($bowls)",
        fours = scoreObj.fours,
        sixes = scoreObj.sixes,
        strikeRate = if(bowls == 0) 0.0 else 100.0 * scoreObj.runs / bowls,
        onStrike = player.isBatting
    )
}