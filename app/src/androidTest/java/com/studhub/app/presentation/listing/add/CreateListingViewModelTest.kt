package com.studhub.app.presentation.listing.add

import com.studhub.app.data.repository.MockAuthRepositoryImpl
import com.studhub.app.data.repository.MockCategoryRepositoryImpl
import com.studhub.app.data.repository.MockListingRepositoryImpl
import com.studhub.app.domain.model.Category
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.usecase.category.GetCategories
import com.studhub.app.domain.usecase.listing.CreateListing
import com.studhub.app.domain.usecase.listing.GetDraftListing
import com.studhub.app.domain.usecase.listing.SaveDraftListing
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.random.Random

class CreateListingViewModelTest {
    private val listingRepo = MockListingRepositoryImpl()
    private val authRepo = MockAuthRepositoryImpl()
    private val catRepo = MockCategoryRepositoryImpl()

    private val createListing = CreateListing(listingRepo, authRepo)
    private val saveDraftListing = SaveDraftListing(listingRepo, authRepo)
    private val getDraftListing = GetDraftListing(listingRepo)
    private val getCategories = GetCategories(catRepo)

    private lateinit var viewModel: CreateListingViewModel

    private val categories = mapOf(
        "1" to Category(id = "1", name = "1"),
        "2" to Category(id = "2", name = "2")
    )

    init {
        // populate categories
        catRepo.categories.putAll(categories)

        viewModel = CreateListingViewModel(
            createListing,
            saveDraftListing,
            getDraftListing,
            getCategories
        )
    }

    @After
    fun clearMockDbs() {
        // catRepo.categories.clear()
    }

    @Test
    fun categoriesAreFetchedByTheViewModelOnInit() {
        // small delay so that the view-model has time to retrieve the categories
        runBlocking { delay(50) }

        assertEquals(
            "First category should be the correct one",
            categories.values.first(),
            viewModel.categories.value.first()
        )


        assertEquals(
            "Second and last category should be the correct one",
            categories.values.last(),
            viewModel.categories.value.last()
        )
    }

    @Test
    fun fetchDraftWorksCorrectly() {
        var allGood = false
        val draft = Listing(id = "SuperId", name = "My super listing")

        // save draft
        runBlocking { listingRepo.saveDraftListing(draft).collect() }

        // fetch the draft on the view-model
        runBlocking { viewModel.fetchDraft(draftId = draft.id) { allGood = true } }
        // wait for the view-model to do its magic
        runBlocking { delay(50) }

        assertTrue("Callback given to fetchDraft should be called", allGood)

        assertEquals(
            "Draft listing of view-model should be the correct one",
            draft.name,
            viewModel.draftListing!!.name
        )
    }

    @Test
    fun createListingWorksCorrectly() {
        var allGood = false
        val listing = Listing(name = "My super listing - ${Random.nextLong()}", price = 50F)
        var savedListingId = ""

        // save the listing via the view-model
        runBlocking {
            viewModel.createListing(
                title = listing.name,
                description = "",
                categories = emptyList(),
                price = listing.price,
                meetingPoint = null,
                pictures = mutableListOf(),
            ) { id ->
                allGood = true
                savedListingId = id
            }
        }
        // wait for the view-model to do its magic
        runBlocking { delay(50) }

        assertTrue("Callback given to createListing should be called", allGood)

        assertEquals("The saved listing should be in the DB", listing.name, listingRepo.listingDB[savedListingId]?.name)
    }

    @Test
    fun saveDraftWorksCorrectly() {
        var allGood = false
        val draft = Listing(name = "My super listing - ${Random.nextLong()}", price = 50F)
        var savedListingId = ""

        // save the draft via the view-model
        runBlocking {
            viewModel.saveDraft(
                title = draft.name,
                description = "",
                categories = emptyList(),
                price = draft.price,
                meetingPoint = null,
                pictures = mutableListOf(),
            ) { id ->
                allGood = true
                savedListingId = id
            }
        }
        // wait for the view-model to do its magic
        runBlocking { delay(50) }

        assertTrue("Callback given to saveDraft should be called", allGood)

        assertEquals("The saved draft should be in the cache", draft.name, listingRepo.localListingCache[savedListingId]?.name)
    }
}
