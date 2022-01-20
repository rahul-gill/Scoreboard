package com.rahul.apps.scoreboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
            return ComposeView(requireContext()).apply {
                setContent {
                    HomeScreen(onPlay = {
                        findNavController().navigate(
                            HomeFragmentDirections.actionHomeFragmentToTeamFragment()
                        )
                    })
                }
            }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreen(onPlay: () -> Unit = {}) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Icon(
            imageVector = Icons.Filled.PlayCircle,
            contentDescription = "",
            modifier = Modifier
                .clickable { onPlay() }
                .size(100.dp),
            tint = MaterialTheme.colors.primary
        )
        Text(
            text = "Play a match",
            fontSize = 20.sp,
            style = TextStyle(color = MaterialTheme.colors.onBackground)
        )
    }
}