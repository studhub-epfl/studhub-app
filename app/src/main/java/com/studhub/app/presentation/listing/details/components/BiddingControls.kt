package com.studhub.app.presentation.listing.details.components

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.studhub.app.R
import com.studhub.app.annotations.ExcludeFromGeneratedTestCoverage
import com.studhub.app.presentation.ui.common.button.BasicFilledButton
import com.studhub.app.presentation.ui.common.input.NumericTextField
import com.studhub.app.presentation.ui.theme.StudHubTheme

@Composable
fun BiddingControls(price: MutableState<String>, onSubmit: () -> Unit = {}) {
    Column (horizontalAlignment = Alignment.CenterHorizontally) {
        NumericTextField(label = stringResource(R.string.bidding_price_label), rememberedValue = price)
        BasicFilledButton(onClick = { onSubmit() }, label = stringResource(R.string.bidding_button))
    }
}

@ExcludeFromGeneratedTestCoverage
@Preview(showBackground = true)
@Composable
fun BiddingControlsPreview() {
    val price = remember { mutableStateOf("") }
    StudHubTheme {
        BiddingControls(price = price)
    }
}
