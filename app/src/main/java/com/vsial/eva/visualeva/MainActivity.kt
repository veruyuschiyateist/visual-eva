package com.vsial.eva.visualeva

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.vsial.eva.visualeva.ui.features.CameraRoute
import com.vsial.eva.visualeva.ui.features.FiltersRoute
import com.vsial.eva.visualeva.ui.features.PhotosRoute
import com.vsial.eva.visualeva.ui.features.LocalNavController
import com.vsial.eva.visualeva.ui.features.camera.CameraScreen
import com.vsial.eva.visualeva.ui.features.filters.FiltersScreen
import com.vsial.eva.visualeva.ui.features.photos.PhotosScreen
import com.vsial.eva.visualeva.ui.theme.VisualEvaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            VisualEvaTheme {
                VisualEvaNavApp()
            }
        }
    }
}

@Composable
fun VisualEvaNavApp() {
    val navController = rememberNavController()
    CompositionLocalProvider(
        LocalNavController provides navController
    ) {
        NavHost(
            navController = navController,
            startDestination = PhotosRoute,
            modifier = Modifier.fillMaxSize()
        ) {
            composable<PhotosRoute> { PhotosScreen() }
            composable<CameraRoute> { CameraScreen() }
            composable<FiltersRoute> { entry ->
                val route: FiltersRoute = entry.toRoute()
                FiltersScreen(imageUri = route.imageUri)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    VisualEvaTheme {
        VisualEvaNavApp()
    }
}