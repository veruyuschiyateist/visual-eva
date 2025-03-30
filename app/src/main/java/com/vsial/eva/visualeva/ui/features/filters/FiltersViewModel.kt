@file:Suppress("MISSING_DEPENDENCY_CLASS_IN_EXPRESSION_TYPE")

package com.vsial.eva.visualeva.ui.features.filters

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vsial.eva.domain_core.Result
import com.vsial.eva.domain_photos.entities.ShareImageRequest
import com.vsial.eva.domain_photos.interactors.ShareFilteredImageUseCase
import com.vsial.eva.visualeva.ui.mappers.UiImageFilterType
import com.vsial.eva.visualeva.ui.models.toDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FiltersViewModel @Inject constructor(
    private val shareFilteredImageUseCase: ShareFilteredImageUseCase
) : ViewModel() {

    private val _shareUri = MutableSharedFlow<String>(replay = 0)
    val shareUri = _shareUri.asSharedFlow()

    fun share(imageUri: String, filter: UiImageFilterType) {
        viewModelScope.launch {
            when (val result = shareFilteredImageUseCase.get(
                ShareImageRequest(imageUri = imageUri.toString(), filter = filter.toDomain())
            )) {
                is Result.Success<String> -> {
                    _shareUri.emit(result.data)
                }

                is Result.Error -> {
                    Log.d("TAG", "error: ${result.error.cause}")
                }
            }
        }
    }

}
