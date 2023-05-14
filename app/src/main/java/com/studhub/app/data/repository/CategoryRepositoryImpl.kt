package com.studhub.app.data.repository

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Category
import com.studhub.app.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Singleton

@Singleton
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
//like a tree structure
    private val subCategoriess: List<Category> = listOf(
        Category(id = "1", name = "Electronics", description = "phones/cameras/.. ", parentCategoryId = null, subCategories =
            listOf(Category(id = "2", name = "phone", description = "", parentCategoryId = "1"),
                   Category(id = "3", name = "camera", description = "", parentCategoryId = "1" ))
        ),
        Category(id = "4", name = "School Items", description ="books/pencils/bags/..", parentCategoryId = null, subCategories =
            listOf(
                Category(id = "5", name = "book", description = "", parentCategoryId = "4"),
                Category(id = "6", name = "pencils", description = "", parentCategoryId = "4" ),
                Category(id = "7", name = "bag", description = "", parentCategoryId = "4" ))
        ),
        Category(id = "8", name = "Sccessories", description = "keys/necklaces/..", parentCategoryId = null, subCategories =
        listOf(Category(id = "9", name = "key", description = "", parentCategoryId = "8"),
            Category(id = "10", name = "necklace", description = "", parentCategoryId = "8" ))
        ),
        Category(id = "11", name = "Instruments", description = "ear phones/guitar/..", parentCategoryId = null, subCategories =
        listOf(Category(id = "12", name = "ear phone", description = "", parentCategoryId = "11"),
            Category(id = "13", name = "guitar", description = "", parentCategoryId = "11" ))
        ),
        Category(id = "14", name = "Mobility", description = "bikes/scooter/..", parentCategoryId = null, subCategories =
        listOf(Category(id = "15", name = "bike", description = "", parentCategoryId = "14"),
            Category(id = "16", name = "scooter", description = "", parentCategoryId = "14"))
        ),
        Category(id = "17", name = "Clothes", description = "pants/shirts/..", parentCategoryId = null, subCategories =
        listOf(Category(id = "18", name = "pant", description = "", parentCategoryId = "17"),
            Category(id = "19", name = "shirt", description = "", parentCategoryId = "17"))
        ),
        Category(id = "20", name = "Art-decorations", description = "paintings/tapis/..", parentCategoryId = null, subCategories =
        listOf(Category(id = "21", name = "painting", description = "", parentCategoryId = "20"),
            Category(id = "22", name = "tapi", description = "", parentCategoryId = "20"))
        ),
        Category(id = "23", name = "Services", description = "online services/apps/supports/..", parentCategoryId = null, subCategories =
        listOf(Category(id = "24", name = "painting", description = "", parentCategoryId = "23"),
            Category(id = "25", name = "tapi", description = "", parentCategoryId = "23"))
        ),
        Category(id = "26", name = "Other", description = "other")
    )

    override suspend fun getCategories(): Flow<ApiResponse<List<Category>>> {

        return flow {
            emit(ApiResponse.Success(ArrayList(categories)))
        }
    }

    override suspend fun getSubCategories(): Flow<ApiResponse<List<Category>>> {

        return flow {
            emit(ApiResponse.Success(ArrayList(subCategoriess)))
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



    override suspend fun getSubCategory(categoryId: String): Flow<ApiResponse<Category>> {
        val matchingCategory = findCategory(categoryId, subCategoriess)

        return flow {
            if (matchingCategory == null)
                emit(ApiResponse.Failure("No category matching given id"))
            else
                emit(ApiResponse.Success(matchingCategory))
        }
    }

    private fun findCategory(categoryId: String, categories: List<Category>): Category? {
        for (category in categories) {
            if (category.id == categoryId) {
                return category
            } else if (category.subCategories.isNotEmpty()) {
                val matchingSubCategory = findCategory(categoryId, category.subCategories)
                if (matchingSubCategory != null) {
                    return matchingSubCategory
                }
            }
        }
        return null
    }






}
