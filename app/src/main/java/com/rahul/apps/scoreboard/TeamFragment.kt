package com.rahul.apps.scoreboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.rahul.apps.scoreboard.models.Match
import com.rahul.apps.scoreboard.models.Player
import com.rahul.apps.scoreboard.models.Team
import kotlin.random.Random


class TeamFragment : Fragment() {
    private val viewModel: ScorecardViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme{
                    TeamScreen(
                        onDone = {
                            buildMatchObject(it)
                            findNavController().navigate(TeamFragmentDirections.actionTeamFragmentToScorecardFragment())
                        },
                        goBack = { findNavController().navigateUp() }
                    )
                }
            }
        }

    }

    private fun buildMatchObject(match: Match) {
        viewModel.match = match
        viewModel.match.apply {
            batsman = battingTeam.players.first()
            bowler = bowlingTeam.players.last()
            nextBatsmanInQueueIndex =
                if (2 < battingTeam.players.size) 2
                else null
            nonStrikeBatsman = battingTeam.players[1]
        }
    }
}


@Preview(showBackground = true)
@Composable
fun TeamScreen(
    onDone: (match: Match) -> Unit = {},
    goBack: () -> Unit = {}
) {
    var tabIndex by remember { mutableStateOf(0) }

    var teamAName by remember { mutableStateOf("") }
    var teamBName by remember { mutableStateOf("") }

    var teamAPlayerNames by remember { mutableStateOf(listOf("")) }
    var teamBPlayerNames by remember { mutableStateOf(listOf("")) }

    val tabTitles = listOf(
        stringResource(id = R.string.team_a_name_placeholder),
        stringResource(id = R.string.team_b_name_placeholder),
    )
    var tossDialogShowing by remember { mutableStateOf(false) }

    val context = LocalContext.current

    TossDialog(
        teamAName,
        teamBName,
        show = tossDialogShowing,
        onDismiss = { tossDialogShowing = false },
        onTossResult = { teamAIsBatting ->
            val match = Match().apply{
                battingTeam = Team(
                    name = if(teamAIsBatting) teamAName else teamBName,
                    players = (if(teamAIsBatting) teamAPlayerNames else teamBPlayerNames).map {
                        Player(name = it)
                    }.toMutableList()
                )
                bowlingTeam = Team(
                    name = if(!teamAIsBatting) teamAName else teamBName,
                    players = (if(!teamAIsBatting) teamAPlayerNames else teamBPlayerNames).map {
                        Player(name = it)
                    }.toMutableList()
                )
            }
            onDone(match)
        }
    )


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "",
                        modifier = Modifier.clickable { goBack() }
                    )
                },
                elevation = 0.dp
            )
        },

        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text(text = stringResource(id = R.string.done_editing_teams)) },
                icon = { Icon(imageVector = Icons.Default.DoneAll, contentDescription = "")  },
                onClick = {
                    when{
                        teamAName.isBlank() -> {
                            tabIndex = 0
                            //TODO string resource && text field shake effect
                            Toast.makeText(context, "Team name can't be empty", Toast.LENGTH_SHORT).show()
                        }
                        teamBName.isBlank() -> {
                            tabIndex = 1
                            Toast.makeText(context, "Team name can't be empty", Toast.LENGTH_SHORT).show()
                        }
                        teamAPlayerNames.any { it.isBlank() } -> {
                            tabIndex = 0
                            Toast.makeText(context, "Player name can't be empty", Toast.LENGTH_SHORT).show()
                        }
                        teamBPlayerNames.any { it.isBlank() } -> {
                            tabIndex = 1
                            Toast.makeText(context, "Player name can't be empty", Toast.LENGTH_SHORT).show()
                        }
                        teamAPlayerNames.size < 2 -> {
                            tabIndex = 0
                            Toast.makeText(context, "Teams must have at least two players", Toast.LENGTH_SHORT).show()
                        }
                        teamBPlayerNames.size < 2 -> {
                            tabIndex = 1
                            Toast.makeText(context, "Teams must have at least two players", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            tossDialogShowing = true
                        }
                    }
                },
            )
        }
    ) {
        Column {
            TabRow(selectedTabIndex = tabIndex) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = tabIndex == index,
                        onClick = { tabIndex = index },
                        text = {
                            Text(text = title)
                        }
                    )
                }
            }

            TextField(
                value = if(tabIndex == 0) teamAName else teamBName,
                onValueChange = {
                    if(tabIndex == 0) teamAName = it
                    else teamBName = it
                },
                label = { Text(text = stringResource(id = R.string.team_name)) },
                colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                leadingIcon = { Icon(imageVector = Icons.Default.Groups, contentDescription = "") }
            )

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(id = R.string.team_players),
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier.padding(start = 4.dp)
                )
                Card(
                    modifier = Modifier.clickable {
                        if (tabIndex == 0) teamAPlayerNames = teamAPlayerNames
                            .toMutableList()
                            .apply { add("") }
                        else teamBPlayerNames = teamBPlayerNames
                            .toMutableList()
                            .apply { add("") }
                    },
                    elevation = 0.dp,
                    shape = CircleShape
                ){

                    Icon(
                        imageVector = Icons.Default.AddCircleOutline,
                        contentDescription = "",
                        modifier = Modifier.padding(8.dp)
                    )
                }

            }

            LazyColumn{
                itemsIndexed(items =
                if(tabIndex == 0) teamAPlayerNames
                else teamBPlayerNames
                ){ playerIndex, player ->
                    PlayerNameTextField(
                        player,
                        onPlayerNameChange = { newName ->
                            if (tabIndex == 0) teamAPlayerNames = teamAPlayerNames.toMutableList().apply {
                                set(playerIndex, newName)
                            }
                            else teamBPlayerNames = teamBPlayerNames.toMutableList().apply {
                                set(playerIndex, newName)
                            }
                        },
                        onPlayerRemove = {
                            if (tabIndex == 0) teamAPlayerNames = teamAPlayerNames.toMutableList().apply {
                                removeAt(playerIndex)
                            }
                            else teamBPlayerNames = teamBPlayerNames.toMutableList().apply {
                                removeAt(playerIndex)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun PlayerNameTextField(
    player: String,
    onPlayerNameChange:  (newName: String) -> Unit,
    onPlayerRemove: () -> Unit
) {

    TextField(
        value = player,
        onValueChange = { onPlayerNameChange(it) },
        label = { Text(text = stringResource(id = R.string.player_name)) },
        leadingIcon = { Icon(imageVector = Icons.Default.Person, contentDescription = "") },
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.RemoveCircleOutline,
                contentDescription = "",
                modifier = Modifier.clickable { onPlayerRemove() }
            )
        },
        colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, end = 4.dp, top = 8.dp)
    )
}

@Composable
private fun TossDialog(
    teamAName: String,
    teamBName: String,
    show: Boolean,
    onDismiss: () -> Unit,
    onTossResult: (teamAIsBatting: Boolean) -> Unit
){
    var tossWinTeam: String
    var tossLoseTeam: String
    Random(System.currentTimeMillis()).nextBoolean().let{ teamAWonTheToss ->
        tossWinTeam = if(teamAWonTheToss) teamAName else teamBName
        tossLoseTeam = if(teamAWonTheToss) teamBName else teamAName
    }


    if (show) AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = stringResource(id = R.string.toss)) },
        text = { Text(
            //TODO: string resource
            text = buildAnnotatedString {
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)){
                    append(tossWinTeam)
                }
                append(" won the toss. Choose the team that will bat first")
            }
        ) },
        buttons = {
            Row(
                modifier = Modifier
                    .padding(all = 8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OutlinedButton(
                    onClick = {
                        onTossResult(tossLoseTeam == teamAName)
                        onDismiss()
                    },
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .weight(1f)
                ) {
                    Text(tossLoseTeam)
                }

                OutlinedButton(
                    onClick = {
                        onTossResult(tossWinTeam == teamAName)
                        onDismiss()
                    },
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .weight(1f)
                ) {
                    Text(tossWinTeam)
                }
            }
        }
    )
}