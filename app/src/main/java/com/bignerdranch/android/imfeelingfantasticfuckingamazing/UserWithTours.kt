package com.bignerdranch.android.imfeelingfantasticfuckingamazing

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class UserWithTours(
    @Embedded val user: User,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(UserTourCrossRef::class, parentColumn = "userId", entityColumn = "tourId")
    )
    val tours: List<Tour>
)