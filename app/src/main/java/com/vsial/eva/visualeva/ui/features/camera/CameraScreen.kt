@file:OptIn(ExperimentalPermissionsApi::class)

package com.vsial.eva.visualeva.ui.features.camera

import androidx.camera.core.CameraSelector
import androidx.camera.view.CameraController.IMAGE_ANALYSIS
import androidx.camera.view.CameraController.IMAGE_CAPTURE
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.ImageSearch
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.vsial.eva.visualeva.ui.features.CameraRoute
import com.vsial.eva.visualeva.ui.features.PhotosRoute
import com.vsial.eva.visualeva.ui.features.LocalNavController

@Composable
fun CameraScreen() {

    val viewModel = hiltViewModel<CameraViewModel>()

    val navController = LocalNavController.current

    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)

    LaunchedEffect(Unit) {
        cameraPermissionState.launchPermissionRequest()
    }

    when {
        cameraPermissionState.status.isGranted -> {
            CameraContent(
                onPhotosClicked = {
                    navController.popBackStack()
                },
                onToggleCamera = viewModel::toggleCamera,
                onCapturePhoto = viewModel::capturePhoto
            )
        }

        !cameraPermissionState.status.shouldShowRationale && !cameraPermissionState.status.isGranted -> {
            LaunchedEffect(Unit) {
                navController.navigate(PhotosRoute) {
                    popUpTo(CameraRoute) { inclusive = true }
                }
            }
        }


        else -> {
            // Waiting for permission - Loading
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraContent(
    onPhotosClicked: () -> Unit, onToggleCamera: () -> Unit, onCapturePhoto: () -> Unit
) {
    val context = LocalContext.current

    val cameraController = rememberCameraController(context)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .padding(top = 88.dp)
        ) {

            CameraPreview(cameraController = cameraController)

            val filters = listOf("None", "Mono", "Sepia", "Cool", "Warm", "Vintage", "Blur", "Glow")
            val (selectedFilter, setSelectedFilter) = remember { mutableStateOf("None") }

            LazyRow(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                items(filters) { filterName ->
                    FilterChip(
                        selected = selectedFilter == filterName,
                        onClick = { setSelectedFilter(filterName) },
                        label = { Text(filterName, color = Color.Black) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color.Black.copy(alpha = 0.2f),
                            selectedLabelColor = Color.Black,
                            labelColor = Color.Black
                        )
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .height(88.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterHorizontally)
        ) {
            IconButton(
                onClick = { onPhotosClicked.invoke() }) {
                Icon(
                    imageVector = Icons.Default.ImageSearch,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(48.dp)
                )
            }
            IconButton(
                onClick = { onCapturePhoto.invoke() }) {
                Icon(
                    imageVector = Icons.Default.Camera,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(48.dp)
                )
            }
            IconButton(
                onClick = {
                    onToggleCamera.invoke()
                }) {
                Icon(
                    imageVector = Icons.Default.Cameraswitch,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(48.dp)
                )
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable()
fun CameraContentPreview() {
    CameraContent(onPhotosClicked = {}, onToggleCamera = {}, onCapturePhoto = {})
}