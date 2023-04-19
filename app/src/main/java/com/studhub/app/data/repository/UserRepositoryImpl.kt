package com.studhub.app.data.repository

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.data.storage.StorageHelper
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.model.Rating
import com.studhub.app.domain.model.User
import com.studhub.app.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl : UserRepository {
    private val db: DatabaseReference = Firebase.database.getReference("users")
    private val storageHelper = StorageHelper()

    override suspend fun createUser(user: User): Flow<ApiResponse<User>> {
        val userId: String = db.push().key.orEmpty()
        val userToPush: User = user.copy(id = userId)

        return flow {
            emit(ApiResponse.Loading)

            val query = db.child(userId).setValue(userToPush)

            query.await()

            if (query.isSuccessful) {
                emit(ApiResponse.Success(userToPush))
            } else {
                val errorMessage = query.exception?.message.orEmpty()
                emit(ApiResponse.Failure(errorMessage.ifEmpty { "Firebase error" }))
            }
        }
    }

    override suspend fun getUser(userId: String): Flow<ApiResponse<User>> = flow {
        emit(ApiResponse.Loading)

        val query = db.child(userId).get()

        query.await()

        if (query.isSuccessful) {
            val retrievedUser: User? = query.result.getValue(User::class.java)
            if (retrievedUser == null) {
                emit(ApiResponse.Failure("User does not exist"))
            } else {
                emit(ApiResponse.Success(retrievedUser))
            }
        } else {
            val errorMessage = query.exception?.message.orEmpty()
            emit(ApiResponse.Failure(errorMessage.ifEmpty { "Firebase error" }))
        }
    }

    override suspend fun updateUserInfo(
        userId: String,
        updatedUser: User
    ): Flow<ApiResponse<User>> = flow {
        emit(ApiResponse.Loading)

        var profilePicture = updatedUser.profilePicture

        // replace profile picture if there is a new one
        // in the future, we may want to remove the previous profile picture
        if (updatedUser.profilePictureUri != null) {
            profilePicture = storageHelper.storePicture(updatedUser.profilePictureUri, "users")
        }

        val query = db.child(userId).updateChildren(
            mapOf(
                "firstName" to updatedUser.firstName,
                "lastName" to updatedUser.lastName,
                "userName" to updatedUser.userName,
                "phoneNumber" to updatedUser.phoneNumber,
                "profilePicture" to profilePicture
            )
        )

        query.await()

        if (query.isSuccessful) {
            val updatedUserQuery = db.child(userId).get()
            updatedUserQuery.await()
            val retrievedUpdatedUser: User? = updatedUserQuery.result.getValue(User::class.java)
            if (retrievedUpdatedUser == null) {
                emit(ApiResponse.Failure("An error occurred while retrieving the updated user"))
            } else {
                emit(ApiResponse.Success(retrievedUpdatedUser))
            }
        } else {
            val errorMessage = query.exception?.message.orEmpty()
            emit(ApiResponse.Failure(errorMessage.ifEmpty { "Firebase error" }))
        }
    }

    override suspend fun removeUser(userId: String): Flow<ApiResponse<Boolean>> {
        TODO("Not yet implemented")
    }

    override suspend fun addFavoriteListing(
        userId: String,
        favListingId: String
    ): Flow<ApiResponse<User>> = flow {
        emit(ApiResponse.Loading)

        val userRef = db.child(userId)
        val favoriteListingsRef = userRef.child("favoriteListings").child(favListingId)

        val userQuery = userRef.get()

        userQuery.await()

        val user: User? = userQuery.result.getValue(User::class.java)

        if (user != null) {
            val updatedFavoriteListings = user.favoriteListings.toMutableMap().apply { put(favListingId, true) }
            val updatedUser = user.copy(favoriteListings = updatedFavoriteListings)
            val query = favoriteListingsRef.setValue(true)

            query.await()

            if (query.isSuccessful) {
                emit(ApiResponse.Success(updatedUser))
            } else {
                val errorMessage = query.exception?.message.orEmpty()
                emit(ApiResponse.Failure(errorMessage.ifEmpty { "Firebase error" }))
            }
        }
    }

    override suspend fun removeFavoriteListing(
        userId: String,
        favListingId: String
    ): Flow<ApiResponse<User>> = flow {
        emit(ApiResponse.Loading)

        val userRef = db.child(userId)
        val favoriteListingsRef = userRef.child("favoriteListings").child(favListingId)

        val userQuery = userRef.get()

        userQuery.await()

        val user: User? = userQuery.result.getValue(User::class.java)

        if (user != null) {
            val updatedFavoriteListings = user.favoriteListings.toMutableMap().apply { remove(favListingId) }
            val updatedUser = user.copy(favoriteListings = updatedFavoriteListings)
            val query = favoriteListingsRef.setValue(null)

            query.await()

            if (query.isSuccessful) {
                emit(ApiResponse.Success(updatedUser))
            } else {
                val errorMessage = query.exception?.message.orEmpty()
                emit(ApiResponse.Failure(errorMessage.ifEmpty { "Firebase error" }))
            }
        }
    }

    override suspend fun getFavoriteListings(userId: String): Flow<ApiResponse<List<Listing>>> =
        flow {
            emit(ApiResponse.Loading)

            val userRef = db.child(userId)
            val favoriteListingsRef = userRef.child("favoriteListings")

            val favoriteListingQuery = favoriteListingsRef.get()

            favoriteListingQuery.await()

            val favoriteListingMap: Map<String, Boolean>? =
                favoriteListingQuery.result.getValue<Map<String, Boolean>>()
            if (favoriteListingMap != null) {
                val favoriteListingIds = favoriteListingMap.keys.toList()
                val favoriteListings = mutableListOf<Listing>()
                val listingRef = Firebase.database.getReference("listings")

                favoriteListingIds.forEach { favoriteListingId ->
                    val listingSnapshot =
                        listingRef.child(favoriteListingId).get().await()

                    if (listingSnapshot.exists()) {
                        val listing = listingSnapshot.getValue(Listing::class.java)!!
                        favoriteListings.add(listing)
                    }
                }

                emit(ApiResponse.Success(favoriteListings))
            } else {
                emit(ApiResponse.Success(emptyList()))
            }
        }

    override suspend fun blockUser(
        userId: String, blockedUserId: String
    ): Flow<ApiResponse<User>> = flow {
        emit(ApiResponse.Loading)

        val userRef = db.child(userId)
        val blockedUsersRef = userRef.child("blockedUsers").child(blockedUserId)

        val userQuery = userRef.get()

        userQuery.await()

        val user: User? = userQuery.result.getValue(User::class.java)

        if (user != null) {
            val updatedBlockedUsers =
                user.blockedUsers.toMutableMap().apply { put(blockedUserId, true) }
            val updatedUser = user.copy(blockedUsers = updatedBlockedUsers)
            val query = blockedUsersRef.setValue(true)

            query.await()

            if (query.isSuccessful) {
                emit(ApiResponse.Success(updatedUser))
            } else {
                val errorMessage = query.exception?.message.orEmpty()
                emit(ApiResponse.Failure(errorMessage.ifEmpty { "Firebase error" }))
            }
        }
    }

    override suspend fun unblockUser(
        userId: String,
        blockedUserId: String
    ): Flow<ApiResponse<User>> = flow {
        emit(ApiResponse.Loading)

        val userRef = db.child(userId)
        val blockedUsersRef = userRef.child("blockedUsers").child(blockedUserId)

        val userQuery = userRef.get()

        userQuery.await()

        val user: User? = userQuery.result.getValue(User::class.java)

        if (user != null) {
            val updatedBlockedUsers =
                user.blockedUsers.toMutableMap().apply { remove(blockedUserId) }
            val updatedUser = user.copy(favoriteListings = updatedBlockedUsers)
            val query = blockedUsersRef.setValue(null)

            query.await()

            if (query.isSuccessful) {
                emit(ApiResponse.Success(updatedUser))
            } else {
                val errorMessage = query.exception?.message.orEmpty()
                emit(ApiResponse.Failure(errorMessage.ifEmpty { "Firebase error" }))
            }
        }
    }

    override suspend fun addRating(userId: String, rating: Rating): Flow<ApiResponse<Rating>> = flow {
        emit(ApiResponse.Loading)

        val ratingId: String = db.child(userId).child("ratings").push().key.orEmpty()
        val ratingToPush: Rating = rating.copy(id = ratingId)

        val query = db.child(userId).child("ratings").child(ratingId).setValue(ratingToPush)
        query.await()

        if (query.isSuccessful) {
            emit(ApiResponse.Success(ratingToPush))
        } else {
            val errorMessage = query.exception?.message.orEmpty()
            emit(ApiResponse.Failure(errorMessage.ifEmpty { "Firebase error" }))
        }
    }

    override suspend fun updateRating(userId: String, ratingId: String, rating: Rating): Flow<ApiResponse<Rating>> = flow {
        emit(ApiResponse.Loading)

        val query = db.child(userId).child("ratings").child(ratingId).setValue(rating)
        query.await()

        if (query.isSuccessful) {
            emit(ApiResponse.Success(rating))
        } else {
            val errorMessage = query.exception?.message.orEmpty()
            emit(ApiResponse.Failure(errorMessage.ifEmpty { "Firebase error" }))
        }
    }

    override suspend fun deleteRating(userId: String, ratingId: String): Flow<ApiResponse<Boolean>> = flow {
        emit(ApiResponse.Loading)

        val query = db.child(userId).child("ratings").child(ratingId).removeValue()
        query.await()

        if (query.isSuccessful) {
            emit(ApiResponse.Success(true))
        } else {
            val errorMessage = query.exception?.message.orEmpty()
            emit(ApiResponse.Failure(errorMessage.ifEmpty { "Firebase error" }))
        }
    }

    override suspend fun getUserRatings(userId: String): Flow<ApiResponse<List<Rating>>> = flow {
        emit(ApiResponse.Loading)

        val query = db.child(userId).child("ratings").get()
        query.await()

        if (query.isSuccessful) {
            val ratingsMap: Map<String, Rating>? = query.result.getValue<Map<String, Rating>>()
            if (ratingsMap != null) {
                val ratingsList = ratingsMap.values.toList()
                emit(ApiResponse.Success(ratingsList))
            } else {
                emit(ApiResponse.Success(emptyList()))
            }
        } else {
            val errorMessage = query.exception?.message.orEmpty()
            emit(ApiResponse.Failure(errorMessage.ifEmpty { "Firebase error" }))
        }
    }
}
