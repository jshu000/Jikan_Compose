package com.jashwant.jikan_compose

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import com.jashwant.jikan_compose.ui.theme.Jikan_ComposeTheme

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
                    Greeting(
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
fun Greeting(name: String, modifier: Modifier = Modifier,viewModel: MainViewModel) {
    var str= remember {
        viewModel.animelist.value?.let { mutableStateOf(it.size) }
    }
    Log.d(TAG, "Greeting: Recomposition happening")
    Text(
        text = "Hello ${str}!",
        modifier = modifier
    )
}

/*
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Jikan_ComposeTheme {
        Greeting("Android")
    }
}*/
