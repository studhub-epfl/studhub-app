package com.studhub.app.core.utils

fun isValidSwissPhoneNumber(phoneNumber: String): Boolean {
    // Swiss phone numbers can start with +41, 0041, 0 or no prefix followed by 9 digits
    val regex = Regex("^((\\+41|0041|0)?[1-9]\\d{1}\\s?\\d{3}\\s?\\d{2}\\s?\\d{2})$")
    return regex.matches(phoneNumber)
}
