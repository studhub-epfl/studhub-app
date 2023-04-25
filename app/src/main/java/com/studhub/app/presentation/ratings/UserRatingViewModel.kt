package com.studhub.app.presentation.ratings

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Rating
import com.studhub.app.domain.model.User
import com.studhub.app.domain.usecase.user.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val _targetUserLoading = MutableStateFlow(true)

    private lateinit var targetUserId: String

    private val _currentUserLoading = MutableStateFlow(true)
    override val currentUserLoading: StateFlow<Boolean> = _currentUserLoading.asStateFlow()


    override suspend fun getUserById(id: String): ApiResponse<User> {
        println("Attempting to fetch user with id: $id")
        val response = getUser(id).first()
        println("Fetched user response: $response")
        return response
    }

    override suspend fun initTargetUser(targetUserId: String) {
        this.targetUserId = targetUserId
        viewModelScope.launch {
            getCurrentUser().collect { currentUserResponse ->
                when (currentUserResponse) {
                    is ApiResponse.Loading -> {
                        _currentUserLoading.value = true
                    }
                    is ApiResponse.Failure -> {
                        _currentUserLoading.value = false
                        _currentUser.value = ApiResponse.Failure(currentUserResponse.message)
                    }
                    is ApiResponse.Success -> {
                        Log.d("UserRatingViewModel","Helllooo")
                        _currentUserLoading.value = false
                        _currentUser.value = currentUserResponse
                    }
                }
            }

            getUserRatingsUseCase(targetUserId).collect { userRatingsResponse ->
                Log.d("ViewModel", "User ratings response: $userRatingsResponse")
                when (userRatingsResponse) {
                    is ApiResponse.Loading -> _ratings.value = ApiResponse.Loading
                    is ApiResponse.Failure -> _ratings.value =
                        ApiResponse.Failure(userRatingsResponse.message)
                    is ApiResponse.Success -> {
                        _ratings.value = userRatingsResponse
                    }
                }
            }
            getUser(targetUserId).collect { targetUserResponse ->
                when (targetUserResponse) {
                    is ApiResponse.Loading -> {
                        _targetUserLoading.value = true
                    }
                    is ApiResponse.Failure -> {
                        _targetUserLoading.value = false
                        _targetUser.value = ApiResponse.Failure(targetUserResponse.message)
                    }
                    is ApiResponse.Success -> {
                        _targetUserLoading.value = false
                        _targetUser.value = targetUserResponse
                    }
                }
            }
        }
    }


    override fun addRating(userId: String, rating: Rating) {
        Log.d(TAG, "addRating called with rating: $rating, comment: ${rating.comment}")
        viewModelScope.launch {
            val currentUserResponse = currentUser.value
            if (currentUserResponse is ApiResponse.Success) {
                addRatingUseCase(
                    userId,
                    rating.copy(reviewerId = currentUserResponse.data.id)
                ).collect { response ->
                    when (response) {
                        is ApiResponse.Success -> {
                            getUserRatings(userId)
                            Log.d(TAG, "Rating added successfully. Fetching updated ratings...")
                        }
                        is ApiResponse.Failure -> {
                            _ratings.value = ApiResponse.Failure(response.message)
                            Log.e(TAG, "Error adding rating: $response")}
                        is ApiResponse.Loading -> _ratings.value = ApiResponse.Loading
                    }
                }
            } else {
                _ratings.value = ApiResponse.Failure("Error: Current user not found")
            }
        }
    }


    override fun updateRating(userId: String, ratingId: String, rating: Rating) {
        Log.d("UserRatingViewModel", "Updating rating")
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
            when (val targetUserResponse = targetUser.value) {
                is ApiResponse.Loading -> _ratings.value = ApiResponse.Loading
                is ApiResponse.Failure -> _ratings.value =
                    ApiResponse.Failure(targetUserResponse.message)
                is ApiResponse.Success -> {
                    _ratings.value = ApiResponse.Loading
                    getUserRatingsUseCase(userId).collect { response ->
                        when (response) {
                            is ApiResponse.Success -> {
                                _ratings.value =
                                    ApiResponse.Success(response.data.sortedByDescending { it.timestamp })
                                Log.d("UserRatingViewModel", "Ratings updated: ${response.data}")
                            }
                            is ApiResponse.Failure -> _ratings.value =
                                ApiResponse.Failure(response.message)
                            is ApiResponse.Loading -> _ratings.value = ApiResponse.Loading
                        }
                    }
                }
            }
        }
    }


}
