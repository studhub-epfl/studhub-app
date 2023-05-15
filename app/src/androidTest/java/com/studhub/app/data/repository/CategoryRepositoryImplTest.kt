package com.studhub.app.data.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Category
import com.studhub.app.domain.usecase.category.GetCategories
import com.studhub.app.domain.usecase.category.GetCategory
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject
import kotlin.random.Random

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class CategoryRepositoryImplTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var getCategories: GetCategories


    @Inject
    lateinit var getCategory: GetCategory


    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun testGetCategoriesRandomlyChosenRetrievedCategoryMatchesGetCategoryWithSameIndex() {
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

    @Test
    fun testParentingIndexIsNullOnMainCategoryAndNotNullOnSub() {
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


            if(expectedCategory.parentCategoryId == null){
                runBlocking {
                    getCategory(randomIndex.toString()).collect {
                        when (it) {
                            is ApiResponse.Success -> retrievedCategory = it.data
                            is ApiResponse.Failure -> fail("Should not fail")
                            is ApiResponse.Loading -> {}
                        }
                    }
                }

                assertNull(retrievedCategory.parentCategoryId)

            } else{

                runBlocking {
                    getCategory(randomIndex.toString()).collect {
                        when (it) {
                            is ApiResponse.Success -> retrievedCategory = it.data
                            is ApiResponse.Failure -> fail("Should not fail")
                            is ApiResponse.Loading -> {}
                        }
                    }
                }

                assertNotNull(retrievedCategory.parentCategoryId)

            }


    }

    @Test
    fun testParentingIndexRightWhenChild() {
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


        if(expectedCategory.parentCategoryId == null){
            runBlocking {
                getCategory(randomIndex.toString()).collect {
                    when (it) {
                        is ApiResponse.Success -> retrievedCategory = it.data
                        is ApiResponse.Failure -> fail("Should not fail")
                        is ApiResponse.Loading -> {}
                    }
                }
            }

            assertNull(retrievedCategory.parentCategoryId)

        } else{

            runBlocking {
                getCategory(randomIndex.toString()).collect {
                    when (it) {
                        is ApiResponse.Success -> retrievedCategory = it.data
                        is ApiResponse.Failure -> fail("Should not fail")
                        is ApiResponse.Loading -> {}
                    }
                }
            }

            assert(retrievedCategories.contains( retrievedCategories.filter { category -> category.id == retrievedCategory.parentCategoryId.toString() }.get(0) ))

        }


    }

}
