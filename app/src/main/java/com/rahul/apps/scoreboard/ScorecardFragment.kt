package com.rahul.apps.scoreboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.rahul.apps.scoreboard.models.*
import com.rahul.apps.scoreboard.util.customMutableStateOf
import com.rahul.apps.scoreboard.util.updateState

class ScorecardFragment : Fragment() {
    private val viewModel: ScorecardViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                ScoreCardScreen(viewModel.match)
            }
        }
    }
}




@Composable
fun ScoreCardScreen(matchArg: Match) = MaterialTheme{
    val match = customMutableStateOf(matchArg)

    Box(Modifier.fillMaxSize()) {

        Column(Modifier.fillMaxWidth()) {
            Text(
                text = match.value.battingTeam.run {
                    "${score.runs}/${wickets} after ${overs.overs}.${overs.bowls} overs"
                },
                modifier = Modifier.padding(start = 8.dp)
            )
            LazyColumn{
                item {
                    PlayerBattingScorecardHeader()
                }
                items(match.value.battingTeam.players){ player ->
                    PlayerBattingScorecard(player)
                }
            }
        }

        Column(
            Modifier
            .align(Alignment.BottomCenter)
            .padding(16.dp)
        ) {
            Row(horizontalArrangement = Arrangement.End){
                OutlinedButton(
                    onClick = {
                        match.updateState {
                            switchTeams()
                        }
                    },
                    content = { Text(text = "BATTING DONE") }
                )
                OutlinedButton(
                    onClick = {
                        match.updateState {
                            nextBowl(BowlResult.SIX)
                        }
                    },
                    content = { Text(text = "6") }
                )
            }

            Row(horizontalArrangement = Arrangement.End){
                OutlinedButton(
                    onClick = {
                        match.updateState {
                            nextBowl(BowlResult.LBW_BOLD_STUMPS)
                        }
                    },
                    content = { Text(text = "OUT") }
                )
                OutlinedButton(
                    onClick = {
                        match.updateState {
                            nextBowl(BowlResult.WIDE_OR_NO_BOWL)
                        }
                    },
                    content = { Text(text = "WIDE/NOBOWL") }
                )
                OutlinedButton(
                    onClick = {
                        match.updateState {
                            nextBowl(BowlResult.DOT_BOWL)
                        }
                    },
                    content = { Text(text = "DOT") }
                )
            }

            Row(Modifier.fillMaxWidth()) {
                OutlinedButton(
                    onClick = {
                        match.updateState {
                            nextBowl(BowlResult.SINGLE_RUN)
                        }
                    },
                    content = { Text(text = "1") }
                )
                OutlinedButton(
                    onClick = {
                        match.updateState {
                            nextBowl(BowlResult.DOUBLE_RUN)
                        }
                    },
                    content = { Text(text = "2") }
                )
                OutlinedButton(
                    onClick = {
                        match.updateState {
                            nextBowl(BowlResult.TRIPLE_RUN)
                        }
                    },
                    content = { Text(text = "3") }
                )
                OutlinedButton(
                    onClick = {
                        match.updateState {
                            nextBowl(BowlResult.FOUR)
                        }
                    },
                    content = { Text(text = "4") }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BattingScoreCardPreview(){
    Column{
        PlayerBattingScorecardHeader()
        for(i in 0 until 5){
            PlayerBattingScorecard(
                Player(
                    "Dhoni",
                    BattingRecord(
                        overs = Overs(5, 3),
                        score = Score(runs = 10, sixes = 1, singleRuns = 1, tripleRuns = 1, dotBowls = 4),
                    ),
                    isBatting = i == 2,
                    isOut = i == 0,
                )
            )
        }
    }
}

@Composable
fun PlayerBattingScorecard(
    player: Player
) = with(player){
    Card {

        Column(modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 4.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = name + if (isBatting) "*" else "",
                    textAlign = TextAlign.Start,
                    modifier = Modifier.weight(3f)
                )
                Text(
                    text = "${battingRecord.score.runs}(${battingRecord.overs.totalBowls})",
                    textAlign = TextAlign.Start,
                    modifier = Modifier.weight(2f)
                )
                Text(
                    text = battingRecord.score.sixes.toString(),
                    textAlign = TextAlign.Start,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = battingRecord.score.fours.toString(),
                    textAlign = TextAlign.Start,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = strikeRate,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.weight(2f)
                )
            }
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(fontStyle = FontStyle.Italic)) {
                        append(if (isOut) "out" else "not out")
                    }
                },
                style = MaterialTheme.typography.caption,
                modifier = Modifier.padding(start = 32.dp, top = 2.dp)
            )
        }
    }
}

@Composable
fun PlayerBattingScorecardHeader(){
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp)
    ) {
        Text(
            text = "Batsman",
            textAlign = TextAlign.Start,
            modifier = Modifier.weight(3f)
        )
        Text(
            text = "R(B)",
            textAlign = TextAlign.Start,
            modifier = Modifier.weight(2f)
        )
        Text(
            text = "6",
            textAlign = TextAlign.Start,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "4",
            textAlign = TextAlign.Start,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "SR",
            textAlign = TextAlign.Start,
            modifier = Modifier.weight(2f)
        )
    }
}