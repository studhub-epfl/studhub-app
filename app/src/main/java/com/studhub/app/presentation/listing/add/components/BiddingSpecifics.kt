package com.studhub.app.presentation.listing.add.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.studhub.app.R
import com.studhub.app.annotations.ExcludeFromGeneratedTestCoverage
import com.studhub.app.presentation.ui.theme.StudHubTheme
import java.util.*

/**
 * This composable renders the controls and form part responsible to set the type of the listing
 * and if it's a bidding, to display a date picker
 *
 * @param checked a boolean state to decide whether the switch is ticked on or off to display
 * controls
 * @param date the date picker state that holds the input data.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BiddingSpecifics(checked: MutableState<Boolean>, date: DatePickerState) {
    Column {
        Row (verticalAlignment = Alignment.CenterVertically) {
            Switch(
                modifier = Modifier.semantics { contentDescription = "Auction" },
                checked = checked.value,
                onCheckedChange = { checked.value = it })
            Spacer(modifier = Modifier.width(10.dp))
            Text(stringResource(R.string.auction_mode_text))
        }
        if (checked.value) {
            BiddingDatePicker(date = date)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BiddingDatePicker(date: DatePickerState) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        DatePicker(
            state = date,
            title = {Text(text = stringResource(R.string.auction_deadline_select))} ,
            modifier = Modifier.padding(16.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@ExcludeFromGeneratedTestCoverage
@Preview(showBackground = true)
@Composable
fun BiddingSpecificsPreview() {
    val checked = remember { mutableStateOf(true) }
    val date = rememberDatePickerState(initialDisplayMode = DisplayMode.Input)
    date.selectedDateMillis
    StudHubTheme {
        BiddingSpecifics(checked, date)
    }
}
