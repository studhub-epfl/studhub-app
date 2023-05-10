package com.studhub.app.core.utils

enum class PriceValidationResult {
    VALID,
    EMPTY,
    NON_NUMERIC,
    NEGATIVE
}

fun validatePrice(price: String): PriceValidationResult {
    if (price.isEmpty()) {
        return PriceValidationResult.EMPTY
    }
    val parsedPrice = price.toDoubleOrNull()
    return when {
        parsedPrice == null -> PriceValidationResult.NON_NUMERIC
        parsedPrice < 0 -> PriceValidationResult.NEGATIVE
        else -> PriceValidationResult.VALID
    }
}
