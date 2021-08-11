package com.buslaev.myfinance.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.buslaev.myfinance.entities.Categories
import com.buslaev.myfinance.entities.Operation

data class OperationAndCategories(
    @Embedded val operation: Operation,
    @Relation(
        parentColumn = "idCategory",
        entityColumn = "idCategory"
    )
    val categories: Categories?=null
)
