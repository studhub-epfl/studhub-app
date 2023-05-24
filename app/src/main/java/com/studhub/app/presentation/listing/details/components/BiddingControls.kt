package com.studhub.app.presentation.listing.details.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.studhub.app.R
import com.studhub.app.annotations.ExcludeFromGeneratedTestCoverage
import com.studhub.app.presentation.ui.common.button.BasicFilledButton
import com.studhub.app.presentation.ui.common.input.NumericTextField
import com.studhub.app.presentation.ui.common.misc.Spacer
import com.studhub.app.presentation.ui.theme.StudHubTheme
import java.text.SimpleDateFormat
import java.util.*

/**
 * This renders the controls for the detailed listing screen only if it's of bidding type so the user
 * can bid on those.
 * @param price mutablestate of string to be used for the price bid field
 * @param onSubmit the submit handler
 */
@SuppressLint("SimpleDateFormat")
@Composable
fun BiddingControls(price: MutableState<String>, deadline: Date, onSubmit: () -> Unit = {}) {
    val calendar =  GregorianCalendar()
    calendar.time = deadline
    val formatter = SimpleDateFormat("dd.MM.yyyy")
    Column (
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = String.format(stringResource(R.string.listing_deadline), formatter.format(calendar.time)))
        Spacer("medium")
        NumericTextField(label = stringResource(R.string.bidding_price_label), rememberedValue = price)
        BasicFilledButton(onClick = { onSubmit() }, label = stringResource(R.string.bidding_button))
    }
}

@ExcludeFromGeneratedTestCoverage
@Preview(showBackground = true)
@Composable
fun BiddingControlsPreview() {
    val price = remember { mutableStateOf("") }
    //using an arbitrary millisecond timestamp from the internet for this preview
    val date = Date(1684946575500L)
    StudHubTheme {
        BiddingControls(price = price, deadline = date)
    }
}
