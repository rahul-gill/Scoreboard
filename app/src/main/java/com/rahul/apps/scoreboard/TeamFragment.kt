package com.rahul.apps.scoreboard

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import com.rahul.apps.scoreboard.databinding.FragmentTeamBinding
import com.rahul.apps.scoreboard.models.Player
import com.rahul.apps.scoreboard.models.Team
import kotlin.random.Random


class TeamFragment : Fragment() {
    private lateinit var binding: FragmentTeamBinding
    private val viewModel: ScorecardViewModel by activityViewModels()
    private val teams = listOf(Team(players = mutableListOf(Player())), Team(players = mutableListOf(Player(), Player())))
    private val adapter = TextRecyclerViewAdapter()
    private var teamIndex: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_team, container, false)
        binding.doneButton.setOnClickListener { onDone() }
        binding.teamPlayers.adapter = adapter

        binding.teamName.doOnTextChanged { text, _, _, _ ->
            teams[teamIndex].name = text.toString()
        }

        tabSetup(0)
        binding.teamTabs.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.position?.let { teamIndex -> tabSetup(teamIndex) }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}

        })

        return binding.root
    }

    private fun onDone(){
        val teamAName = teams[0].name
        val teamBName = teams[1].name
        val winner =
            if(Random(SystemClock.currentThreadTimeMillis()).nextBoolean()) teamAName
            else teamBName

        val tossDialog = AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.toss))
            .setMessage(winner + getString(R.string.won_the_toss))
            .setPositiveButton(teamAName){ _, _ ->
                viewModel.match.battingTeam = teams[0]
                viewModel.match.bowlingTeam = teams[1]
                findNavController().navigate(TeamFragmentDirections.actionTeamFragmentToScorecardFragment())
            }
            .setNegativeButton(teamBName){ _, _ ->
                viewModel.match.battingTeam = teams[1]
                viewModel.match.bowlingTeam = teams[0]
                findNavController().navigate(TeamFragmentDirections.actionTeamFragmentToScorecardFragment())
            }
            .create()
        tossDialog.show()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun tabSetup(teamIndex: Int){
        binding.teamTabs.tabMode
        this.teamIndex = teamIndex
        binding.teamName.setText(teams[teamIndex].name)
        adapter.players = teams[teamIndex].players
        adapter.notifyDataSetChanged()
        binding.newPlayerButton.setOnClickListener {
            teams[teamIndex].players.add(Player())
            Log.d("DEBUG", "onNewPlayer ${teams[teamIndex].players.map { it.name  }}")
            Log.d("DEBUG", "onNewPlayer adpater ${adapter.players.map { it.name  }}")
            adapter.notifyItemInserted(teams[teamIndex].players.size - 1)
        }
    }

}