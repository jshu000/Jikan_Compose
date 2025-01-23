@file:OptIn(ExperimentalMaterial3Api::class)

package com.jashwant.jikan_compose

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jashwant.jikan_compose.models.Data

@Composable
fun DetailScreen(modifier: Modifier,  viewModel: MainViewModel) {
    val state by viewModel.detailstate.collectAsState()
    Text(text = viewModel.currentdata!!.title)
    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = {
                Text("AnimeItem")
            }, actions = {
                IconButton(onClick = {  }) {
                    Icon(
                        Icons.Filled.Refresh,
                        contentDescription = null
                    )
                }
            })
        }) { paddingValues ->
        when (state) {
            is UiState.Failed -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column {
                    Text(text = "Something went wrong :(")
                    Spacer(modifier = Modifier.padding(4.dp))
                    Button(onClick = { viewModel.fetchAll() }) {
                        Text("Try again")
                    }
                }
            }

            is UiState.Loading -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

            is UiState.Success ->
                Column(modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()){
                    Text(text = viewModel.currentdata!!.title)

                }

        }

    }

}
