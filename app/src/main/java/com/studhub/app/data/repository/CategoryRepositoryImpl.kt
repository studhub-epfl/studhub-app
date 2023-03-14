package com.studhub.app.data.repository

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Category
import com.studhub.app.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CategoryRepositoryImpl : CategoryRepository {
    private val categories: List<Category> = listOf(
        Category(id = "1", name = "electronics", description = "phones/cameras/.. "),
        Category(id = "2", name = "school items", description ="books/pencils/bags/.."),
        Category(id = "3", name = "accessories", description = "keys/necklaces/.."),
        Category(id = "4", name = "instruments", description = "ear phones/guitar/.."),
        Category(id = "5", name = "mobility", description = "bikes/scooter/.."),
        Category(id = "6", name = "clothes", description = "pants/shirts/.."),
        Category(id = "7", name = "art-decorations", description = "paintings/tapis/.."),
        Category(id = "8", name = "services", description = "online services/apps/supports/.."),
        Category(id = "9", name = "other", description = "other")
    )

    override suspend fun getCategories(): Flow<ApiResponse<List<Category>>> {

        return flow {
            emit(ApiResponse.Success(ArrayList(categories)))
        }
    }

    override suspend fun getCategory(categoryId: String): Flow<ApiResponse<Category>> {
        val matchingCats: List<Category> = categories.filter { it.id == categoryId }
        return flow {
            if (matchingCats.isEmpty())
                emit(ApiResponse.Failure("No category matching given id"))
            else
                emit(ApiResponse.Success(matchingCats[0]))
        }
    }
}
