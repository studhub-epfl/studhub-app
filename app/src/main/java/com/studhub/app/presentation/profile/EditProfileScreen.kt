package com.studhub.app.presentation.profile

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.studhub.app.R
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.User
import com.studhub.app.presentation.profile.components.EditProfileForm
import com.studhub.app.presentation.ui.common.misc.LoadingCircle
import com.studhub.app.presentation.ui.common.text.BigLabel
import com.studhub.app.presentation.ui.common.misc.Spacer

@Composable
fun EditProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    navigateToProfile: () -> Unit
) {
    when (val currentUser = viewModel.currentUser) {
        is ApiResponse.Loading -> LoadingCircle()
        is ApiResponse.Failure -> {}
        is ApiResponse.Success -> {
            val scrollState = rememberScrollState()

            val firstName = rememberSaveable { mutableStateOf(currentUser.data.firstName) }
            val lastName = rememberSaveable { mutableStateOf(currentUser.data.lastName) }
            val userName = rememberSaveable { mutableStateOf(currentUser.data.userName) }
            val phoneNumber = rememberSaveable { mutableStateOf(currentUser.data.phoneNumber) }
            val profilePictureUri = rememberSaveable { mutableStateOf(currentUser.data.profilePictureUri) }
            val profilePicture = currentUser.data.profilePicture

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .horizontalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer()

                BigLabel(stringResource(R.string.profile_edit_title))

                Spacer()

                EditProfileForm(
                    firstName = firstName,
                    lastName = lastName,
                    userName = userName,
                    phoneNumber = phoneNumber,
                    profilePictureUri = profilePictureUri,
                    profilePicture = profilePicture,
                    onSubmit = {
                        viewModel.updateUserInfo(
                            User(
                                firstName = firstName.value,
                                lastName = lastName.value,
                                userName = userName.value,
                                phoneNumber = phoneNumber.value,
                                profilePictureUri = profilePictureUri.value,
                                profilePicture = profilePicture
                            ),
                            navigateToProfile
                        )
                    }
                )
            }
        }
    }
}
