package com.studhub.app.presentation.home

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Category
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.model.MeetingPoint
import com.studhub.app.domain.model.User
import com.studhub.app.domain.usecase.listing.CreateCategory
import com.studhub.app.domain.usecase.user.GetCurrentUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor (
    private val getCurrentUser: GetCurrentUser,
    private val _createCategory: CreateCategory,
) : ViewModel() {
    private val _currUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currUser

    init {
        getLoggedInUser()
    }

    private fun getLoggedInUser() {
        viewModelScope.launch {
            getCurrentUser().collect {
                when (it) {
                    is ApiResponse.Loading -> {}
                    is ApiResponse.Failure -> {
                        /** HANDLE ERROR */
                    }
                    is ApiResponse.Success -> {
                        _currUser.value = it.data
                    }
                }
            }
        }
    }

    fun createCategory(
        title: String,
        description: String,
        parentId : String?
    ) {
        val category = Category(
            name = title,
            description = description,
            parentCategoryId = parentId
        )

        viewModelScope.launch {
            _createCategory(category).collect {
                when (it) {
                    is ApiResponse.Success -> {  }
                    is ApiResponse.Failure -> { /* should not fail */
                    }
                    is ApiResponse.Loading -> { /* TODO SHOW LOADING ICON */
                    }
                }
            }
        }
    }

}



