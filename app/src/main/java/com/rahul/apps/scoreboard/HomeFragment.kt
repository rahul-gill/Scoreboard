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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
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
                    MaterialTheme {
                        Scaffold(
                            topBar = {
                                TopAppBar(
                                    title = { Text(text = stringResource(id = R.string.app_name)) }
                                )
                            }
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                Card(
                                    elevation = 0.dp,
                                    shape = CircleShape
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.PlayCircle,
                                        contentDescription = "",
                                        modifier = Modifier
                                            .clickable {
                                                findNavController().navigate(
                                                    HomeFragmentDirections.actionHomeFragmentToTeamFragment()
                                                )
                                            }
                                            .size(100.dp),
                                        tint = MaterialTheme.colors.primary,
                                    )
                                }
                                Text(
                                    text = "Play a match",
                                    fontSize = 20.sp,
                                    style = TextStyle(color = MaterialTheme.colors.onBackground)
                                )
                            }
                        }
                    }
                }
            }
    }
}