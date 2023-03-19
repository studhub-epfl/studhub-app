package com.studhub.app.domain.usecase.user

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.User
import kotlinx.coroutines.flow.Flow

/***
 * Created to be able to preview View Models
 */
interface IGetCurrentUser {
    suspend operator fun invoke(): Flow<ApiResponse<User>>
}
