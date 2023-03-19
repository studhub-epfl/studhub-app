package com.studhub.app.domain.model

import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver

data class Category(
    val id: String = "",
    val name: String = "",
    val description: String = "",

    val CategorySaver: Saver<Category, Any> = listSaver(
        save = { listOf(it.id, it.name) },
        restore = { Category(id = it[0] as String, name = it[1] as String) }
    )

)
