package com.studhub.app.categories

import com.studhub.app.domain.model.Category

class Categories {
    val Electronics: Category = Category(id = 1, name = "Electronics")
    val SchoolsItem: Category = Category(id = 1, name = "School items")
}



enum class CategoriesEnum(val description: String) {
    ELECTRONICS("electronics"),
    SCHOOLSITEMS("school items"),
    INSTRUMENTS("music instruments"),
    MOBILITY("bikes/scooter/.."),
    CLOTHES("clothes"),
    ARTDECORATIONS("art decoration"),
    SERVICES("services"),
    OTHER("other")
}



