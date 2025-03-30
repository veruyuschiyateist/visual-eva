@file:OptIn(ExperimentalMaterial3Api::class)

package com.vsial.eva.visualeva.ui.features.filters

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.imageLoader
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.util.DebugLogger
import com.vsial.eva.visualeva.R

@Composable
fun FiltersScreen(uri: String = "https://picsum.photos/800") {
    FiltersContent(imageUri = uri, onClose = {}, onConfirm = {})
}

@Composable
fun FiltersContent(
    imageUri: String,
    onConfirm: () -> Unit,
    onClose: () -> Unit
) {
    var selectedFilter by remember { mutableStateOf(ImageFilter.None) }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(vertical = 44.dp)
    ) {
        FilteredImage(
            uri = imageUri, selectedFilter = selectedFilter
        )

        FiltersSelectionBar(
            selectedFilter = selectedFilter,
            onSelect = { selectedFilter = it },
            imageUri = imageUri,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        )

        CenteredTopBar(
            title = stringResource(R.string.filter),
            onClose = { onClose.invoke() },
            onConfirm = { onConfirm.invoke() })
    }
}

@Composable
fun CenteredTopBar(
    title: String,
    onClose: () -> Unit,
    onConfirm: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(Color.Transparent),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title, color = Color.White, style = MaterialTheme.typography.titleMedium
        )

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onClose) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.close),
                    tint = Color.White
                )
            }

            IconButton(onClick = onConfirm) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = stringResource(R.string.confirm),
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun FilteredImage(
    uri: String, selectedFilter: ImageFilter
) {
    val context = LocalContext.current
    val imagePainter = remember(uri) {
        ImageRequest.Builder(context).data(uri).crossfade(true).build()
    }

    val matrixColorFilter = selectedFilter.matrix?.let { ColorFilter.colorMatrix(it) }

    AsyncImage(
        model = imagePainter,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize(),
        colorFilter = matrixColorFilter
    )
}

@Composable
fun FiltersSelectionBar(
    selectedFilter: ImageFilter,
    onSelect: (ImageFilter) -> Unit,
    imageUri: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val imageModel = remember(imageUri) {
        ImageRequest.Builder(context).data(imageUri).build()
    }

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .height(144.dp)
            .background(brush = Brush.verticalGradient(listOf(Color.Black, Color(0xAA000000))))
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .then(modifier),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(ImageFilter.entries.toTypedArray()) { filter ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .border(
                            width = if (selectedFilter == filter) 2.dp else 0.dp,
                            color = Color.White,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable { onSelect(filter) }) {
                    AsyncImage(
                        model = imageModel,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                        colorFilter = filter.matrix?.let { ColorFilter.colorMatrix(it) })
                }
                Text(
                    text = filter.label,
                    color = Color.White,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FiltersPreview() {
    FiltersContent(imageUri = "https://picsum.photos/800", onConfirm = {}, onClose = {})
}