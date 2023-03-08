package com.studhub.app.categories

import com.studhub.app.domain.model.Category

class Categories {
    val Electronics: Category = Category(id = 1, name = "Electronics")
    val SchoolsItem: Category = Category(id = 2, name = "School items")
    val insruments: Category = Category(id = 3, name = "insruments")
    val mobility: Category = Category(id = 4, name = "mobility")
    val clothes: Category = Category(id = 5, name = "clothes")
    val artdecorations: Category = Category(id = 6, name = "artdecorations")
    val services: Category = Category(id = 7, name = "services")
    val other: Category = Category(id = 8, name = "other")
}


/*
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
*/


