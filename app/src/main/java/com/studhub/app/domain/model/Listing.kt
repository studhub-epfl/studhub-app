package com.studhub.app.domain.model

data class Listing(
        var id: Long = 0L,
        var name: String = "",
        var description: String = "",
        var seller: User = User(),
        var price: Float = 0F,
        var categories: List<Category> = emptyList()
)
