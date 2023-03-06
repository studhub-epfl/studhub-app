package com.studhub.app.domain.model

data class Listing(
        var id: Long = 0L,
        var name: String = "",
        var description: String = "",
        var seller: User,
        var price: Float = 0F,
// TODO - CREATE CATEGORY MODEL
        var categories: List<Any> = emptyList()
)
