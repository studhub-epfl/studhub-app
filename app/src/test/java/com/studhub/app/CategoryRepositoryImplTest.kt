package com.studhub.app

import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.data.repository.CategoryRepositoryImpl
import com.studhub.app.domain.model.Category
import com.studhub.app.domain.usecase.category.GetCategories
import com.studhub.app.domain.usecase.category.GetCategory
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test
import kotlin.random.Random

class CategoryRepositoryImplTest {

    @Test
    fun `GetCategories randomly chosen retrieved category matches GetCategory with same index`() {
        val repository = CategoryRepositoryImpl()
        val getCategories = GetCategories(repository)
        val getCategory = GetCategory(repository)

        var retrievedCategories: List<Category> = emptyList()

        runBlocking {
            getCategories().collect {
                when (it) {
                    is ApiResponse.Success -> retrievedCategories = it.data
                    is ApiResponse.Failure -> fail("Should not fail")
                    is ApiResponse.Loading -> {}
                }
            }
        }

        val numberOfCats = retrievedCategories.size

        assert(numberOfCats > 0)

        val randomIndex = Random.nextInt(numberOfCats) + 1 // IDs start at 1
        val expectedCategory = retrievedCategories.first { it.id == randomIndex.toString() }
        var retrievedCategory = Category()

        runBlocking {
            getCategory(randomIndex.toString()).collect {
                when (it) {
                    is ApiResponse.Success -> retrievedCategory = it.data
                    is ApiResponse.Failure -> fail("Should not fail")
                    is ApiResponse.Loading -> {}
                }
            }
        }

        assertEquals(expectedCategory, retrievedCategory)
    }
}
