package com.studhub.app.domain.usecase.user

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
/***
 * Created to be able to preview View Models
 */
class FakeGetCurrentUser : IGetCurrentUser {
    override suspend fun invoke(): Flow<ApiResponse<User>> {
        return flow {
            emit(ApiResponse.Success(User(firstName = "John", lastName = "Doe")))
        }
    }
}

