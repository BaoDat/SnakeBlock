package com.datdang.snakeblock.data.util

import com.datdang.snakeblock.domain.util.Result

suspend fun <T> safeApiCall(apiCall: suspend () -> T): Result<T> = try {
    Result.Success(apiCall.invoke())
} catch (e: Exception) {
    Result.Error(e)
}
