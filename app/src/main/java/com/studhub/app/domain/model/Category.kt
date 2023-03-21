package com.studhub.app.domain.model

import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver

data class Category(
    val id: String = "",
    val name: String = "",
    val description: String = ""
)
