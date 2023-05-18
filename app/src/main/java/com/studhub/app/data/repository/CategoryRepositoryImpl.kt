package com.studhub.app.data.repository

import com.google.firebase.database.FirebaseDatabase
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.data.local.LocalDataSource
import com.studhub.app.data.network.NetworkStatus
import com.studhub.app.data.storage.StorageHelper
import com.studhub.app.domain.model.Category
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.repository.CategoryRepository
import com.studhub.app.domain.repository.ListingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepositoryImpl @Inject constructor(
    private val remoteDb: FirebaseDatabase,
    private val localDb: LocalDataSource,
    private val networkStatus: NetworkStatus
) : CategoryRepository {

    private val db = remoteDb.getReference("categories")
    private var provisionalListing = mutableListOf<Listing>()
    private val storageHelper = StorageHelper()


    override suspend fun getCategories(): Flow<ApiResponse<List<Category>>> = flow {
        emit(ApiResponse.Loading)

        if (!networkStatus.isConnected) {
            emit(ApiResponse.NO_INTERNET_CONNECTION)
            return@flow
        }

        val query = db.get()

        query.await()

        if (query.isSuccessful) {
            val categories = mutableListOf<Category>()

            for (listingSnapshot in query.result.children) {
                val retrievedCategory: Category? = listingSnapshot.getValue(Category::class.java)
                if (retrievedCategory != null) {
                    categories.add(retrievedCategory)
                }
            }

            emit(ApiResponse.Success(categories))
        } else {
            val errorMessage = query.exception?.message.orEmpty()
            emit(ApiResponse.Failure(errorMessage.ifEmpty { "Firebase error" }))
        }
    }


    override suspend fun getCategory(categoryId: String): Flow<ApiResponse<Category>> = flow {
        emit(ApiResponse.Loading)

        if (!networkStatus.isConnected) {
            emit(ApiResponse.NO_INTERNET_CONNECTION)
            return@flow
        }

        val query = db.child(categoryId).get()

        query.await()

        if (query.isSuccessful) {
            val retrievedCategory: Category? = query.result.getValue(Category::class.java)
            if (retrievedCategory == null) {
                emit(ApiResponse.Failure("Category does not exist"))
            } else {
                emit(ApiResponse.Success(retrievedCategory))
            }
        } else {
            val errorMessage = query.exception?.message.orEmpty()
            emit(ApiResponse.Failure(errorMessage.ifEmpty { "Firebase error" }))
        }
    }
}










