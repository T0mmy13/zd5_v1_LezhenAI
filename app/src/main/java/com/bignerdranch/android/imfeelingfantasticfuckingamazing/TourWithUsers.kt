package com.bignerdranch.android.imfeelingfantasticfuckingamazing

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class TourWithUsers(
    @Embedded val tour: Tour,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(UserTourCrossRef::class, parentColumn = "tourId", entityColumn = "userId")
    )
    val users: List<User>
)