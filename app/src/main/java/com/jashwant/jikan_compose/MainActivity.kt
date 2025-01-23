@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)

package com.jashwant.jikan_compose

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
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
                App(viewModel)
            }
        }
    }
}
@Composable
fun App(viewModel: MainViewModel){
    Log.d(TAG, "App: Recomposition Happening")
    var currentPage = rememberSaveable {mutableStateOf(Pages.LISTING)}
    if(currentPage.value==Pages.LISTING || viewModel.currentdata ==null) {
        Log.d(TAG, "App: Listing")
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            AnimeListScreen(
                modifier = Modifier.padding(innerPadding),
                viewModel = viewModel,
                onClick = {data: Data ->
                    viewModel.currentdata=data
                    Log.d(TAG, "App: data result-$data")
                    currentPage.value=Pages.DETAIL
                }
            )
        }
    }
    else{
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Log.d(TAG, "App: DetailScreen")
            DetailScreen(
                modifier = Modifier.padding(innerPadding),
                viewModel = viewModel
            )
        }
    }
}


enum class Pages{
    LISTING,
    DETAIL
}
