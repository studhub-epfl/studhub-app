package com.studhub.app.presentation.home

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.studhub.app.data.repository.MockAuthRepositoryImpl
import com.studhub.app.data.repository.MockUserRepositoryImpl
import com.studhub.app.domain.usecase.user.GetCurrentUser
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeViewModelTest {
    private val authRepo = MockAuthRepositoryImpl()
    private val userRepo = MockUserRepositoryImpl()
    private val getCurrentUser = GetCurrentUser(userRepo, authRepo)

    private lateinit var viewModel: HomeViewModel

    @Test
    fun getLoggedInUserIsCorrectlyDispatchedOnInit() {
        viewModel = HomeViewModel(getCurrentUser)

        // wait for view model to dispatch
        runBlocking { delay(50) }

        assertEquals(
            "User Id should be the expected one",
            authRepo.currentUserUid,
            viewModel.currentUser.value!!.id
        )
    }
}
