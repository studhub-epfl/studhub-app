package com.studhub.app.domain.model

import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver

open class Category(
    open val id: String = "",
    open val name: String = "",
    open val description: String = "",
    open val parentCategoryId: String? = null,
    val subCategories: List<Category> = emptyList()
)
