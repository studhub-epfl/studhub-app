package com.studhub.app.categories

import com.studhub.app.domain.model.Category

class Categories {
    val Electronics: Category = Category(id = 1, name = "Electronics", "phones/cameras/.. ")
    val SchoolsItem: Category = Category(id = 2, name = "School items","books/pencils/bags/..")
    val accessories: Category = Category(id = 8, name = "accessories","keys/necklaces/..")
    val insruments: Category = Category(id = 3, name = "insruments","ear phones/guitar/..")
    val mobility: Category = Category(id = 4, name = "mobility","bikes/scooter/..")
    val clothes: Category = Category(id = 5, name = "clothes","pants/shirts/..")
    val artdecorations: Category = Category(id = 6, name = "artdecorations","paintings/tapis/..")
    val services: Category = Category(id = 7, name = "services","online services/apps/supports/..")
    val other: Category = Category(id = 9, name = "other","other")
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


