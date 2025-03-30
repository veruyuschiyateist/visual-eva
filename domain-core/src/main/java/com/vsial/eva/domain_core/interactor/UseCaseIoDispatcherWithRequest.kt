package com.vsial.eva.domain_core.interactor

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class UseCaseIoDispatcherWithRequest<Result, Request : Any> {

    protected lateinit var request: Request

    abstract suspend operator fun invoke(): Result

    suspend fun get(request: Request): Result = withContext(Dispatchers.IO) {
        this@UseCaseIoDispatcherWithRequest.request = request
        invoke()
    }
}