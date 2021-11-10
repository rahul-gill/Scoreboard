package com.rahul.apps.scoreboard

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.rahul.apps.scoreboard.databinding.FragmentScorecardBinding
import com.rahul.apps.scoreboard.models.BowlResult

class ScorecardFragment : Fragment() {
    private lateinit var binding: FragmentScorecardBinding
    private val viewModel: ScorecardViewModel by activityViewModels()
    private lateinit var adapter: BattingScorecardAdapter
    private  var nextQueueBatsmanIndex = 2
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_scorecard, container, false)
        adapter = BattingScorecardAdapter()
        binding.battingScorecard.adapter = adapter
        binding.teamScoreWicketsOvers.text = "0/0 after 0.0 overs"
        viewModel.match.batsman = viewModel.match.battingTeam.players.first()
        viewModel.match.bowler = viewModel.match.bowlingTeam.players.last()
        viewModel.match.nextBatsmanInQueue =
            if(nextQueueBatsmanIndex < viewModel.match.battingTeam.players.size) viewModel.match.battingTeam.players[nextQueueBatsmanIndex]
            else null
        viewModel.match.nonStrikeBatsman = viewModel.match.battingTeam.players[1]

        viewModel.match.battingTeam.players.forEach {
            adapter.data.add(playerToBattingScorecardItem(it))
        }.also { adapter.notifyDataSetChanged() }

        binding.dotBowl.setOnClickListener { viewModel.match.nextBowl(BowlResult.DOT_BOWL); sameWork() }
        binding.oneRun.setOnClickListener { viewModel.match.nextBowl(BowlResult.SINGLE_RUN); sameWork() }
        binding.twoRun.setOnClickListener {  viewModel.match.nextBowl(BowlResult.DOUBLE_RUN); sameWork() }
        binding.threeRun.setOnClickListener { viewModel.match.nextBowl(BowlResult.TRIPLE_RUN); sameWork() }
        binding.four.setOnClickListener { viewModel.match.nextBowl(BowlResult.FOUR); sameWork() }
        binding.six.setOnClickListener { viewModel.match.nextBowl(BowlResult.SIX); sameWork() }
        binding.wicket.setOnClickListener { viewModel.match.nextBowl(BowlResult.LBW_BOLD_STUMPS); sameWork() }
        binding.wide.setOnClickListener { viewModel.match.nextBowl(BowlResult.WIDE_OR_NO_BOWL); sameWork()  }
        binding.switchTeams.setOnClickListener {
            viewModel.match.switchTeams()
            viewModel.match.battingTeam.players.forEach {
                adapter.data.add(playerToBattingScorecardItem(it))
            }.also { adapter.notifyDataSetChanged() }
        }
        return binding.root
    }
    private fun sameWork(){
        val battingPlayers = viewModel.match.battingTeam.players
        for(i in battingPlayers.indices){
            adapter.data[i+1] = playerToBattingScorecardItem(battingPlayers[i])
        }
        val tswo = "${viewModel.match.battingTeam.score.runs}/${viewModel.match.battingTeam.wickets} after" +
                " ${viewModel.match.battingTeam.overs.overs}.${viewModel.match.battingTeam.overs.bowls} overs"
        binding.teamScoreWicketsOvers.text = tswo
        adapter.notifyDataSetChanged()
    }
}

