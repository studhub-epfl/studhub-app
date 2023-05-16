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


//like a tree structure
    private val categories: List<Category> = listOf(
        Category(id = "1", name = "Electronics", description = "phones/cameras/.. ", parentCategoryId = null),
        Category(id = "2", name = "phone", description = "", parentCategoryId = "1"),
        Category(id = "3", name = "camera", description = "", parentCategoryId = "1" ),

        Category(id = "4", name = "School Items", description ="books/pencils/bags/..", parentCategoryId = null),
        Category(id = "5", name = "book", description = "", parentCategoryId = "4"),
        Category(id = "6", name = "pencils", description = "", parentCategoryId = "4" ),
        Category(id = "7", name = "bag", description = "", parentCategoryId = "4" ),

        Category(id = "8", name = "Accessories", description = "keys/necklaces/..", parentCategoryId = null),
        Category(id = "9", name = "key", description = "", parentCategoryId = "8"),
        Category(id = "10", name = "necklace", description = "", parentCategoryId = "8" ),

        Category(id = "11", name = "Instruments", description = "ear phones/guitar/..", parentCategoryId = null),
        Category(id = "12", name = "ear phone", description = "", parentCategoryId = "11"),
        Category(id = "13", name = "guitar", description = "", parentCategoryId = "11" ),

        Category(id = "14", name = "Mobility", description = "bikes/scooter/..", parentCategoryId = null),
        Category(id = "15", name = "bike", description = "", parentCategoryId = "14"),
        Category(id = "16", name = "scooter", description = "", parentCategoryId = "14"),

        Category(id = "17", name = "Clothes", description = "pants/shirts/..", parentCategoryId = null),
        Category(id = "18", name = "pant", description = "", parentCategoryId = "17"),
        Category(id = "19", name = "shirt", description = "", parentCategoryId = "17"),

        Category(id = "20", name = "Art-decorations", description = "paintings/tapis/..", parentCategoryId = null),
        Category(id = "21", name = "painting", description = "", parentCategoryId = "20"),
        Category(id = "22", name = "tapi", description = "", parentCategoryId = "20"),

        Category(id = "23", name = "Services", description = "online services/apps/supports/..", parentCategoryId = null),
        Category(id = "24", name = "online service", description = "", parentCategoryId = "23"),
        Category(id = "25", name = "app", description = "", parentCategoryId = "23"),
        Category(id = "26", name = "support", description = "", parentCategoryId = "23")

    )



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










