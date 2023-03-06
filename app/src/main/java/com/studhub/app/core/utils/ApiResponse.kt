package com.studhub.app.core.utils

/**
 * Models an API query response/state.
 * Inspired from https://gist.github.com/VladBytsyuk/9fceb4761b2f690ddcceb13160d02567
 */
sealed class ApiResponse<out T> {
    data class Success<T>(val data: T): ApiResponse<T>()
    data class Failure(val message: String): ApiResponse<Nothing>()
    object Loading: ApiResponse<Nothing>()
}