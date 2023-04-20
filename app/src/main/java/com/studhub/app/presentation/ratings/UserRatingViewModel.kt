package com.studhub.app.presentation.ratings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Rating
import com.studhub.app.domain.model.User
import com.studhub.app.domain.usecase.user.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserRatingViewModel @Inject constructor(
    private val addRatingUseCase: AddRating,
    private val updateRatingUseCase: UpdateRating,
    private val deleteRatingUseCase: DeleteRating,
    private val getUserRatingsUseCase: GetUserRatings,
    val getUser: GetUser,
    private val getCurrentUser: GetCurrentUser,

    ) : ViewModel(), IUserRatingViewModel {

    private val _ratings = MutableStateFlow<ApiResponse<List<Rating>>>(ApiResponse.Loading)
    override val ratings: StateFlow<ApiResponse<List<Rating>>> = _ratings

    private val _currentUser = MutableStateFlow<ApiResponse<User>>(ApiResponse.Loading)
    override val currentUser: StateFlow<ApiResponse<User>> = _currentUser

    private val _targetUser = MutableStateFlow<ApiResponse<User>>(ApiResponse.Loading)
    override val targetUser: StateFlow<ApiResponse<User>> = _targetUser

    private lateinit var targetUserId: String

    override suspend fun getUserById(id: String): ApiResponse<User> {
        println("Attempting to fetch user with id: $id")
        val response = getUser(id).first()
        println("Fetched user response: $response")
        return response
    }

    override fun initTargetUser(targetUserId: String) {
        this.targetUserId = targetUserId
        viewModelScope.launch {
            getCurrentUser().collect { currentUserResponse ->
                when (currentUserResponse) {
                    is ApiResponse.Loading -> {}
                    is ApiResponse.Failure -> {
                        _currentUser.value = ApiResponse.Failure(currentUserResponse.message)
                    }
                    is ApiResponse.Success -> {
                        _currentUser.value = currentUserResponse
                    }
                }
            }

            getUser(targetUserId).collect { targetUserResponse ->
                when (targetUserResponse) {
                    is ApiResponse.Loading -> {}
                    is ApiResponse.Failure -> {
                        _targetUser.value = ApiResponse.Failure(targetUserResponse.message)
                    }
                    is ApiResponse.Success -> {
                        _targetUser.value = targetUserResponse
                    }
                }
            }

            getUserRatingsUseCase(targetUserId).collect { userRatingsResponse ->
                when (userRatingsResponse) {
                    is ApiResponse.Loading -> _ratings.value = ApiResponse.Loading
                    is ApiResponse.Failure -> _ratings.value = ApiResponse.Failure(userRatingsResponse.message)
                    is ApiResponse.Success -> {
                        _ratings.value = userRatingsResponse
                    }
                }
            }
        }
    }







    override fun addRating(userId: String, rating: Rating) {
        viewModelScope.launch {
            addRatingUseCase(userId, rating).collect { response ->
                when (response) {
                    is ApiResponse.Success -> getUserRatings(userId)
                    is ApiResponse.Failure -> _ratings.value = ApiResponse.Failure(response.message)
                    is ApiResponse.Loading -> _ratings.value = ApiResponse.Loading
                }
            }
        }
    }

    override fun updateRating(userId: String, ratingId: String, rating: Rating) {
        viewModelScope.launch {
            updateRatingUseCase(userId, ratingId, rating).collect { response ->
                when (response) {
                    is ApiResponse.Success -> getUserRatings(userId)
                    is ApiResponse.Failure -> _ratings.value = ApiResponse.Failure(response.message)
                    is ApiResponse.Loading -> _ratings.value = ApiResponse.Loading
                }
            }
        }
    }

    override fun deleteRating(userId: String, ratingId: String) {
        viewModelScope.launch {
            deleteRatingUseCase(userId, ratingId).collect { response ->
                when (response) {
                    is ApiResponse.Success -> getUserRatings(userId)
                    is ApiResponse.Failure -> _ratings.value = ApiResponse.Failure(response.message)
                    is ApiResponse.Loading -> _ratings.value = ApiResponse.Loading
                }
            }
        }
    }

    override fun getUserRatings(userId: String) {
        viewModelScope.launch {
            _ratings.value = ApiResponse.Loading
            getUserRatingsUseCase(userId).collect { response ->
                _ratings.value = response
            }
        }
    }
}
