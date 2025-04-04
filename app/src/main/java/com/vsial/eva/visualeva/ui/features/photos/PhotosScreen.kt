@file:OptIn(ExperimentalPermissionsApi::class)

package com.vsial.eva.visualeva.ui.features.photos

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.vsial.eva.domain_core.Result
import com.vsial.eva.domain_photos.entities.CameraPhoto
import com.vsial.eva.visualeva.ui.components.PillIconBar
import com.vsial.eva.visualeva.ui.features.CameraRoute
import com.vsial.eva.visualeva.ui.features.FiltersRoute
import com.vsial.eva.visualeva.ui.features.LocalNavController

@Composable
fun PhotosScreen() {
    val navController = LocalNavController.current
    val viewModel = hiltViewModel<PhotosViewModel>()
    val photosState by viewModel.photosState.collectAsStateWithLifecycle()

    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    var requestedCamera by remember { mutableStateOf(false) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            navController.navigate(FiltersRoute(it.toString()))
        }
    }

    LaunchedEffect(cameraPermissionState.status.isGranted, requestedCamera) {
        if (cameraPermissionState.status.isGranted && requestedCamera) {
            navController.navigate(CameraRoute)
            requestedCamera = false
        }
    }

    PhotosContent(
        isLoading = photosState.isLoading,
        imagesList = { photosState.images },
        errorMessage = photosState.errorMessage,
        onImageClicked = { imageUri ->
            navController.navigate(FiltersRoute(imageUri))
        },
        onCameraClicked = {
            if (cameraPermissionState.status.isGranted) {
                navController.navigate(CameraRoute)
            } else {
                requestedCamera = true
                cameraPermissionState.launchPermissionRequest()
            }
        },
        onGalleryClicked = {
            galleryLauncher.launch("image/*")
        }
    )
}

@Composable
fun PhotosContent(
    isLoading: Boolean = true,
    errorMessage: String? = null,
    imagesList: () -> List<CameraPhoto>,
    onImageClicked: (String) -> Unit,
    onCameraClicked: () -> Unit,
    onGalleryClicked: () -> Unit
) {
    var selectedTab by remember { mutableStateOf("Recents") }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(horizontal = 30.dp, vertical = 44.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            Text(
                text = "Media",
                color = Color.White,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF1A1A1A))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                listOf("Recents", "Cloud").forEach { tab ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (selectedTab == tab) Color(0xFF2D2D2D) else Color.Transparent)
                            .clickable { selectedTab = tab }
                            .padding(horizontal = 20.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = tab,
                            color = if (selectedTab == tab) Color.White else Color.Gray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (isLoading) {
                CircularProgressIndicator()
            } else {
                SelectableImageGrid(
                    items = imagesList.invoke(),
                    onItemClick = { onImageClicked.invoke(it) }
                )
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)
        ) {
            PillIconBar(
                onPhotoCameraClick = {
                    onCameraClicked.invoke()
                },
                onGalleryClick = {
                    onGalleryClicked.invoke()
                }
            )
        }
    }
}

@Composable
fun SelectableImageGrid(
    items: List<CameraPhoto>,
    onItemClick: (String) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items) { itemImage ->
            ImageGridItem(
                item = itemImage,
                onClick = { onItemClick(itemImage.uri) }
            )
        }
    }
}

@Composable
fun ImageGridItem(
    item: CameraPhoto,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.DarkGray)
            .clickable { onClick.invoke() }
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(item.uri)
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

val PhotosUiState.images: List<CameraPhoto>
    get() = when (this) {
        is PhotosUiState.Success -> data
        is PhotosUiState.Empty -> emptyList()
        is PhotosUiState.Error -> emptyList()
        PhotosUiState.Loading -> emptyList()
    }

val PhotosUiState.isLoading: Boolean
    get() = this is PhotosUiState.Loading

val PhotosUiState.errorMessage: String?
    get() = if (this is PhotosUiState.Error) message else null

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun GalleryPreview() {
    PhotosContent(
        imagesList = { emptyList() },
        onImageClicked = {},
        onCameraClicked = {},
        onGalleryClicked = {}
    )
}