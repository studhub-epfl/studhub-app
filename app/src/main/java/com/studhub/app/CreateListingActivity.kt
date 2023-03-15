package com.studhub.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.studhub.app.presentation.ui.common.input.BasicTextField
import com.studhub.app.presentation.ui.common.text.BigLabel
import com.studhub.app.presentation.ui.common.input.TextBox
import com.studhub.app.ui.theme.StudHubTheme

class CreateListingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CreateListingView()
        }
    }
}


@Composable
fun CreateListingView() {
    StudHubTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    BigLabel(label = "Create Listing")
                    ListingForm()
                }
            }
        }
    }
}

@Composable
fun ListingForm(){
    BasicTextField(label = "Item title")
    TextBox(label = "Item description")
}


@Preview(showBackground = true)
@Composable
fun CreateListingPreview() {
    CreateListingView()
}
