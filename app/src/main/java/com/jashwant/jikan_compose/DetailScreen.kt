@file:OptIn(ExperimentalMaterial3Api::class)

package com.jashwant.jikan_compose

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

@Composable
fun DetailScreen(modifier: Modifier,  viewModel: MainViewModel) {
    val state by viewModel.detailstate.collectAsState()
    //Text(text = viewModel.currentdata!!.title)
    Scaffold(modifier = Modifier
        .fillMaxSize()
        .padding(20.dp),
        topBar = {
            TopAppBar(title = {
                Text(text=viewModel.currentdata!!.title)
            }, actions = {
                /*IconButton(onClick = {  }) {
                    Icon(
                        Icons.Filled.Refresh,
                        contentDescription = null
                    )
                }*/
            })
        }) { paddingValues ->
        when (state) {
            is DetailUiState.Failed -> Box(
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

            is DetailUiState.Loading -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

            is DetailUiState.Success ->
                Column(modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())

                ){
                    VideoPlayerScreen(viewModel)
                    ExoPlayerView(viewModel = viewModel)
                    val genreString = viewModel.currentdata!!.genres.joinToString(", ") { it.name }
                    Text(text ="Genre-"+genreString)
                    val episodes="Episodes - ${viewModel.currentdata!!.episodes}"
                    Text(text = episodes)
                    val ratings="Ratings - ${viewModel.currentdata!!.rating}"
                    Text(text = ratings)
                    Text(text = "Synopsis")
                    Text(text = viewModel.currentdata!!.synopsis)

                }
        }
    }
}
@Composable
fun VideoPlayerScreen(viewModel: MainViewModel) {
    val context = LocalContext.current
    var playWhenReady by remember { mutableStateOf(true) }
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            val url= viewModel.currentdata?.trailer?.url
            setMediaItem(MediaItem.fromUri("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"))
            repeatMode = ExoPlayer.REPEAT_MODE_ALL
            playWhenReady = playWhenReady
            prepare()
            play()
        }
    }
    DisposableEffect(exoPlayer) {
        onDispose {
            exoPlayer.release()
        }
    }

    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        factory = {
            PlayerView(context).apply {
                player = exoPlayer
                useController = true
                layoutParams = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
        }
    )

    LaunchedEffect(playWhenReady) {
        exoPlayer.playWhenReady = playWhenReady
    }
}

@Composable
fun ExoPlayerView(viewModel: MainViewModel) {
    // Get the current context
    val context = LocalContext.current

    // Initialize ExoPlayer
    val exoPlayer = ExoPlayer.Builder(context).build()

    // Create a MediaSource
    val mediaSource = remember(viewModel.currentdata?.trailer?.url) {
        MediaItem.fromUri(viewModel.currentdata?.trailer?.url!!)
    }

    // Set MediaSource to ExoPlayer
    LaunchedEffect(mediaSource) {
        exoPlayer.setMediaItem(mediaSource)
        exoPlayer.prepare()
    }

    // Manage lifecycle events
    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    // Use AndroidView to embed an Android View (PlayerView) into Compose
    AndroidView(
        factory = { ctx ->
            PlayerView(ctx).apply {
                player = exoPlayer
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp) // Set your desired height
    )
}