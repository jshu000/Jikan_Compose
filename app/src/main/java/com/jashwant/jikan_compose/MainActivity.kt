@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)

package com.jashwant.jikan_compose

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.jashwant.jikan_compose.models.Data
import com.jashwant.jikan_compose.ui.theme.Jikan_ComposeTheme

const val TAG="JashwantJikan"
class MainActivity : ComponentActivity() {
    lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val apiService= RetrofitBuilder.apiService
        val animeListDao = AnimeDatabase.getDatabase(applicationContext).animeListDao()
        val repository = AnimeListRepository(apiService,animeListDao)
        viewModel = ViewModelProvider(this,MainViewModelFactory(repository)).get(MainViewModel::class.java)
        Log.d(TAG, "onCreate: ${viewModel.animelist.value}")
        setContent {
            Jikan_ComposeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AnimeListScreen(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding),
                        viewModel=viewModel
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: ${viewModel.animelist.value.toString()}")
    }
}

@Composable
fun AnimeListScreen(name: String, modifier: Modifier = Modifier,viewModel: MainViewModel) {
    val state by viewModel.state.collectAsState()
    val animelistflows by viewModel.animelistflow.collectAsState()
    Log.d(TAG, "Greeting: UI recomposition happening")

    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = {
                Text("AnimeList")
            }, actions = {
                IconButton(onClick = { viewModel.fetchAll() }) {
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
                LazyColumn(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                ) {

                    (state as? UiState.Success)?.let {
                        Log.d(TAG, "Greeting: ${it.response}")
                        items(animelistflows) { item ->
                            ListItem(item)
                        }
                    }
                }
        }

    }
}

@Composable
fun ListItem(item: Data) {

    Card(onClick = { /*TODO*/ },
        modifier = Modifier.padding(15.dp)
            .fillMaxWidth()) {
        Row {
            GlideImage(model = item.images.jpg.image_url, contentDescription = "s", modifier = Modifier.wrapContentSize())
            Column(modifier = Modifier.padding(10.dp)) {
                Text(text = item.title, fontSize = 25.sp, style = TextStyle(fontWeight = Bold), maxLines = 1)
                Text(text = item.episodes.toString())
                Text(text = item.rating)
            }
        }

    }
}
