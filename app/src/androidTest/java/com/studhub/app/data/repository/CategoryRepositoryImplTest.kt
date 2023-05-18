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

    //like a tree structure
    private val provisoryCatHard: List<Category> = listOf(
        Category(id = "-NV_ff7dXGytMxTdOISP", name = "Electronics", description = "phones/cameras/.. ", parentCategoryId = null),
        Category(id = "-NV_gvd8fhYRYtUpBmdy", name = "phone", description = "", parentCategoryId = "-NV_ff7dXGytMxTdOISP"),
        Category(id = "-NV_h5m-CIDbV9lzUcYy", name = "camera", description = "", parentCategoryId = "-NV_ff7dXGytMxTdOISP" ),

        Category(id = "-NV_iA_M1IyfsR5WAx-e", name = "School Items", description ="books/pencils/bags/..", parentCategoryId = null),
        Category(id = "-NV_jA0bDJtsB4ME72AT", name = "book", description = "", parentCategoryId = "-NV_iA_M1IyfsR5WAx-e"),
        Category(id = "-NV_jE0AeJBPAB_IWGme", name = "pencils", description = "", parentCategoryId = "-NV_iA_M1IyfsR5WAx-e" ),
        Category(id = "-NV_jN4HfHjiw1RzB2w4", name = "bag", description = "", parentCategoryId = "-NV_iA_M1IyfsR5WAx-e" ),

        Category(id = "-NV_oO9OWfq8zkdp0R9t", name = "Accessories", description = "keys/necklaces/..", parentCategoryId = null),
        Category(id = "-NV_rR7-Opyp0MAAksmg", name = "key", description = "", parentCategoryId = "-NV_oO9OWfq8zkdp0R9t"),
        Category(id = "-NV_rR76cm4_JwxGlleL", name = "necklace", description = "", parentCategoryId = "-NV_oO9OWfq8zkdp0R9t" ),

        Category(id = "-NV_oO9SV6kxVDNerPF4", name = "Instruments", description = "ear phones/guitar/..", parentCategoryId = null),
        Category(id = "-NV_rR79wJUDw21JxtjS", name = "ear phone", description = "", parentCategoryId = "-NV_oO9SV6kxVDNerPF4"),
        Category(id = "-NV_rR7C0LHXLG2PvpQt", name = "guitar", description = "", parentCategoryId = "-NV_oO9SV6kxVDNerPF4" ),

        Category(id = "-NV_oO9THptu5_EyKism", name = "Mobility", description = "bikes/scooter/..", parentCategoryId = null),
        Category(id = "-NV_rR7Q6CKgTdZBrYEP", name = "bike", description = "", parentCategoryId = "-NV_oO9THptu5_EyKism"),
        Category(id = "-NV_rR7VYhw5FJqT5Y1U", name = "scooter", description = "", parentCategoryId = "-NV_oO9THptu5_EyKism"),

        Category(id = "-NV_oO9THptu5_EyKisn", name = "Clothes", description = "pants/shirts/..", parentCategoryId = null),
        Category(id = "-NV_rR7Zv7uIMpWb962X", name = "pant", description = "", parentCategoryId = "-NV_oO9THptu5_EyKisn"),
        Category(id = "-NV_rR7a2Vjr2ghYx58Q", name = "shirt", description = "", parentCategoryId = "-NV_oO9THptu5_EyKisn"),

        Category(id = "-NV_oO9UBVf3GhVqisj7", name = "Art-decorations", description = "paintings/tapis/..", parentCategoryId = null),
        Category(id = "-NV_rR7cps_WE-dnn9bn", name = "painting", description = "", parentCategoryId = "-NV_oO9UBVf3GhVqisj7"),
        Category(id = "-NV_rR7dk6IvOw6IwDPx", name = "tapi", description = "", parentCategoryId = "-NV_oO9UBVf3GhVqisj7"),

        Category(id = "-NV_oO9VrFJsY9xU2rID", name = "Services", description = "online services/apps/supports/..", parentCategoryId = null),
        Category(id = "-NV_rR7eX77_v83vs8Hu", name = "online service", description = "", parentCategoryId = "-NV_oO9VrFJsY9xU2rID"),
        Category(id = "-NV_rR7gHjocbuXW7cva", name = "app", description = "", parentCategoryId = "-NV_oO9VrFJsY9xU2rID"),
        Category(id = "-NV_rR7i9NEUYNp3_wdu", name = "support", description = "", parentCategoryId = "-NV_oO9VrFJsY9xU2rID")

    )

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

        val randomIndex = Random.nextInt(numberOfCats)
        val expectedCategory = retrievedCategories.first { it.id == provisoryCatHard.get(randomIndex).id  }
        var retrievedCategory = Category()

        runBlocking {
            getCategory(provisoryCatHard.get(randomIndex).id).collect {
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

        val randomIndex = Random.nextInt(numberOfCats)
        val expectedCategory = retrievedCategories.first { it.id == provisoryCatHard.get(randomIndex).id }
        var retrievedCategory = Category()


            if(expectedCategory.parentCategoryId == null){
                runBlocking {
                    getCategory(provisoryCatHard.get(randomIndex).id).collect {
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
                    getCategory(provisoryCatHard.get(randomIndex).id).collect {
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

        val randomIndex = Random.nextInt(numberOfCats)
        val expectedCategory = retrievedCategories.first { it.id == provisoryCatHard.get(randomIndex).id }
        var retrievedCategory = Category()


        if(expectedCategory.parentCategoryId == null){
            runBlocking {
                getCategory(provisoryCatHard.get(randomIndex).id).collect {
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
                getCategory(provisoryCatHard.get(randomIndex).id).collect {
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
