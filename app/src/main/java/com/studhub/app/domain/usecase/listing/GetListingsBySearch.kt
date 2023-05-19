package com.studhub.app.domain.usecase.listing

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.repository.AuthRepository
import com.studhub.app.domain.repository.ListingRepository
import com.studhub.app.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject


/**
 * Use case for getting all listings from a given [repository] matching a searching constraint
 *
 * @param [repository] the repository which the use case will act on
 */
class GetListingsBySearch @Inject constructor(
    private val repository: ListingRepository,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {

    /**
     * Retrieves all listings matching the given [keyword] from the [repository]
     *
     * @param [keyword] the value to compare to the listings
     */
    suspend operator fun invoke(
            keyword: String,
            minPrice: String,
            maxPrice: String
        ):Flow<ApiResponse<List<Listing>>> {
        if (keyword.isEmpty()) {
            return repository.getListings()
        }
/*
        if (keyword.length < 3) {
            return flowOf(ApiResponse.Failure("Too few characters"))
        }
        */

        if (minPrice.toFloatOrNull() == null || maxPrice.toFloatOrNull() == null) {
            return flowOf(ApiResponse.Failure("Input is not a number"))
        }

        return flow {
            userRepository.getUser(authRepository.currentUserUid).collect { userQuery ->
                when (userQuery) {
                    is ApiResponse.Success -> {
                        repository.getListingsBySearch(keyword, minPrice, maxPrice, userQuery.data.blockedUsers)
                            .collect { listingQuery ->
                                when (listingQuery) {
                                    is ApiResponse.Failure -> emit(ApiResponse.Failure(listingQuery.message))
                                    is ApiResponse.Loading -> emit(ApiResponse.Loading)
                                    is ApiResponse.Success -> emit(listingQuery)
                                }
                            }
                    }
                    is ApiResponse.Loading -> emit(ApiResponse.Loading)
                    else -> emit(ApiResponse.Failure("No user available"))
                }
            }
        }
    }
}
