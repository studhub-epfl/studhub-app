package com.studhub.app.presentation.ratings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Rating
import com.studhub.app.domain.model.User
import com.studhub.app.domain.usecase.user.*
import dagger.hilt.android.lifecycle.HiltViewModel
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

) : ViewModel() {

    private val _ratings = MutableStateFlow<ApiResponse<List<Rating>>>(ApiResponse.Loading)
    val ratings: StateFlow<ApiResponse<List<Rating>>> = _ratings

    private val _currentUser = MutableStateFlow<ApiResponse<User>>(ApiResponse.Loading)
    val currentUser: StateFlow<ApiResponse<User>> = _currentUser

    init {
        viewModelScope.launch {
            val currentUserResponse = getCurrentUser().first()
            if (currentUserResponse is ApiResponse.Success) {
                _currentUser.value = currentUserResponse
                val userRatingsResponse = getUserRatingsUseCase(currentUserResponse.data.id).first()
                if (userRatingsResponse is ApiResponse.Success) {
                    _ratings.value = userRatingsResponse
                }
            }
        }
    }



    fun addRating(userId: String, rating: Rating) {
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

    fun updateRating(userId: String, ratingId: String, rating: Rating) {
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

    fun deleteRating(userId: String, ratingId: String) {
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

    fun getUserRatings(userId: String) {
        viewModelScope.launch {
            _ratings.value = ApiResponse.Loading
            getUserRatingsUseCase(userId).collect { response ->
                _ratings.value = response
            }
        }
    }
}
