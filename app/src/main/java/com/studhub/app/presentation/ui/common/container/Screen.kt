package com.studhub.app.presentation.ui.common.container

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.studhub.app.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Screen(
    title: String,
    onGoBackClick: (() -> Unit)?,
    content: @Composable ColumnScope.() -> Unit
) {
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            if (onGoBackClick != null)
                TopAppBar(
                    title = { Text(text = title) },
                    navigationIcon = {
                        IconButton(
                            onClick = onGoBackClick
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.ArrowBack,
                                contentDescription = stringResource(R.string.misc_btn_go_back),
                            )
                        }
                    }
                )
            else TopAppBar( title = { Text(text = title) } )
        },
        content = {
            Column(
                modifier = Modifier
                    .padding(it)
                    .verticalScroll(scrollState)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                content = content
            )
        }
    )
}
