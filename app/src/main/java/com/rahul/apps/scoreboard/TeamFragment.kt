package com.rahul.apps.scoreboard

import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.rahul.apps.scoreboard.databinding.FragmentTeamBinding
import com.rahul.apps.scoreboard.models.Player
import kotlin.random.Random


class TeamFragment : Fragment() {
    private lateinit var binding: FragmentTeamBinding
    private val viewModel: ScorecardViewModel by activityViewModels()
    private val adapterA = TextRecyclerViewAdapter()
    private val adapterB = TextRecyclerViewAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_team, container, false)
        binding.teamAPlayers.adapter = adapterA
        binding.teamBPlayers.adapter = adapterB


        binding.teamAAddPlayer.setOnClickListener {
            adapterA.data.add(getString(R.string.player_name_placeholder))
            adapterA.notifyItemChanged(adapterA.data.size - 1)
        }
        binding.teamBAddPlayer.setOnClickListener {
            adapterB.data.add(resources.getString(R.string.player_name_placeholder))
            adapterB.notifyItemChanged(adapterB.data.size - 1)
        }

        binding.doneButton.setOnClickListener {
            val teamAName = binding.teamAName.text.toString()
            val teamBName = binding.teamBName.text.toString()
            val winner =
                if(Random(SystemClock.currentThreadTimeMillis()).nextBoolean()) teamAName
                else teamBName

            val tossDialog = AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.toss))
                .setMessage(winner + getString(R.string.won_the_toss))
                .setPositiveButton(teamAName){ _, _ ->
                    setupTeams(0)
                    findNavController().navigate(TeamFragmentDirections.actionTeamFragmentToScorecardFragment())
                }
                .setNegativeButton(teamBName){ _, _ ->
                    setupTeams(1)
                    findNavController().navigate(TeamFragmentDirections.actionTeamFragmentToScorecardFragment())
                }
                .create()
            tossDialog.show()
        }
        return binding.root
    }

    private fun setupTeams(battingTeam: Int){
        val battingTeamPlayers = viewModel.match.battingTeam.players
        val bowlingTeamPlayers = viewModel.match.bowlingTeam.players
        adapterA.notifyDataSetChanged()
        adapterB.notifyDataSetChanged()
        if(battingTeam == 0) {
            adapterA.data.forEach{
                battingTeamPlayers.add(Player(name = it))
            }
            adapterB.data.forEach{
                bowlingTeamPlayers.add(Player(name = it))
            }
        }else{
            adapterB.data.forEach{
                battingTeamPlayers.add(Player(name = it))
            }
            adapterA.data.forEach{
                bowlingTeamPlayers.add(Player(name = it))
            }
        }
        Log.i("debug", "${viewModel.match.battingTeam.players}\n ${viewModel.match.bowlingTeam.players}")

    }
}