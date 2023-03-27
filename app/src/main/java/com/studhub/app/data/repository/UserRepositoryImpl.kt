package com.studhub.app.data.repository

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.model.User
import com.studhub.app.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl : UserRepository {
    private val db: DatabaseReference = Firebase.database.getReference("users")

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

    override suspend fun updateUserInfo(userId: String, updatedUser: User): Flow<ApiResponse<User>> = flow {
        emit(ApiResponse.Loading)

        val query = db.child(userId).updateChildren(mapOf(
            "firstName" to updatedUser.firstName,
            "lastName" to updatedUser.lastName,
            "userName" to updatedUser.userName,
            "phoneNumber" to updatedUser.phoneNumber
        ))

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
        val favoriteListingsRef = userRef.child("favoriteListings")

        val userSnapshot = userRef.get().await()

        val user = userSnapshot.getValue(User::class.java)!!
        if (user.favoriteListings.contains(favListingId)) {
            emit(ApiResponse.Failure("Listing is already a favorite"))
            return@flow
        }

        val updatedFavoriteListings =
            user.favoriteListings.toMutableList().apply { add(favListingId) }
        val updatedUser = user.copy(favoriteListings = updatedFavoriteListings)

        val query = favoriteListingsRef.setValue(updatedUser)

        query.await()

        if (query.isSuccessful) {
            emit(ApiResponse.Success(updatedUser))
        } else {
            val errorMessage = query.exception?.message.orEmpty()
            emit(ApiResponse.Failure(errorMessage.ifEmpty { "Firebase error" }))
        }
    }

    override suspend fun removeFavoriteListing(
        userId: String,
        favListingId: String
    ): Flow<ApiResponse<Boolean>> = flow {
        emit(ApiResponse.Loading)

        val userRef = db.child(userId)
        val favoriteListingsRef = userRef.child("favoriteListings")

        val userSnapshot = userRef.get().await()

        val user = userSnapshot.getValue(User::class.java)!!
        if (!user.favoriteListings.contains(favListingId)) {
            emit(ApiResponse.Failure("Listing is not a favorite"))
            return@flow
        }

        val updatedFavoriteListings =
            user.favoriteListings.toMutableList().apply { remove(favListingId) }

        val query = favoriteListingsRef.setValue(updatedFavoriteListings)

        query.await()

        if (query.isSuccessful) {
            emit(ApiResponse.Success(true))
        } else {
            val errorMessage = query.exception?.message.orEmpty()
            emit(ApiResponse.Failure(errorMessage.ifEmpty { "Firebase error" }))
        }
    }

    override suspend fun getFavoriteListings(userId: String): Flow<ApiResponse<List<Listing>>> =
        flow {
            emit(ApiResponse.Loading)

            val userRef = db.child(userId)
            val favoriteListingsRef = userRef.child("favoriteListings")

            val favoriteListingsSnapshot = favoriteListingsRef.get().await()

            if (favoriteListingsSnapshot.exists()) {
                val favoriteListingIds = favoriteListingsSnapshot.getValue<List<String>>()!!
                val favoriteListings = mutableListOf<Listing>()

                favoriteListingIds.forEach { favoriteListingId ->
                    val listingSnapshot =
                        Firebase.database.getReference("listings").child(favoriteListingId).get()
                            .await()

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
}
