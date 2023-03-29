package com.studhub.app.presentation.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.studhub.app.R
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.User
import com.studhub.app.presentation.profile.components.EditProfileForm
import com.studhub.app.presentation.ui.common.misc.LoadingCircle
import com.studhub.app.presentation.ui.common.text.BigLabel

@Composable
fun EditProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    navigateToProfile: () -> Unit
) {
    when (val currentUser = viewModel.currentUser) {
        is ApiResponse.Loading -> LoadingCircle()
        is ApiResponse.Failure -> {}
        is ApiResponse.Success -> {
            val firstName = rememberSaveable { mutableStateOf(currentUser.data.firstName) }
            val lastName = rememberSaveable { mutableStateOf(currentUser.data.lastName) }
            val userName = rememberSaveable { mutableStateOf(currentUser.data.userName) }
            val phoneNumber = rememberSaveable { mutableStateOf(currentUser.data.phoneNumber) }

            BigLabel(stringResource(R.string.profile_title))
            EditProfileForm(
                firstName = firstName,
                lastName = lastName,
                userName = userName,
                phoneNumber = phoneNumber,
                onSubmit = {
                    viewModel.updateUserInfo(
                        User(
                            firstName = firstName.value,
                            lastName = lastName.value,
                            userName = userName.value,
                            phoneNumber = phoneNumber.value,
                        )
                    )

                    navigateToProfile()
                }
            )
        }
    }
}
