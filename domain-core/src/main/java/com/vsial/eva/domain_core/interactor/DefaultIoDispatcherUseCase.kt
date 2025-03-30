package com.vsial.eva.domain_core.interactor

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class DefaultIoDispatcherUseCase<Result> {

    abstract suspend operator fun invoke(): Result

    suspend fun get(): Result = withContext(Dispatchers.IO) {
        invoke()
    }

}