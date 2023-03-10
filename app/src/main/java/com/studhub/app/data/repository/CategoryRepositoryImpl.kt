package com.studhub.app.data.repository

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Category
import com.studhub.app.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CategoryRepositoryImpl : CategoryRepository {
    private val categories: List<Category> = listOf(
        Category(id = 1, name = "electronics", "phones/cameras/.. "),
        Category(id = 2, name = "school items","books/pencils/bags/.."),
        Category(id = 3, name = "accessories","keys/necklaces/.."),
        Category(id = 4, name = "insruments","ear phones/guitar/.."),
        Category(id = 5, name = "mobility","bikes/scooter/.."),
        Category(id = 6, name = "clothes","pants/shirts/.."),
        Category(id = 7, name = "artdecorations","paintings/tapis/.."),
        Category(id = 8, name = "services","online services/apps/supports/.."),
        Category(id = 9, name = "other","other")
    )

    override suspend fun getCategories(): Flow<ApiResponse<List<Category>>> {

        return flow {
            emit(ApiResponse.Success(ArrayList(categories)))
        }
    }

    override suspend fun getCategory(categoryId: Long): Flow<ApiResponse<Category>> {
        val matchingCats: List<Category> = categories.filter { it.id == categoryId }
        return flow {
            if (matchingCats.isEmpty())
                emit(ApiResponse.Failure("No category matching given id"))
            else
                emit(ApiResponse.Success(matchingCats[0]))
        }
    }
}