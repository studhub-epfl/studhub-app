package com.studhub.app.presentation.listing.add.components

import android.net.Uri
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.studhub.app.R
import com.studhub.app.core.utils.PriceValidationResult
import com.studhub.app.core.utils.validatePrice
import com.studhub.app.domain.model.Category
import com.studhub.app.domain.model.MeetingPoint
import com.studhub.app.presentation.ui.common.button.BasicFilledButton
import com.studhub.app.presentation.ui.common.container.Carousel
import com.studhub.app.presentation.ui.common.input.BasicTextField
import com.studhub.app.presentation.ui.common.input.ImagePicker
import com.studhub.app.presentation.ui.common.input.TextBox
import com.studhub.app.presentation.ui.common.misc.Spacer

/**
 * The form to create a listing
 * Params are all the mutable state passed from the parent to be used in the different parts of the
 * form itself.
 * @param onSubmit is the handler of the form submission
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListingForm(
    chosen: SnapshotStateList<Category>,
    title: MutableState<String>,
    description: MutableState<String>,
    price: MutableState<String>,
    meetingPoint: MutableState<MeetingPoint?>,
    pictures: MutableList<Uri>,
    onSubmit: () -> Unit,
    onSaveDraft: () -> Unit,
    openCategorySheet: MutableState<Boolean>,
    checked: MutableState<Boolean>,
    date: DatePickerState
) {
    BasicTextField(
        label = stringResource(R.string.listings_add_form_title),
        rememberedValue = title
    )

    Spacer("large")

    if (pictures.isNotEmpty()) {
        Carousel(modifier = Modifier.fillMaxWidth(0.8F), pictures = pictures)
    }

    ImagePicker(onNewPicture = { pictures.add(it) })

    Spacer("large")

    TextBox(
        label = stringResource(R.string.listings_add_form_description),
        rememberedValue = description
    )

    Spacer("large")

    CategoryChoice(chosen = chosen, openCategorySheet = openCategorySheet)

    Spacer("large")


    PriceRow(rememberedValue = price)

    Spacer("large")

    BiddingSpecifics(checked = checked, date = date)

    Spacer("large")

    MeetingPointInput(meetingPoint = meetingPoint)

    Spacer()


    val priceValidationResult = validatePrice(price.value)

    if (priceValidationResult != PriceValidationResult.VALID) {
        Text(
            text = when (priceValidationResult) {
                PriceValidationResult.NEGATIVE -> stringResource(R.string.listing_add_form_validation_neg_price)
                PriceValidationResult.NON_NUMERIC -> stringResource(R.string.listing_add_form_validation_numeric_price)
                else -> ""
            },
            modifier = Modifier.padding(4.dp),
            color = Color.Red
        )
    }

    // Check if the category is selected and the price is non-negative
    val isFormValid = (priceValidationResult == PriceValidationResult.VALID) && chosen.isNotEmpty()
    Row(
        modifier = Modifier.padding(32.dp),
    ) {

        BasicFilledButton(
            label = stringResource(R.string.listings_add_form_save_draft),
            onClick = {
                onSaveDraft()
            }
        )

        BasicFilledButton(
            label = stringResource(R.string.listings_add_form_send),
            enabled = isFormValid,
            onClick = {
                if (isFormValid) {
                    onSubmit()
                }
            }
        )
    }
}
